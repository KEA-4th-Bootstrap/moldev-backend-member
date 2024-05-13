package org.bootstrap.member.repository;

import org.bootstrap.member.dto.response.MemberInfoForAdminResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryRepository {
    Page<MemberInfoForAdminResponseDto> getMemberInfoForAdmin(Pageable pageable);
}
