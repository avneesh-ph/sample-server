package com.pharynxai.julius.server.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pharynxai.julius.server.dto.UserDTO;
import com.pharynxai.julius.server.dto.UserDTOPayload;
import com.pharynxai.julius.server.model.Users;
import com.pharynxai.julius.server.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private Users convertToEntity(UserDTO userdto) {
        Users user = new Users();
        user.setEmail(userdto.email());
        user.setPassword(userdto.password());
        return user;
    }

    private UserDTOPayload convertToDTO(Users user) {
        return new UserDTOPayload(user.getId(), user.getEmail());
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        Users savedUsers = userRepository.save(user);
        return convertToDTO(savedUsers);
    }

    public Users updateUsers (Users user) {
        Users existingUsers = userRepository.findById(user.getId()).orElseThrow();
        existingUsers.setEmail("abc@gmail.com");
        Users updatedUsers = userRepository.save(user);

        return updatedUsers;
    }

    public void deleteUsersById(UUID id) {
        userRepository.deleteById(id);
    }
}
