package com.example.user_service_app.serviceImpl;

import com.example.user_service_app.config.PasswordEncoderConfig;
import com.example.user_service_client.dto.UserLoginDto;
import com.example.user_service_client.dto.UserRegisterDto;
import com.example.user_service_client.dto.UserResponseDto;
import com.example.user_service_client.dto.UserRoleResponse;
import com.example.user_service_app.entity.Role;
import com.example.user_service_app.entity.User;
import com.example.user_service_app.entity.UserPrincipal;
import com.example.user_service_app.entity.UserProfile;
import com.example.user_service_app.exception.AlreadyExistsException;
import com.example.user_service_app.exception.InvalidCredentialsException;
import com.example.user_service_app.exception.UserNotFoundException;
import com.example.user_service_app.mapper.UserMapper;
import com.example.user_service_app.repository.UserProfileRepository;
import com.example.user_service_app.repository.UserRepository;
import com.example.user_service_app.security.JwtService;
import com.example.user_service_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserProfileRepository userProfileRepository;


    @Override
    public String register(UserRegisterDto userRegisterDto) {
        if (userRepository.findByUsernameIgnoreCase(userRegisterDto.username()).isPresent()) {
            throw new AlreadyExistsException("User already exists");
        }
        if (userRepository.findByEmail(userRegisterDto.email()).isPresent()) {
            throw new AlreadyExistsException("Email already exists");
        }
        User user = User.builder()
                .username(userRegisterDto.username())
                .email(userRegisterDto.email())
                .password(passwordEncoderConfig.passwordEncoder().encode(userRegisterDto.password()))
                .roles(new HashSet<>(Collections.singleton(Role.USER)))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .address(userRegisterDto.address())
                .dob(userRegisterDto.dob())
                .gender(userRegisterDto.gender())
                .full_name(userRegisterDto.full_name())
                .phone_number(userRegisterDto.phone_number())
                .build();
        userProfileRepository.save(userProfile);

        User savedUser = userRepository.save(user);
        UserDetails userDetails = new UserPrincipal(savedUser);

        return jwtService.generateToken(userDetails);
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        User user = userRepository.findByUsernameIgnoreCase(userLoginDto.username())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.username(),
                userLoginDto.password()
        ));

        if (!passwordEncoderConfig.passwordEncoder().matches(userLoginDto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        UserDetails userDetails = new UserPrincipal(user);
        return jwtService.generateToken(userDetails);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public Void deleteUserById(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUserById(Long id, UserRegisterDto userRegisterDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setUsername(userRegisterDto.username());
        if(userRegisterDto.email() !=null && !userRegisterDto.email().isBlank()) {
            user.setEmail(userRegisterDto.email());
        }
        if(userRegisterDto.password() !=null && !userRegisterDto.password().isBlank()){
            user.setPassword(passwordEncoderConfig.passwordEncoder().encode(userRegisterDto.password()));
        }
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public List<UserRoleResponse> getUsersByRole(String role) {
        Role enumRole;
        try {
            enumRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Geçersiz rol: " + role);
        }

        List<User> users = userRepository.findByRolesContaining(enumRole);

        return users.stream()
                .map(user -> new UserRoleResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().toString()
                ))
                .collect(Collectors.toList());
    }


}
