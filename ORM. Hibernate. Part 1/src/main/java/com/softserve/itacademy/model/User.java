package com.softserve.itacademy.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(generator = "user-generator")
    @GenericGenerator(
            name = "user-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "10"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(name = "first_name")
    @Pattern(regexp = "^[A-Z][a-z]+(-[A-Z][a-z]+)?$")
    private String firstName;

    @NotBlank
    @Column(name = "last_name")
    @Pattern(regexp = "^[A-Z][a-z]+(-[A-Z][a-z]+)?$")
    private String lastName;

    @NotBlank
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "todo_collaborator",
            schema = "public",
            joinColumns = @JoinColumn(name = "collaborator_id"),
            inverseJoinColumns = @JoinColumn(name = "todo_id")
    )
    private Set<ToDo> todo_collaborators = new HashSet<>();

    @OneToMany(mappedBy = "owner")
    private Set<ToDo> todos = new HashSet<>();

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
