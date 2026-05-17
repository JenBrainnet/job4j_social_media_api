package ru.job4j.socialmedia.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.model.PostImage;
import ru.job4j.socialmedia.repository.AccountRepository;
import ru.job4j.socialmedia.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Transactional
    public Optional<Post> create(Integer accountId, String title, String text, List<PostImage> images) {
        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            log.warn("Post was not created. Account with id={} was not found.", accountId);
            return Optional.empty();
        }
        var post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setAccount(accountOptional.get());
        if (images != null) {
            for (var image : images) {
                image.setPost(post);
                post.getImages().add(image);
            }
        }
        return Optional.of(postRepository.save(post));
    }

    @Transactional
    public boolean update(Integer accountId, Integer postId, String title, String text) {
        return postRepository.updateTitleAndTextByIdAndAccountId(
                postId, accountId, title, text
        ) > 0;
    }

    @Transactional
    public boolean delete(Integer accountId, Integer postId) {
        postRepository.deleteImagesByPostIdAndAccountId(postId, accountId);
        return postRepository.deletePostByIdAndAccountId(postId, accountId) > 0;
    }

}
