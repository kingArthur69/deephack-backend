package com.proton.backend.controller;

import com.proton.backend.dto.UserDTO;
import com.proton.backend.mapper.UserMapper;
import com.proton.backend.model.User;
import com.proton.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> findUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(UserMapper.toUserDTO(userService.find(id)));
        } catch (Exception e) {
            log.error("Error while getting user", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<List<UserDTO>> findUsersByPage(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        try {
            PageRequest pageRequest = PageRequest.of(page, 25);
            return ResponseEntity.ok(userService.find(pageRequest));
        } catch (Exception e) {
            log.error("Error while getting users", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<UserDTO> addNewUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            User user = userService.save(UserMapper.toUser(userDTO));
            return ResponseEntity.ok(UserMapper.toUserDTO(user));
        } catch (Exception e) {
            log.error("Error while saving user", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            User user = UserMapper.toUser(userDTO);
            user.setId(id);
            return ResponseEntity.ok(UserMapper.toUserDTO(userService.save(user)));
        } catch (Exception e) {
            log.error("Error while updating user", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error while deleting user", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
