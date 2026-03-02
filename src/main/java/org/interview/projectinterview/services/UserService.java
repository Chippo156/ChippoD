package org.interview.projectinterview.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.interview.projectinterview.dto.request.UserCreationRequest;
import org.interview.projectinterview.dto.response.UserResponse;
import org.interview.projectinterview.enums.RoleEnum;
import org.interview.projectinterview.exception.AppException;
import org.interview.projectinterview.exception.ErrorCode;
import org.interview.projectinterview.mapper.UserMapper;
import org.interview.projectinterview.models.Role;
import org.interview.projectinterview.repositories.RoleRepository;
import org.interview.projectinterview.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreationRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        var user = userMapper.toUser(userRequest);
        Role role = roleRepository.findByRoleName(RoleEnum.USER_ROLE).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setActive(Boolean.TRUE);
        var savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserResponse(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
