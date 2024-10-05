package com.lcwd.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//lombok
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(name="id")
    private String categoryId;

    @NotBlank(message = "title is required!!")
    @Size(min=5,message = "title can not less than 5 character")
    @Column(name = "category_title",length = 60, nullable = false)
    private String title;

    @NotBlank(message = "title is required!!")
    @Column(name = "category_desc", length = 500)
    private String description;

    private String coverImage;
}