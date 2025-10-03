package com.example.user_service.serviceImpl;

import com.example.user_service.dto.UserInformationDto;
import com.example.user_service.entity.UserInformation;
import com.example.user_service.mapper.UserInformationMapper;
import com.example.user_service.repository.UserInformationRepository;
import com.example.user_service.service.UserInformationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInformationServiceImpl implements UserInformationService {

    private final UserInformationRepository informationRepository;
    private final UserInformationMapper mapper;

    @Override
    public UserInformationDto getUserInformationById(Long userId) {
        UserInformation info = informationRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("User Not Found")
        );
        return mapper.toDto(info);
    }

    @Override
    public UserInformationDto updateUserInformation(Long userId, UserInformationDto dto) {
        UserInformation info = informationRepository.findByUserId(userId).orElseThrow(() ->
                new EntityNotFoundException("User Not Found"));
        info.setAddress(dto.address());
        info.setDob(dto.dob());
        info.setFull_name(dto.full_name());
        info.setPhone_number(dto.phone_number());
        info.setGender(dto.gender());
        informationRepository.save(info);
        return mapper.toDto(info);
    }

}
