package com.example.bookstore.common;

import com.example.bookstore.common.api.ApiResponse;
import com.example.bookstore.common.api.PageResponse;
import com.example.bookstore.common.util.DateUtil;
import com.example.bookstore.user.entity.Gender;
import com.example.bookstore.user.entity.GenderConverter;
import com.example.bookstore.user.entity.Role;
import com.example.bookstore.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonUnitTests {

    @Test
    void apiResponseSuccessWithData() {
        ApiResponse<String> response = ApiResponse.success("ok", "payload");
        assertEquals("success", response.getStatus());
        assertEquals("200", response.getStatusCode());
        assertEquals("payload", response.getData());
        assertEquals("ok", response.getMessage());
    }

    @Test
    void apiResponseSuccessWithoutDataHasNullData() {
        ApiResponse<Void> response = ApiResponse.success("done");
        assertEquals("success", response.getStatus());
        assertEquals("200", response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void apiResponseCreatedSets201() {
        ApiResponse<String> response = ApiResponse.created("item");
        assertEquals("201", response.getStatusCode());
        assertEquals("item", response.getData());
    }

    @Test
    void apiResponseNoContentSets204AndNullData() {
        ApiResponse<Void> response = ApiResponse.noContent();
        assertEquals("204", response.getStatusCode());
        assertNull(response.getData());
    }

    @Test
    void apiResponseErrorKeepsMessage() {
        ApiResponse<Void> response = ApiResponse.error("fail", "400");
        assertEquals("error", response.getStatus());
        assertEquals("400", response.getStatusCode());
        assertEquals("fail", response.getMessage());
    }

    @Test
    void dateUtilNowUsesSeoulClock() {
        LocalDateTime now = DateUtil.now();
        LocalDateTime expected = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        long seconds = Math.abs(Duration.between(now, expected).getSeconds());
        assertTrue(seconds < 60, "now should be within 60 seconds of Seoul time");
    }

    @Test
    void genderConverterToDatabaseColumn() {
        GenderConverter converter = new GenderConverter();
        assertEquals("male", converter.convertToDatabaseColumn(Gender.MALE));
    }

    @Test
    void genderConverterFromDatabaseValueIsCaseInsensitive() {
        GenderConverter converter = new GenderConverter();
        assertEquals(Gender.FEMALE, converter.convertToEntityAttribute("FEMALE"));
        assertEquals(Gender.MALE, converter.convertToEntityAttribute("male"));
    }

    @Test
    void genderConverterHandlesNull() {
        GenderConverter converter = new GenderConverter();
        assertNull(converter.convertToEntityAttribute(null));
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void genderFromThrowsOnUnknownValue() {
        assertThrows(IllegalArgumentException.class, () -> Gender.from("other"));
    }

    @Test
    void userFactorySetsDefaults() {
        LocalDate birthday = LocalDate.of(1990, 1, 1);
        User user = User.createUser(
                "a@b.com",
                "encPw",
                "name",
                "010",
                "addr",
                Gender.MALE,
                birthday,
                Role.ADMIN
        );
        assertTrue(user.getActive());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(birthday, user.getBirthday());
    }

    @Test
    void userChangeProfileSkipsBlankValues() {
        User user = User.createUser(
                "a@b.com",
                "encPw",
                "old",
                "010",
                "addr",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                Role.USER
        );
        user.changeProfile("new", "", null);
        assertEquals("new", user.getName());
        assertEquals("010", user.getPhoneNumber());
        assertEquals("addr", user.getAddress());
    }

    @Test
    void userDeactivateAndChangePassword() {
        User user = User.createUser(
                "a@b.com",
                "encPw",
                "name",
                "010",
                "addr",
                Gender.FEMALE,
                LocalDate.of(1990, 1, 1),
                Role.USER
        );
        user.deactivate();
        user.changePassword("newPw");
        assertFalse(user.getActive());
        assertEquals("newPw", user.getPassword());
    }

    @Test
    void userPrePersistSetsTimestampsWhenNullActive() {
        User user = User.builder()
                .email("a@b.com")
                .password("pw")
                .name("name")
                .phoneNumber("010")
                .address("addr")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1990, 1, 1))
                .role(Role.USER)
                .build();
        user.onCreate();
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
        assertTrue(user.getActive());
    }

    @Test
    void pageResponseFromMapsFields() {
        PageImpl<Integer> page = new PageImpl<>(List.of(1, 2, 3));
        PageResponse<Integer> response = PageResponse.from(page);
        assertEquals(0, response.getPage());
        assertEquals(3, response.getSize());
        assertEquals(3, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertEquals(List.of(1, 2, 3), response.getContent());
    }

    @Test
    void roleEnumContainsAdminAndUser() {
        assertNotNull(Role.valueOf("ADMIN"));
        assertNotNull(Role.valueOf("USER"));
    }
}
