package com.charly.demo.repository;

import com.charly.demo.entity.OtpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpStatusRepository extends JpaRepository<OtpStatus, Long> {

    Optional<OtpStatus> findByStatus(String status);
}
