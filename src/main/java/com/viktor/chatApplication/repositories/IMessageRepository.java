package com.viktor.chatApplication.repositories;

import com.viktor.chatApplication.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMessageRepository extends JpaRepository<MessageModel, Long> {

    @Override
    List<MessageModel> findAll();

    Optional<MessageModel> findById(Long id);

    void deleteById(Long id);
}
