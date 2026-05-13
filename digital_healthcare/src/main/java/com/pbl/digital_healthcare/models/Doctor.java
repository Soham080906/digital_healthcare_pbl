package com.pbl.digital_healthcare.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String specialization;

    private String licenseNumber;

    private Integer experience;

    private String education;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "clinic_id")
    @JsonIgnoreProperties({"doctors"})
    private Clinic clinic;

    @OneToMany(mappedBy = "doctor")
    private List<Appointment> appointments;
}