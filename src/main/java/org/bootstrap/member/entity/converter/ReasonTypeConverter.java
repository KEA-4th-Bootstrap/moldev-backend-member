package org.bootstrap.member.entity.converter;

import org.bootstrap.member.entity.ReasonType;
import org.bootstrap.member.utils.EnumCodeAttributeConverter;

public class ReasonTypeConverter extends EnumCodeAttributeConverter<ReasonType> {

    public ReasonTypeConverter() {
        super(ReasonType.class);
    }
}
