package com.example.bookstore.user.service;

import com.example.bookstore.common.api.PageResponse;
import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.util.SecurityUtil;
import com.example.bookstore.user.dto.UserProfileResponse;
import com.example.bookstore.user.dto.UserResponse;
import com.example.bookstore.user.dto.UserUpdateRequest;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getMyProfile() {
        String email = SecurityUtil.getCurrentUserEmailOrNull();
        if (email == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, Map.of("reason", "anonymous"));
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", email)
                ));

        return new UserProfileResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getGender(),
                user.getBirthday(),
                user.getRole()
        );
    }

    @Transactional
    public void updateMyProfile(UserUpdateRequest request) {
        String email = SecurityUtil.getCurrentUserEmailOrNull();
        if (email == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, Map.of("reason", "anonymous"));
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("email", email)
                ));

        user.changeProfile(request.name(), request.phoneNumber(), request.address());
    }

    public PageResponse<UserResponse> getUsers(int page, int size, String sortParam) {
        Sort sort = Sort.by("createdAt").descending();
        if (sortParam != null && !sortParam.isBlank()) {
            // 예: sort=name,ASC
            String[] parts = sortParam.split(",");
            String property = parts[0];
            Sort.Direction direction = parts.length > 1 && "ASC".equalsIgnoreCase(parts[1])
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC;
            sort = Sort.by(direction, property);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> users = userRepository.findAll(pageable);

        Page<UserResponse> mapped = users.map(user -> new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getGender(),
                user.getBirthday(),
                user.getRole(),
                user.getActive(),
                user.getCreatedAt()
        ));

        return PageResponse.from(mapped);
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", userId)
                ));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new CustomException(
                    ErrorCode.STATE_CONFLICT,
                    Map.of("reason", "already deactivated")
            );
        }

        user.deactivate();
    }
}