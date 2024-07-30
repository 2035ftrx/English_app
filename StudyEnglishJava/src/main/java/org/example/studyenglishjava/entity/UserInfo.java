package org.example.studyenglishjava.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.example.studyenglishjava.dto.UserInfoDTO;

@Setter
@Getter
@Entity(name = "users_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;

    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private String description;
    private int gender;
    private long lastLoginTime;

    private String school;
    private String grade;

    public UserInfoDTO toDTO() {
        return new UserInfoDTO(
                nickname,
                gender,
                lastLoginTime,
                school,
                grade
        );
    }
}
