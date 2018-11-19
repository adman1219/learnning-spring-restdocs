package me.jace.learnningspringrestdocs.controller;

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
public class PublisherInput {
    @NotBlank
    @Max(100)
    private String name;
    private ContactInput contact;
}
