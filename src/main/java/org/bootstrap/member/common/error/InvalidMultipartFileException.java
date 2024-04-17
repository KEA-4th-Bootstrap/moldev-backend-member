package org.bootstrap.member.common.error;

public class InvalidMultipartFileException extends BaseErrorException {
    public static final InvalidMultipartFileException EXCEPTION = new InvalidMultipartFileException();

    public InvalidMultipartFileException() {
        super(GlobalErrorCode.INVALID_MULTIPART_FILE);
    }

}
