package com.graphqlguy.moviedb.user;

import com.graphqlguy.moviedb.exception.InvalidInputException;
import com.graphqlguy.moviedb.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public AuthResponse login(LoginInput input) {
        AppUser user = userRepository.findByUsername(input.username())
                .orElseThrow(() -> new InvalidInputException("credentials", "Invalid credentials"));
        if (!passwordEncoder.matches(input.password(), user.getPassword())) {
            throw new InvalidInputException("credentials", "Invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new AuthResponse(token, user);
    }
}