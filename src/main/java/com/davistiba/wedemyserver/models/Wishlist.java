package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "wishlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishlistId;

    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @ManyToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "courseId")
    @JsonBackReference
    private Course course;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

}
