package org.interview.projectinterview.repositories;

import org.interview.projectinterview.models.InvalidDateToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidDateTokenRepository extends JpaRepository<InvalidDateToken, String> {
}
