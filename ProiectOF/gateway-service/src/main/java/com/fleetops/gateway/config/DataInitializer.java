package com.fleetops.gateway.config;

import com.fleetops.gateway.model.User;
import com.fleetops.gateway.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${initial.admin.username}")
    private String adminUsername;

    @Value("${initial.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("Capitanu").isEmpty()) {
            User user = new User();
            user.setUsername(adminUsername);
            user.setPassword(passwordEncoder.encode(adminPassword));
            userRepository.save(user);
        }
    }
}