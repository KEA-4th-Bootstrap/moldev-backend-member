package org.bootstrap.member.utils;

import java.util.Objects;

public class EntityUpdateValueUtils {
    public static <T> T updateValue(T currentValue, T newValue) {
        if (Objects.isNull(newValue)) {
            return currentValue;
        } else {
            return newValue;
        }
    }
}
