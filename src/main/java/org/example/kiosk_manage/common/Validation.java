package org.example.kiosk_manage.common;

import org.example.kiosk_manage.common.exception.BadRequestException;

public class Validation {

    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$";

    public static boolean validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException("비밀번호는 필수입니다.");
        }
        if (!password.matches(PASSWORD_PATTERN)) {
            throw new BadRequestException("비밀번호 양식은 길이: 8~15, 영문+숫자+특수문자로 구성되어야 합니다.");
        }
        return true;
    }

    public static boolean matchPassword(String password1, String password2) {
        if (password1 == null || password2 == null) {
            throw new BadRequestException("비밀번호는 필수입니다.");
        }
        if (!password1.equals(password2)) {
            throw new BadRequestException("입력해주신 비밀번호가 서로 다릅니다.");
        }
        return true;
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("이메일은 필수입니다.");
        }
        return true;
    }
}
