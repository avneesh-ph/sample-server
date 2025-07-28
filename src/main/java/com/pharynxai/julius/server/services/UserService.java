package com.pharynxai.julius.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pharynxai.julius.server.dto.UserDTO;
import com.pharynxai.julius.server.dto.UserDTOPayload;
import com.pharynxai.julius.server.model.Users;
import com.pharynxai.julius.server.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private Users convertToEntity(UserDTO userdto) {
        Users user = new Users();
        user.setEmail(userdto.email());
        user.setPassword(userdto.password());
        return user;
    }

    private UserDTOPayload convertToDTO(Users user) {
        return new UserDTOPayload(user.getId(), user.getEmail());
    }

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTOPayload> getAllUsers() {
        List<UserDTOPayload> userList = new ArrayList<UserDTOPayload>();
        for (Users i : userRepository.findAll()) {
            userList.add(convertToDTO(i));
        }
        return userList;
    }

    public UserDTOPayload getUsersById(UUID id) {
        Optional<Users> optionalUsers = userRepository.findById(id);
        if (optionalUsers.isPresent()) {
            return convertToDTO(optionalUsers.get());
        }
        return null;
    }

    public UserDTOPayload saveUsers(UserDTO userdto) {
        Users user = convertToEntity(userdto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Users savedUsers = userRepository.save(user);
        return convertToDTO(savedUsers);
    }

    public UserDTOPayload updateUsers (UUID id, UserDTO user) {
        Users existingUsers = userRepository.findById(id).orElseThrow();
        existingUsers.setEmail(user.email());
        existingUsers.setPassword(user.password());
        Users updatedUsers = userRepository.save(existingUsers);
        return convertToDTO(updatedUsers);
    }

    public void deleteUsersById(UUID id) {
        userRepository.deleteById(id);
    }
}
