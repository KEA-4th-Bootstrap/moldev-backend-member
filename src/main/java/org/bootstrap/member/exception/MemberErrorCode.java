package org.bootstrap.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bootstrap.member.common.error.BaseErrorCode;
import org.bootstrap.member.common.error.ErrorReason;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_404_1", "존재하지 않는 멤버입니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER_401_1", "비밀번호 입력 오류입니다."),
    ;

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}

