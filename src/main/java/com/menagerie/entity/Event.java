package com.menagerie.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.menagerie.constants.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Constants.EVENT_TABLE_NAME)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = Constants.PET_ID_JOIN_COLUMN)
    @JsonIgnore
    private Pet pet;

    @Column(name = "date")
    private LocalDate date;
    @Column(length = 15)
    private String type;

    @Column(length = 255)
    private String remark;



}
