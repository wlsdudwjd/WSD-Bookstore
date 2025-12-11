package com.example.bookstore.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof Long l) {
            return l;
        }
        try {
            return Long.parseLong(principal.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}