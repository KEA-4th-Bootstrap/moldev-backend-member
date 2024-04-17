package org.bootstrap.member.common.error;

public class FileUploadErrorException extends BaseErrorException {

    public FileUploadErrorException(GlobalErrorCode errorCode) {
        super(errorCode);
    }
}