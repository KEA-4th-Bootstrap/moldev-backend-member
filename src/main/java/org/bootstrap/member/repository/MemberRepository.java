package org.bootstrap.member.repository;

import org.bootstrap.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMoldevId(String moldevId);
}
