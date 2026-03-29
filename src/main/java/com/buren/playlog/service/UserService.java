package com.buren.playlog.service;

import com.buren.playlog.dto.UserRequestDTO;
import com.buren.playlog.dto.UserResponseDTO;
import com.buren.playlog.model.User;
import com.buren.playlog.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<User, Long>{

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
        user.setPassword(BCrypt.hashpw(dto.password(), BCrypt.gensalt()));

        return dtoFrom(abstractRepository.save(user));
    }

    private static UserResponseDTO dtoFrom(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
