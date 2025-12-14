package com.example.bookstore.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            return null;
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof Integer i) {
            return i;
        }
        try {
            return Integer.parseInt(principal.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
