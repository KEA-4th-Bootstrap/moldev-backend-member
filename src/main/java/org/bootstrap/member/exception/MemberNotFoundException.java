package org.bootstrap.member.exception;

import org.bootstrap.member.common.error.BaseErrorException;

public class MemberNotFoundException extends BaseErrorException {
    public static final MemberNotFoundException EXCEPTION = new MemberNotFoundException();

    public MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }

}
