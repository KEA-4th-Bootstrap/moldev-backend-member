package org.bootstrap.member.repository;

import org.bootstrap.member.dto.response.MemberInfoForAdminResponseDto;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface MemberQueryRepository {
    Page<MemberInfoForAdminResponseDto> getMemberInfoForAdmin(Boolean marketingAgree, String searchMoldevId, Pageable pageable);
    List<MemberProfileResponseDto> getTrendingMembers(Set<Long> memberIds);
}
