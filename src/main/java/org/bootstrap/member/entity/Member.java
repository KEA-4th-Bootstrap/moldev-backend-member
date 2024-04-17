package org.bootstrap.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bootstrap.member.common.BaseTimeEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Table(name = "member")
@Entity
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String password;

    private String email;

    @Column(name = "moldev_id")
    private String moldevId;

    private String nickname;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "island_name")
    private String islandName;

}