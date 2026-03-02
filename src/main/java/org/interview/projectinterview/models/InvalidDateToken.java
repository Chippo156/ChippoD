package org.interview.projectinterview.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity(name = "InvalidDateToken")
@Table(name = "invalid_date_tokens")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidDateToken {
    @Id
    private String id;
    @Column(name = "expiry_time", nullable = false)
    private Date expiryTime;
}
