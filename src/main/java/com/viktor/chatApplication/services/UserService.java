package com.viktor.chatApplication.services;

import com.viktor.chatApplication.config.AppPasswordConfig;
import com.viktor.chatApplication.models.MessageModel;
import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {

    private final IUserRepository iUserRepository;
    private final AppPasswordConfig appPasswordConfig;
    @Autowired
    public UserService(IUserRepository iUserRepository, AppPasswordConfig appPasswordConfig) {
        this.iUserRepository = iUserRepository;
        this.appPasswordConfig = appPasswordConfig;
    }

    // CRUD funktionalitet
    // CREATE
    public UserModel createUser(UserModel user) {
        user.setPassword(appPasswordConfig.bCryptPasswordEncoder().encode(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountEnabled(true);
        user.setCredentialsNonExpired(true);

        return iUserRepository.save(user);
    }

    // PUTs
    public ResponseEntity<UserModel> updateUser(UUID id, UserModel user) {
        Optional<UserModel> existingUserOptional = iUserRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            UserModel existingUser = existingUserOptional.get();

            // Update only the non-null fields from the input user
            if (user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            }

            if (user.getPassword() != null) {
                existingUser.setPassword(user.getPassword());
            }

            iUserRepository.save(existingUser);

            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // GETs
    public List<UserModel> getAllUsers() {
        return iUserRepository.findAll();
    }

    public Optional<UserModel> getById(UUID id) {
        return iUserRepository.findById(id);
    }
    // DELETE
    public void deleteUser(Optional<UserModel> user) {
        user.ifPresent(userModel -> iUserRepository.deleteById(userModel.getId()));
    }

    // implements from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel userModel = iUserRepository.findByUsername(username);

        return userModel;
    }

}
