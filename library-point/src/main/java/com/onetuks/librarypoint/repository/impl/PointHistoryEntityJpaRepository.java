package com.onetuks.librarypoint.repository.impl;

import com.onetuks.librarypoint.repository.entity.PointHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryEntityJpaRepository extends JpaRepository<PointHistoryEntity, Long> {

  Page<PointHistoryEntity> findAllByMemberEntityMemberId(long loginId, Pageable pageable);
}
