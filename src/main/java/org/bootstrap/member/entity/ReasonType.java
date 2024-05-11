package org.bootstrap.member.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bootstrap.member.utils.EnumField;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ReasonType implements EnumField {
    PROFANITY(1, "욕설"),
    HATE_SPEECH(2, "혐오표현"),
    SEXUAL_CONTENT(3, "성적인 콘텐츠"),
    VIOLENCE(4, "폭력적인 콘텐츠"),
    SPAM(5, "스팸"),
    HARMFUL_CONTENT(6, "유해한 콘텐츠");

    private final Integer code;
    private final String desc;
}
