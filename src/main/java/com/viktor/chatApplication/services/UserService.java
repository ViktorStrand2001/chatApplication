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
        user.setPassword(appPasswordConfig.bCryptPasswordEncoder().encode(user.getPassword()));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountEnabled(true);
        user.setCredentialsNonExpired(true);

        return iUserRepository.save(user);
    }

    // PUTs
    public void updateUser(UUID id, UserModel user) {
        Optional<UserModel> existingUserOptional = iUserRepository.findById(id);

        try {
        if (existingUserOptional.isPresent()) {
            UserModel existingUser = existingUserOptional.get();

            existingUser.setAccountEnabled(user.isEnabled());
            existingUser.setAccountNonExpired(user.isAccountNonExpired());
            existingUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
            existingUser.setAccountNonLocked(user.isAccountNonLocked());

            if (user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            }

            if (!user.getPassword().equals(existingUser.getPassword())){
                existingUser.setPassword(appPasswordConfig.bCryptPasswordEncoder().encode(existingUser.getPassword()));
                System.out.println("new password : " + existingUser.getPassword());
            }else{
                existingUser.setPassword(user.getPassword());
            }

            if (user.getRole() != null) {
                existingUser.setRole(user.getRole());
            }

            if (
                existingUser.isAccountNonExpired() == false ||
                existingUser.isAccountNonLocked() == false ||
                existingUser.isCredentialsNonExpired() == false
            ){
                existingUser.setAccountEnabled(false);
            }else{
                existingUser.setAccountEnabled(true);
            }

            iUserRepository.save(existingUser);

            System.out.println();
        } else {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // GETs
    public List<UserModel> getAllUsers() {
        return iUserRepository.findAll();
    }

    public Optional<UserModel> getById(UUID id) {
        return iUserRepository.findById(id);
    }

    public boolean doesUsernameExist(String username) {
        UserModel existingUser = iUserRepository.findByUsername(username);
        return existingUser != null;
    }
    // DELETE
    public void deleteUser(Optional<UserModel> user) {
        try {
            user.ifPresent(userModel -> iUserRepository.deleteById(userModel.getId()));
        }catch (Exception e){
            System.out.println(e);
        }
    }

    // implements from UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel userModel = iUserRepository.findByUsername(username);

        return userModel;
    }

}
