package com.lcwd.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CategoryDto {
    private String categoryId;

    @NotBlank
    private String title;

    @NotBlank(message = "Decription Required")
    private String description;

    //@NotBlank(message = "")
    private String coverImage;
}
