package org.bootstrap.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bootstrap.member.common.BaseTimeEntity;
import org.bootstrap.member.entity.converter.ReasonTypeConverter;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "ban")
@Entity
public class Ban extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "moldev_id", referencedColumnName = "moldev_id")
    private Member member;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "unban_date", nullable = false)
    private LocalDateTime unbanDate;

    @Convert(converter = ReasonTypeConverter.class)
    @Column(nullable = false)
    private ReasonType reason;

    public static Ban of(Member member, Long reportId, LocalDateTime unbanDate, ReasonType reason) {
        return Ban.builder()
                .member(member)
                .reportId(reportId)
                .unbanDate(unbanDate)
                .reason(reason)
                .build();
    }
}
