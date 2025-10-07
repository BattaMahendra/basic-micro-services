// UserMapper.java
package com.mahi.user.mapper;

import com.mahi.user.dto.UserRequestDto;
import com.mahi.user.dto.UserResponseDto;
import com.mahi.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User toEntity(UserRequestDto dto) {
        return modelMapper.map(dto, User.class);
    }

    public UserResponseDto toDto(User entity) {
        return modelMapper.map(entity, UserResponseDto.class);
    }
}
