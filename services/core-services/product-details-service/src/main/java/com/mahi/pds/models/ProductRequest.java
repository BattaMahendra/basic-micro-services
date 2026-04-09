package com.mahi.pds.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductRequest {


    private Long id;

    @NotNull
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    private Double price;

    @Size(max = 1000)
    private String description;
    @NotBlank
    private String category;
    private String image;
}
