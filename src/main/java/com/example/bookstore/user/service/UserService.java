package com.example.bookstore.user.service;

import com.example.bookstore.common.exception.CustomException;
import com.example.bookstore.common.exception.ErrorCode;
import com.example.bookstore.common.util.SecurityUtil;
import com.example.bookstore.user.dto.UserProfileResponse;
import com.example.bookstore.user.dto.UserResponse;
import com.example.bookstore.user.dto.UserUpdateRequest;
import com.example.bookstore.user.entity.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ======== 내 프로필 조회 ========
    public UserProfileResponse getMyProfile() {
        return UserProfileResponse.from(getCurrentUser());
    }

    // ======== 내 프로필 수정 ========
    @Transactional
    public UserProfileResponse updateMyProfile(UserUpdateRequest request) {
        User user = getCurrentUser();

        // 이름 / 전화번호 / 주소 변경
        user.changeProfile(
                request.name(),
                request.phoneNumber(),
                request.address()
        );

        if (request.password() != null && !request.password().isBlank()) {
            user.changePassword(passwordEncoder.encode(request.password()));
        }

        return UserProfileResponse.from(user);
    }

    // ======== (ADMIN) 유저 목록 조회 ========
    public Page<UserResponse> getUsers(int page, int size, String sort) {
        Sort sortSpec = Sort.by(Sort.Direction.DESC, "createdAt");
        if (sort != null && !sort.isBlank()) {
            // "createdAt,ASC" 같은 형식 처리하고 있다면 여기에서 파싱
        }

        PageRequest pageRequest = PageRequest.of(page, size, sortSpec);
        Page<User> users = userRepository.findAll(pageRequest);

        return users.map(UserResponse::from);
    }

    // ======== (ADMIN) 유저 비활성화 ========
    @Transactional
    public void deactivateUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", String.valueOf(userId))
                ));
        user.deactivate();
    }

    public Page<UserResponse> getUserList(Pageable pageable) {

        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponse::from);
    }

    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", String.valueOf(userId))
                ));
        return UserResponse.from(user);
    }

    @Transactional
    public void deleteMyAccount() {
        User user = getCurrentUser();
        user.deactivate();
    }

    private User getCurrentUser() {
        Integer userId = SecurityUtil.getCurrentUserId();
        if (userId == null) {
            throw new CustomException(
                    ErrorCode.UNAUTHORIZED,
                    Map.of("auth", "인증 정보가 없습니다.")
            );
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        Map.of("userId", String.valueOf(userId))
                ));
    }
}
