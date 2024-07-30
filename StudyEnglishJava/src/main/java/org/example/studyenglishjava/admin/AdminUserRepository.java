package org.example.studyenglishjava.admin;

import org.example.studyenglishjava.dto.UserInfoADTO;
import org.example.studyenglishjava.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminUserRepository extends JpaRepository<User, Long> {

    @Query("SELECT new org.example.studyenglishjava.dto.UserInfoADTO(u.id, u.username, ui.nickname, ui.gender, ui.lastLoginTime, ui.school, ui.grade) " +
            "FROM User u JOIN users_info ui ON u.id = ui.userId " +
            "WHERE u.role = 3 ")
    List<UserInfoADTO> fetchUserAndUserInfo(Pageable pageable);

    @Query("SELECT new org.example.studyenglishjava.dto.UserInfoADTO(u.id, u.username, ui.nickname, ui.gender, ui.lastLoginTime, ui.school, ui.grade) " +
            "FROM User u JOIN users_info ui ON u.id = ui.userId " +
            "WHERE u.id = :userId ")
    Optional<UserInfoADTO> fetchUserAndUserInfoById(@Param("userId") Long userId);


}
