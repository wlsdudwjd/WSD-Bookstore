package com.example.bookstore.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;

public final class DateUtil {

    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    private DateUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE_ID);
    }
}