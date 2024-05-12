package org.bootstrap.member.repository;

import org.bootstrap.member.entity.Ban;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanRepository extends JpaRepository<Ban, Long> {
}
