package com.example.ucitelnice.mapper;

import com.example.ucitelnice.dto.CurrentUserResponse;
import com.example.ucitelnice.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {


    CurrentUserResponse toResponse(User user);
}
