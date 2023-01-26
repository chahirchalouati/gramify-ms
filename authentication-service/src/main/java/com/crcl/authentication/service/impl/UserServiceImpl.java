package com.crcl.authentication.service.impl;

import com.crcl.authentication.clients.ProfileClient;
import com.crcl.authentication.clients.ServerStorageClient;
import com.crcl.authentication.domain.User;
import com.crcl.authentication.dto.CreateUserRequest;
import com.crcl.authentication.dto.UserDto;
import com.crcl.authentication.helpers.AuthenticationHelper;
import com.crcl.authentication.mappers.UserMapper;
import com.crcl.authentication.repository.UserRepository;
import com.crcl.authentication.service.UserService;
import com.crcl.authentication.utils.ProfileUtils;
import com.crcl.authentication.utils.RoleUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final ProfileClient profileClient;
    private final AuthenticationHelper authenticationHelper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServerStorageClient serverStorageClient;

    @Override
    public UserDto save(UserDto userDto) {
        User user = this.userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(RoleUtils.getDefaultUserRoles());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> save(List<UserDto> entities) {
        return entities.stream()
                .map(this::save).toList();
    }

    @Override
    public void deleteById(String id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setEnabled(false);
            userRepository.save(user);
            log.info("user with id %s was disabled".formatted(user.getId()));
        });

    }

    @Override
    public UserDto findById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toDto).orElse(null);
    }


    @Override
    public List<UserDto> findAll() {
        return userRepository.getAll(authenticationHelper.getCurrent());
    }


    @Override
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.getAll(pageable, authenticationHelper.getCurrent());
    }

    @Override
    public UserDto update(UserDto userDto, String id) {
        return userRepository.findById(id)
                .map(user -> userMapper.toEntity(userDto))
                .map(userRepository::save)
                .map(userMapper::toDto).orElse(null);
    }

    @Override
    public UserDto findByUsername(String username) {
        return userRepository.findByUsernameAllIgnoreCase(username)
                .map(userMapper::toDto).orElse(null);
    }

    @Override
    public UserDto save(CreateUserRequest request) {
        final User user = userMapper.toEntity(request);
        this.addUserAvatar(request, user);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(RoleUtils.getDefaultUserRoles());
        return userMapper.toDto(this.userRepository.save(user));
    }

    @Override
    public Set<UserDto> findByUserNames(Set<String> userNames) {
        List<User> users = this.userRepository.findByUsernameIn(userNames);
        this.profileClient.findByUsernames(new ArrayList<>(userNames));
        return new HashSet<>(userMapper.mapToDto(users));
    }


    private void addUserAvatar(CreateUserRequest request, User user) {
        try {
            if (nonNull(request.getAvatarFile())) {
                final var fileSaveResponse = this.serverStorageClient.save(request.getAvatarFile());
                user.setAvatar(fileSaveResponse.getLink());
            }
        } catch (Exception e) {
            log.error("An error occurred while saving avatar for user: {}", user.getUsername(), e);
            user.setAvatar(ProfileUtils.getAvatar(user));
        }
    }
}
