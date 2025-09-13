package com.proton.backend.service;

import com.proton.backend.dto.UserDTO;
import com.proton.backend.mapper.UserMapper;
import com.proton.backend.model.User;
import com.proton.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public User find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found by id: " + id));
    }

    public List<UserDTO> find(Pageable pageable) {
        return userRepository.findAll(pageable).get().map(UserMapper::toUserDTO).toList();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByIdnp(String idnp) {
        return userRepository.findByIdnp(idnp);
    }
}
