package ru.job4j.socialmedia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.job4j.socialmedia.model.PostImage;

import java.util.List;

@Data
public class PostRequestDto {

    @NotNull(message = "accountId must not be null")
    @Min(value = 1, message = "accountId must be 1 or greater")
    private Integer accountId;

    @NotBlank(message = "title must not be blank")
    private String title;

    @NotBlank(message = "text must not be blank")
    private String text;

    private List<PostImage> images;

}
