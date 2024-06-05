package org.bootstrap.member.repository;

import org.bootstrap.member.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanRepository extends JpaRepository<Ban, Long> {
    Optional<Ban> findByReportId(Long reportId);
}
