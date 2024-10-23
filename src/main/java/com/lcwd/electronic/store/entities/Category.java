package com.lcwd.electronic.store.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "category_title",length = 60, nullable = false)
    private String title;

    @Column(name = "category_desc", length = 500)
    private String description;

    private String coverImage;


    //for mapping with categories
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    private List <Product> products = new ArrayList<>();

}