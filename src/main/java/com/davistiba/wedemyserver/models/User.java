package com.davistiba.wedemyserver.models;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID", nullable = false)
    private Integer userID;
    @Column(nullable = false)
    private String fullname;
    @Column(nullable = false, unique = true)
    @Email
    private String email;
    @Column(nullable = false)
    @JsonIgnore
    @Size(min = 8)
    private String password;
    @CreationTimestamp
    @Column(nullable = false)
    private Instant datejoined;

}
