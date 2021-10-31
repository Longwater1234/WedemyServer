package com.davistiba.wedemyserver.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "wishlist",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "course_id"}))
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wishlistId;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(nullable = false)
    private Instant createdAt;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wishlist wishlist = (Wishlist) o;

        if (!wishlistId.equals(wishlist.wishlistId)) return false;
        if (!user.equals(wishlist.user)) return false;
        if (!course.equals(wishlist.course)) return false;
        return Objects.equals(createdAt, wishlist.createdAt);
    }

    @Override
    public int hashCode() {
        int result = wishlistId.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + course.hashCode();
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }
}
