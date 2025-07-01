package com.menagerie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.menagerie.constants.Constants;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = Constants.PET_TABLE_NAME)
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20)
    private String owner;

    @Column(length = 20)
    private String species;

    @Column(length = 1)
    private String sex; // 'm' or 'f'

    private LocalDate birth;
    private LocalDate death;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Event> events;


}
