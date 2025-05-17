package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.constant.Constants.USER_NOT_FOUND_ERR;
import static ru.practicum.shareit.constant.Constants.USER_WITH_SAME_EMAIL_ERR;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Collection<UserDto> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format(USER_NOT_FOUND_ERR, userId)));

        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (isEmailUnique(userDto)) {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        } else {
            throw new ValidationException(String.format(USER_WITH_SAME_EMAIL_ERR, userDto.getEmail()));
        }
    }

    @Override
    public UserDto updateUserById(UserDto userDto, Long userId) {
        userDto.setId(userId);
        if (userDto.getEmail() != null && !isEmailUnique(userDto)) {
            throw new ValidationException(String.format(USER_WITH_SAME_EMAIL_ERR, userDto.getEmail()));
        }
        if (findUserById(userId) != null) {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userDto)));
        } else {
            throw new NotFoundException(String.format(USER_NOT_FOUND_ERR, userId));
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (findUserById(userId) != null) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException(String.format(USER_NOT_FOUND_ERR, userId));
        }
    }

    private boolean isEmailUnique(UserDto user) {
       return findAllUsers().stream()
               .noneMatch(u -> u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId()));

    }
}
