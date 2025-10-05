package com.example.user_service.serviceImpl;

import com.example.user_service.config.PasswordEncoderConfig;
import com.example.user_service.dto.UserLoginDto;
import com.example.user_service.dto.UserRegisterDto;
import com.example.user_service.dto.UserResponseDto;
import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.entity.UserPrincipal;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.security.JwtService;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;


    @Override
    public String register(UserRegisterDto userRegisterDto) {
        if (userRepository.findByUsername(userRegisterDto.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(userRegisterDto.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .username(userRegisterDto.username())
                .email(userRegisterDto.email())
                .password(passwordEncoderConfig.passwordEncoder().encode(userRegisterDto.password()))
                .roles(Set.of(Role.USER))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        User savedUser = userRepository.save(user);
        UserDetails userDetails = new UserPrincipal(savedUser);

        return jwtService.generateToken(userDetails);
    }

    @Override
    public String login(UserLoginDto userLoginDto) {
        User user = userRepository.findByUsername(userLoginDto.username())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userLoginDto.username(),
                userLoginDto.password()
        ));

        if (!passwordEncoderConfig.passwordEncoder().matches(userLoginDto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
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
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public Void deleteUserById(Long id) {
        userRepository.deleteById(id);
        return null;
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto updateUserById(Long id, UserRegisterDto userRegisterDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
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


}
