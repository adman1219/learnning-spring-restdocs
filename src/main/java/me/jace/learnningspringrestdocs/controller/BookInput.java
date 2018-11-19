package me.jace.learnningspringrestdocs.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookInput {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Max(100)
    private String subject;

    @NotBlank
    @Max(255)
    private String description;

    @NotBlank
    @Max(100)
    private String author;

    private PublisherInput publisher;
}
