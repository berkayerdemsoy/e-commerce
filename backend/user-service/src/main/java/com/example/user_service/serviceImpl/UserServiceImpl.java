package com.example.user_service.serviceImpl;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @RequestMapping(path = "delete/{id}")
    public void deleteUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        userRepository.deleteById(user.getId());
    }
}
