package com.bobgarage.userservice.mappers;

import com.bobgarage.userservice.dtos.RegisterUserRequest;
import com.bobgarage.userservice.dtos.UpdateUserRequest;
import com.bobgarage.userservice.dtos.UserDto;
import com.bobgarage.userservice.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(RegisterUserRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void update(UpdateUserRequest request, @MappingTarget User user);
}