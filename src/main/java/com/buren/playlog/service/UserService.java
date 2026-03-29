package com.buren.playlog.service;

import com.buren.playlog.dto.UserRequestDTO;
import com.buren.playlog.dto.UserResponseDTO;
import com.buren.playlog.model.User;
import com.buren.playlog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User, Long>{

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public UserResponseDTO getUser(Long id) {
        return dtoFrom(super.get(id));
    }

    public UserResponseDTO add(UserRequestDTO dto) {
        User user = new User();
        user.setEmail(dto.email());
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));

        return dtoFrom(abstractRepository.save(user));
    }

    private static UserResponseDTO dtoFrom(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
