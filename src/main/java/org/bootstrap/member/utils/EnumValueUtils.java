package org.bootstrap.member.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bootstrap.member.exception.EnumValidateException;

import java.util.EnumSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnumValueUtils {
    public static <T extends Enum<T> & EnumField> Integer toDBCode(T enumClass) {
        if (enumClass == null) return -1;
        return enumClass.getCode();
    }

    public static <T extends Enum<T> & EnumField> T toEntityCode(Class<T> enumClass, Integer dbCode) {
        if (dbCode == -1) return null;
        return EnumSet.allOf(enumClass).stream()
                .filter(e -> e.getCode().equals(dbCode))
                .findAny()
                .orElseThrow(() -> EnumValidateException.EXCEPTION);
    }
}
