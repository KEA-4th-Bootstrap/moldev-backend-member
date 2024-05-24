package org.bootstrap.member.dto.request;

public record PasswordPatchRequestDto(
        String email,
        String password
) {
}
