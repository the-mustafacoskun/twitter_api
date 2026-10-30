package com.workintech.TwitterApi.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="user_name")
    @NotNull(message="Username cannot be null")
    @NotBlank(message="Username cannot be blank")
    private String username;

    @Email
    @Column(name="email")
    @NotNull(message="Email cannot be null")
    @NotBlank(message="Email cannot be blank")
    private String email;

    @Column(name="password")
    @NotNull(message="Password cannot be null")
    @NotBlank(message="Password cannot be blank")
    private String password;

}
