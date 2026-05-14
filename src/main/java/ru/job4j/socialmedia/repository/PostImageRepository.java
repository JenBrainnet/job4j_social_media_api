package ru.job4j.socialmedia.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.socialmedia.model.PostImage;

public interface PostImageRepository extends CrudRepository<PostImage, Integer> {
}
