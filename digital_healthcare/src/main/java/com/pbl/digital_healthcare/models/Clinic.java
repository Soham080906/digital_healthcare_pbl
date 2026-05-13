package com.pbl.digital_healthcare.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "clinics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;
    private String phone;
    private String email;
    private String hours;
    private String services;

    @OneToMany(mappedBy = "clinic")
    @JsonIgnore
    private List<Doctor> doctors;
}
