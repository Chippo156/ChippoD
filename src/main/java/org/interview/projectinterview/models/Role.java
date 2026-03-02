package org.interview.projectinterview.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "Role")
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    private String roleName;
}
