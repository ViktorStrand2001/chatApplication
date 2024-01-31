package com.viktor.chatApplication.services;

import com.viktor.chatApplication.models.MessageModel;
import com.viktor.chatApplication.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final IMessageRepository iMessageRepository;

    @Autowired
    public MessageService(IMessageRepository iMessageRepository) {
        this.iMessageRepository = iMessageRepository;
    }

    /////////////////////////  METHODS  /////////////////////////////

    public List<MessageModel> massageHistory(){
        return iMessageRepository.findAll();
    }

    public MessageModel sendMessage(MessageModel messageModel, Authentication authentication){

        String user = authentication.getName();

        messageModel.setSender(user);
        return iMessageRepository.save(messageModel);
    }

    public ResponseEntity<MessageModel> editMessage(Long id, MessageModel message) {
        Optional<MessageModel> existingMessageOptional = iMessageRepository.findById(id);

        if (existingMessageOptional.isPresent()) {
            MessageModel existingMessage = existingMessageOptional.get();

            // Update only the non-null fields from the input message
            if (message.getSender() != null) {
                existingMessage.setSender(message.getSender());
            }

            if (message.getContent() != null) {
                existingMessage.setContent(message.getContent());
            }

            if (message.getId() != null){
                existingMessage.setId(message.getId());
            }

            iMessageRepository.save(existingMessage);

            return new ResponseEntity<>(existingMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
