package com.charly.demo.repository;

import com.charly.demo.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {

    Optional<UserOtp> findUserOtpByUserIdAndStatusId(String userId, Long statusId);
}
