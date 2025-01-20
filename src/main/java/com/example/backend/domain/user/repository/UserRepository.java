// UserRepository.java
package com.example.backend.domain.user.repository;

import com.example.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByMatricule(String matricule);
}
