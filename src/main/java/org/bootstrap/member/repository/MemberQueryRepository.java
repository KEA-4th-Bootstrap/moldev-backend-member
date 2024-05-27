package org.bootstrap.member.repository;

import org.bootstrap.member.dto.response.MemberInfoForAdminResponseDto;
import org.bootstrap.member.dto.response.MemberProfileResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Set;

public interface MemberQueryRepository {
    Page<MemberInfoForAdminResponseDto> getMemberInfoForAdmin(Boolean marketingAgree, String searchMoldevId, Pageable pageable);
    List<MemberProfileResponseDto> getTrendingMembers(Set<String> moldevIds);
    Slice<MemberProfileResponseDto> findMemberSearchResult(String text, Pageable pageable);
}
