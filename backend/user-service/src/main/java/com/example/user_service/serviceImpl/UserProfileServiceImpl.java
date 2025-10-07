package com.example.user_service.serviceImpl;

import com.example.user_service.dto.UserProfileDto;
import com.example.user_service.entity.UserProfile;
import com.example.user_service.mapper.UserProfileMapper;
import com.example.user_service.repository.UserProfileRepository;
import com.example.user_service.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    @Override
    public UserProfileDto getUserProfileById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        return userProfileMapper.toDto(userProfile);
    }

    // null kontrolune gerek yok
    @Override
    public UserProfileDto updateUserProfile(Long id ,UserProfileDto userProfileDto) {
        UserProfile userProfile = userProfileRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found"));
        if(userProfileDto.address() !=null) {
            userProfile.setAddress(userProfileDto.address());
        }
        if (userProfileDto.gender() !=null) {
            userProfile.setGender(userProfileDto.gender());
        }
        if (userProfileDto.full_name() !=null ) {
            userProfile.setFull_name(userProfileDto.full_name());
        }
        if (userProfileDto.phone_number()!=null) {
            userProfile.setPhone_number(userProfileDto.phone_number());
        }
        if(userProfileDto.dob() !=null) {
            userProfile.setDob(userProfileDto.dob());
        }
        return userProfileMapper.toDto(userProfile);
    }
}
