package br.com.fiap.agroorbit.services;

import br.com.fiap.agroorbit.dtos.request.LoginRequest;
import br.com.fiap.agroorbit.dtos.request.RegisterRequest;
import br.com.fiap.agroorbit.dtos.response.AuthResponse;
import br.com.fiap.agroorbit.exceptions.BusinessException;
import br.com.fiap.agroorbit.models.User;
import br.com.fiap.agroorbit.repositories.UserRepository;
import br.com.fiap.agroorbit.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @CacheEvict(value = {"users", "user", "dashboard"}, allEntries = true)
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(request.role())
                .build();

        User savedUser = userRepository.save(user);
        String token = tokenService.generateToken(savedUser);

        return AuthResponse.fromEntity(token, savedUser);
    }

    public AuthResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user);

        return AuthResponse.fromEntity(token, user);
    }
}