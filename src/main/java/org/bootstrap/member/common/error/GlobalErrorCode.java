package org.bootstrap.member.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {

    /* global error */
    HTTP_MESSAGE_NOT_READABLE(BAD_REQUEST,"GLOBAL_400_1", "잘못된 형식의 값을 입력했습니다."),
    _INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR, "GLOBAL_500_1", "서버 오류. 관리자에게 문의 부탁드립니다."),

    /* Enum error */
    INVALID_ENUM_CODE(BAD_REQUEST, "ENUM_400_1", "잘못된 Enum class code 입니다."),

    /* S3 */
    INVALID_FILE_UPLOAD(BAD_REQUEST, "S3_400_1", "파일을 업로드할 수 없습니다."),
    INVALID_MULTIPART_FILE(BAD_REQUEST, "S3_400_2", "잘못된 Multipart 입니다."),
    ;

    private HttpStatus status;
    private String code;
    private String reason;

    @Override
    public ErrorReason getErrorReason() {
        return ErrorReason.of(status.value(), code, reason);
    }
}

