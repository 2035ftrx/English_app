package org.example.studyenglishjava.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.example.studyenglishjava.auth.JwtService;
import org.example.studyenglishjava.dto.*;
import org.example.studyenglishjava.entity.User;
import org.example.studyenglishjava.entity.UserInfo;
import org.example.studyenglishjava.entity.UserRole;
import org.example.studyenglishjava.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService {
    // logger
    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserAuthService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public Optional<User> getUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").split(" ")[1];
        String username = jwtService.getUsernameFromToken(token);
        return userRepository.findByUsername(username);
    }

    public UserDTO registerUser(RegisterRequest newUser) throws DuplicateUsernameException {
        Optional<User> existingUser = userRepository.findByUsername(newUser.username());
        if (existingUser.isPresent()) {
            throw new DuplicateUsernameException("Username already exists.");
        }

        String encodedPassword = passwordEncoder.encode(newUser.password());

        User userToSave = new User();
        userToSave.setUsername(newUser.username());
        userToSave.setPassword(encodedPassword);
        userToSave.setRole(UserRole.USER.value());
        userToSave.setCreateTime(System.currentTimeMillis());

        val savedUser = userRepository.save(userToSave);
        val userInfo = new UserInfo();
        userInfo.setUserId(savedUser.getId());
        userInfo.setNickname(savedUser.getUsername());
        userInfoRepository.save(userInfo);

        return savedUser.toDTO();
    }

    public UserDTO loginUser(LoginRequest user) throws UnauthorizedException, NotFoundException {
        User userDb = userRepository.findByUsername(user.username())
                .orElseThrow(() -> new NotFoundException("User not found."));


        boolean isPasswordValid = passwordEncoder.matches(user.password(), userDb.getPassword());

        if (!isPasswordValid) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        userInfoRepository.findByUserId(userDb.getId()).ifPresent(info -> {
            info.setLastLoginTime(System.currentTimeMillis());
            userInfoRepository.save(info);
        });

        return userDb.toDTO();
    }


    public User changePassword(ChangePasswordRequest request) throws DuplicateUsernameException, IncorrectPasswordException, PasswordNotMatchException {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new DuplicateUsernameException("Username already exists."));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IncorrectPasswordException();
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordNotMatchException();
        }

        String encryptedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);

    }


    public UserInfoDTO updateUserProfile(Long userId, UpdateProfileRequest updateInfo) throws NotFoundException {
        UserInfo userInfo = userInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID " + userId));
        logger.info("updateInfo: {}", updateInfo);
        userInfo.setNickname(updateInfo.nickname() != null ? updateInfo.nickname() : userInfo.getNickname());
        userInfo.setSchool(updateInfo.school() != null ? updateInfo.school() : userInfo.getSchool());
        userInfo.setGrade(updateInfo.grade() != null ? updateInfo.grade() : userInfo.getGrade());
        userInfo.setGender(updateInfo.gender() != null ? updateInfo.gender() : userInfo.getGender());
        val save = userInfoRepository.save(userInfo);
        return save.toDTO();
    }


    public Optional<UserInfoDTO> getUserDetails(String username) {
        val user = userRepository.findByUsername(username).orElseThrow();
        val userInfo = userInfoRepository.findByUserId(user.getId()).orElseThrow();
        return Optional.of(userInfo.toDTO());
    }
}