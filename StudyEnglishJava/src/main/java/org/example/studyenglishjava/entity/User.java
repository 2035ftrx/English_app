package org.example.studyenglishjava.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.studyenglishjava.dto.UserDTO;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private long createTime;
    private int status;
    private int role;

    // Default constructor
    public User() {
        this.id = 0L;
        this.username = DefaultStringValue.Impl.UNKNOWN.value();
        this.password = DefaultStringValue.Impl.UNKNOWN.value();
        this.createTime = 0L;
        this.status = UserStatus.ACTIVE.value();
        this.role = UserRole.UNKNOWN.value();
    }

    // Full constructor
    public User(Long id, String username, String password, long createTime,  int status, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createTime = createTime;
        this.status = status;
        this.role = role;
    }

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE.value();
    }

    // Getters and setters
    // ... (implement all getters and setters for each field)

    // UserDTO conversion method
    public UserDTO toDTO() {
        return new UserDTO(
                this.id,
                this.username,
                this.role
        );
    }
}
