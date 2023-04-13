package com.api.pontoeletronico.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.pontoeletronico.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID>{
    boolean existsByLogin(String login);
    boolean existsByRegistration(String registration);
    Optional<UserModel> findByLoginAndPassword(String login, String password);
    Optional<UserModel> findByLogin(String login);
}
