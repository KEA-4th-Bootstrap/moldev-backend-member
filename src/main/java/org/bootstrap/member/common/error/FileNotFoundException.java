package org.bootstrap.member.common.error;

public class FileNotFoundException extends BaseErrorException {

    public FileNotFoundException(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}