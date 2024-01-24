package com.viktor.chatApplication.services;

import com.viktor.chatApplication.config.AppPasswordConfig;
import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        user.setId(UUID.randomUUID());
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

    // DELETE
    public void deleteUser(UUID id) {
        iUserRepository.deleteById(id);
    }

    // Kollar vad användaren har för status
    // User Status
    private final Set<UserModel> onlineUsers = new LinkedHashSet<>();

    public Set<UserModel> getOnlineUsers() {
        return onlineUsers;
    }

    public void addOnlineUser(UserModel userModel) {
        onlineUsers.add(userModel);
    }

    public void removeOnlineUser(UserModel userModel) {
        onlineUsers.remove(userModel);
    }

    // implements from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return iUserRepository.findUserByUsername(username);
    }
}
