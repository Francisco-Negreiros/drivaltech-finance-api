package com.drivaltech.finance.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password, Role role) {

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(username, encodedPassword, role);

        return userRepository.save(user);
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
