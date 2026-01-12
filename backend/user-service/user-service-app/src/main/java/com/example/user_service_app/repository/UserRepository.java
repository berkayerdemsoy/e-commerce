package com.example.user_service_app.repository;

import com.example.user_service_app.entity.Role;
import com.example.user_service_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRolesContaining(Role role);

}
