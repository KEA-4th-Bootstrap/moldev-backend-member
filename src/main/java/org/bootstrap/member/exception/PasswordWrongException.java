package org.bootstrap.member.exception;

import org.bootstrap.member.common.error.BaseErrorException;

public class PasswordWrongException extends BaseErrorException {
    public static final PasswordWrongException EXCEPTION = new PasswordWrongException();

    public PasswordWrongException() {
        super(MemberErrorCode.WRONG_PASSWORD);
    }

}
