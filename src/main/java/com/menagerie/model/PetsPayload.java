package com.menagerie.model;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetsPayload {

    private String name;
    private String owner;
    private String species;
    private PetSex sex;
    private LocalDate birth;
    private LocalDate death;

}
