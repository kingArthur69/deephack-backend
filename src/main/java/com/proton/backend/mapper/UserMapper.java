package com.proton.backend.mapper;

import com.proton.backend.dto.UserDTO;
import com.proton.backend.model.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setIdnp(user.getIdnp());
        userDTO.setName(user.getName());
        userDTO.setSurname(userDTO.getSurname());
        return userDTO;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        user.setIdnp(userDTO.getIdnp());
        user.setName(userDTO.getName());
        user.setSurname(userDTO.getSurname());
        return user;
    }
}
