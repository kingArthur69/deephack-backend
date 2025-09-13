package com.proton.backend.service;

import com.proton.backend.model.Meter;
import com.proton.backend.model.User;
import com.proton.backend.repository.MeterRepository;
import com.proton.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeterService {

    private final UserService userService;
    private final MeterRepository meterRepository;

    public List<Meter> findAllByUserId(Long id) {
        User user = userService.find(id);
        return meterRepository.findAllByUser(user);
    }

    public Meter save(Meter meter) {
        return meterRepository.save(meter);
    }
}
