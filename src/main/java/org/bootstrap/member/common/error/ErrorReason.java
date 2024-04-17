package org.bootstrap.member.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorReason {

    private final Integer status;
    private final String code;
    private final String reason;

    @Builder
    public ErrorReason(Integer status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }

    public static ErrorReason of(Integer status, String code, String reason) {
        return ErrorReason.builder()
                .status(status)
                .code(code)
                .reason(reason)
                .build();
    }
}
