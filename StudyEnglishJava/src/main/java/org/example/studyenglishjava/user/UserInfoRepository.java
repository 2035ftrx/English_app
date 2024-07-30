package org.example.studyenglishjava.user;

import org.example.studyenglishjava.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findByUserId(long userId);

    void deleteByUserId(long userId);

}