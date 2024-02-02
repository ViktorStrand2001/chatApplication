package com.viktor.chatApplication.services;

import com.viktor.chatApplication.models.MessageModel;
import com.viktor.chatApplication.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /////////////////////////  GET  /////////////////////////////
    public List<MessageModel> massageHistory(){
        return iMessageRepository.findAll();
    }
    public Optional<MessageModel> getById(Long messageId){
        return iMessageRepository.findById(messageId);
    }

    /////////////////////////  POST  /////////////////////////////
    public MessageModel sendMessage(MessageModel messageModel, Authentication authentication){

        String user = authentication.getName();

        messageModel.setSender(user);
        return iMessageRepository.save(messageModel);
    }

    /////////////////////////  PUT  /////////////////////////////
    public String editMessage(Long id, MessageModel unEditedMessage) {
        Optional<MessageModel> unEditedMessageOptional = iMessageRepository.findById(id);

        if (unEditedMessageOptional.isPresent()) {
            MessageModel editedMessage = unEditedMessageOptional.get();

            // Update only the non-null fields from the input user
            if (unEditedMessage.getContent() != null) {
                editedMessage.setContent(unEditedMessage.getContent());
            }

            if (unEditedMessage.getSender() != null) {
                editedMessage.setSender(unEditedMessage.getSender());
            }

            if (unEditedMessage.getId()!= null) {
                editedMessage.setId(unEditedMessage.getId());
            }

        iMessageRepository.save(editedMessage);
            return "Message Edited";
        }
        return "Message not edited";
    }

    /////////////////////////  DELETE  /////////////////////////////
    public void deleteMessage(Optional<MessageModel> message) {
        if (message.isPresent()) {
            iMessageRepository.deleteById(message.get().getId());
        }
    }
}
