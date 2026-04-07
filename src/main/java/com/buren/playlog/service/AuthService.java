package com.buren.playlog.service;

import com.buren.playlog.dto.LoginRequestDTO;
import com.buren.playlog.dto.TokenResponseDTO;
import com.buren.playlog.exceptions.PasswordException;
import com.buren.playlog.exceptions.WrongUsernameOrPasswordException;
import com.buren.playlog.model.User;
import com.buren.playlog.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenResponseDTO login(LoginRequestDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.username())
                .orElseThrow(() -> new WrongUsernameOrPasswordException(getClass().getSimpleName() + " with name " + loginDTO.username() + " not found or inactive"));
        if(passwordEncoder.matches(loginDTO.password(), user.getPassword())){
            return new TokenResponseDTO(jwtService.generateToken(user.getId(), user.getUsername()));
        }
        throw new PasswordException("Password is incorrect");
    }

}
