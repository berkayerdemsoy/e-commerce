package com.example.user_service.serviceImpl;

import com.example.user_service.config.PasswordEncoderConfig;
import com.example.user_service.dto.*;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtService;
import com.example.user_service.security.UserPrincipal;
import com.example.user_service.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoderConfig paswordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;

    @Override
    @RequestMapping(path = "delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        userRepository.deleteById(user.getId());
    }

    @Override
    public Optional<UserResponseDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDto);
    }

    @Override
    public AuthResponse registerUser(UserRegisterRequestDto dto) {
        if (userRepository.existsByEmail(dto.email()) || userRepository.existsByUsername(dto.username())){
            throw new RuntimeException("Username or email already exists");
        }

        User user = userMapper.toEntityFromRegister(dto);
        user.setPassword(paswordEncoder.passwordEncoder().encode(dto.password()));
        user.setRoles(Set.of(Role.USER));
        User saved = userRepository.save(user);

        String jwtToken = jwtService.generateToken(new UserPrincipal(saved));
        return new AuthResponse(jwtToken);
    }

    @Override
    public AuthResponse loginUser(UserLoginRequestDto dto) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(),dto.password())
        );

        User user = userRepository.findByUsername(dto.username()).orElseThrow(
                () -> new RuntimeException("User not found")
        );

        String jwtToken = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse(jwtToken);

    }

    @Override
    public Page<UserResponseDto> getALlUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUserById(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRoles(userUpdateRequestDto.roles());
        user.setPassword(userUpdateRequestDto.password());
        user.setEmail(userUpdateRequestDto.email());
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }


}
