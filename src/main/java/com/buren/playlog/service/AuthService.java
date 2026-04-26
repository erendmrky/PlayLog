package com.buren.playlog.service;

import com.buren.playlog.dto.LoginRequestDTO;
import com.buren.playlog.dto.TokenResponseDTO;
import com.buren.playlog.exceptions.PasswordException;
import com.buren.playlog.model.BlacklistToken;
import com.buren.playlog.model.User;
import com.buren.playlog.repository.BlacklistTokenRepository;
import com.buren.playlog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BlacklistTokenRepository blacklistTokenRepository;

    private final Logger logger = LoggerFactory.getLogger("AUTHENTICATION_SERVICE");

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,BlacklistTokenRepository blacklistTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.blacklistTokenRepository = blacklistTokenRepository;
    }

    public TokenResponseDTO login(LoginRequestDTO loginDTO) {
        User user = userRepository.findByUsername(loginDTO.username())
                .orElseThrow(() -> new EntityNotFoundException(getClass().getSimpleName() + " with name " + loginDTO.username() + " not found or inactive"));
        if(passwordEncoder.matches(loginDTO.password(), user.getPassword())){
            logger.info("User {} logged in successfully", user.getUsername());
            return new TokenResponseDTO(jwtService.generateToken(user.getId(), user.getUsername()));
        }
        throw new PasswordException("Password is incorrect");
    }

    public void logout(String token) {
        String username = jwtService.extractUserName(token);
        BlacklistToken blacklist = new BlacklistToken();
        blacklist.setToken(token);
        blacklist.setActive(false);
        blacklistTokenRepository.save(blacklist);
        logger.info("User {} logged out successfully", username);
    }
}
