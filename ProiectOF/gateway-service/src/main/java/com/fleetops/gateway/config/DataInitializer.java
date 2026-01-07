package com.fleetops.gateway.config;

import com.fleetops.gateway.model.User;
import com.fleetops.gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("Capitanu").isEmpty()) {
            User user = new User();
            user.setUsername("Capitanu");
            user.setPassword(passwordEncoder.encode("123"));
            userRepository.save(user);
        }
    }
}