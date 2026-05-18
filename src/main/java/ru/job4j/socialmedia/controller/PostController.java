package ru.job4j.socialmedia.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.job4j.socialmedia.dto.PostRequestDto;
import ru.job4j.socialmedia.model.Post;
import ru.job4j.socialmedia.service.PostService;

import java.util.List;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<Post> getAll() {
        return postService.findAll();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> get(@PathVariable @Min(1) Integer postId) {
        return postService.findById(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> create(@Valid @RequestBody PostRequestDto request) {
        return postService.create(
                request.getAccountId(),
                request.getTitle(),
                request.getText(),
                request.getImages()
        ).map(post -> {
            var uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(post.getId())
                    .toUri();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .location(uri)
                    .body(post);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Void> update(
            @PathVariable @Min(1) Integer postId,
            @Valid @RequestBody PostRequestDto request
    ) {
        if (postService.update(request.getAccountId(), postId, request.getTitle(), request.getText())) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{postId}/account/{accountId}")
    public ResponseEntity<Void> delete(
            @PathVariable @Min(1) Integer postId,
            @PathVariable @Min(1) Integer accountId
    ) {
        if (postService.delete(accountId, postId)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
