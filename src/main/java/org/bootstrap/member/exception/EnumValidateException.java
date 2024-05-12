package org.bootstrap.member.exception;

import org.bootstrap.member.common.error.BaseErrorException;
import org.bootstrap.member.common.error.GlobalErrorCode;

public class EnumValidateException extends BaseErrorException {
    public static final BaseErrorException EXCEPTION = new EnumValidateException();

    public EnumValidateException() {
        super(GlobalErrorCode.INVALID_ENUM_CODE);
    }

}