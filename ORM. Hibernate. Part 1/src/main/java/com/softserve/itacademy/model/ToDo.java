package com.softserve.itacademy.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "todos")
public class ToDo {

    @Getter
    @Setter
    @Id
    @GeneratedValue(generator = "todo-generator")
    @GenericGenerator(
            name = "todo-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "todo_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "100"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    @Setter
    @Getter
    private LocalDateTime createdAt;

    @NotBlank
    @Setter
    @Getter
    private String title;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    @Setter
    @Getter
    @NotNull
    private User owner;

    @ManyToMany(mappedBy = "todo_collaborators")
    @Setter
    @Getter
    private Set<User> collaborators = new HashSet<>();

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "todo")
    @Setter
    @Getter
    private List<Task> tasks = new ArrayList<>();

}
