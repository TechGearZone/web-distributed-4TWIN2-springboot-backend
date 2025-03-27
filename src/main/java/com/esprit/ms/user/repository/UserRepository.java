package com.esprit.ms.user.repository;

import com.esprit.ms.user.model.User;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @QueryHints(@QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}