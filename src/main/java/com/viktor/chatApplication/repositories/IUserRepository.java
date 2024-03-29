package com.viktor.chatApplication.repositories;

import com.viktor.chatApplication.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, UUID> {

    @Override
    List<UserModel> findAll();

    void deleteById(UUID id);

    UserModel findByUsername(String username);

    Optional<UserModel> findById(UUID id);

}
