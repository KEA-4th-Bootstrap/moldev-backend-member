package org.bootstrap.member.common.error;

public class InvalidFileUploadException extends BaseErrorException {
    public static final InvalidFileUploadException EXCEPTION = new InvalidFileUploadException();

    public InvalidFileUploadException() {
        super(GlobalErrorCode.INVALID_FILE_UPLOAD);
    }

}