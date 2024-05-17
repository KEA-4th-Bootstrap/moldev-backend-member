package org.bootstrap.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.bootstrap.member.common.BaseTimeEntity;
import org.bootstrap.member.dto.request.PasswordPatchRequestDto;
import org.bootstrap.member.dto.request.ProfilePatchRequestDto;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "view_count")
    private Integer viewCount;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Ban ban;

    @Column(name = "marketing_agree")
    private Boolean isMarketingAgree;

    public void updateProfile(ProfilePatchRequestDto profilePatchRequestDto){
        this.islandName = profilePatchRequestDto.islandName();
        this.nickname = profilePatchRequestDto.nickname();
    }

    public void updatePassword(String encodedPassword){
        this.password = encodedPassword;
    }

    public void updateProfileImage(String profileImgUrl){
        this.profileImgUrl = profileImgUrl;
    }

    public void updateViewCount(Integer viewCount){
        this.viewCount = viewCount;
    }
}