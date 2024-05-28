package org.bootstrap.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bootstrap.member.entity.converter.ReasonTypeConverter;
import org.bootstrap.member.utils.EnumValueUtils;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "ban")
@Entity
public class Ban {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "unban_date", nullable = false)
    private LocalDateTime unbanDate;

    @Convert(converter = ReasonTypeConverter.class)
    @Column(nullable = false)
    private ReasonType reason;

    public static Ban of(Member member, LocalDateTime unbanDate, ReasonType reason) {
        return Ban.builder()
                .member(member)
                .unbanDate(unbanDate)
                .reason(reason)
                .build();
    }
}
