package io.rainett.adminmicroservice.model;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "admins", uniqueConstraints = {
        @UniqueConstraint(name = "admin_username_uq", columnNames = "username"),
        @UniqueConstraint(name = "admin_email_uq", columnNames = "email")
})
@AllArgsConstructor
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

}