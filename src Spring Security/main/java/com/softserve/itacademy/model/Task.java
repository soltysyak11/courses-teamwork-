package com.softserve.itacademy.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "todo_id")
    private ToDo todo;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    @Override
    public String toString() {
        return "Task {" +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", priority = " + priority +
                ", todo = " + todo +
                ", state = " + state +
                "} ";
    }
}
