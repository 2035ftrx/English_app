package org.example.studyenglishjava.admin;

import lombok.val;
import org.example.studyenglishjava.config.PageConfig;
import org.example.studyenglishjava.dto.RegisterRequest;
import org.example.studyenglishjava.dto.UpdateProfileRequest;
import org.example.studyenglishjava.dto.UserInfoADTO;
import org.example.studyenglishjava.exception.DuplicateUsernameException;
import org.example.studyenglishjava.exception.NotFoundException;
import org.example.studyenglishjava.user.UserAuthService;
import org.example.studyenglishjava.user.UserInfoRepository;
import org.example.studyenglishjava.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/admin/user")
public class ManagerUserController {

    @Autowired
    private AdminUserRepository admindUserRepository;

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping("/list")
    public List<UserInfoADTO> getAllUsers(
            @RequestParam(value = "page", defaultValue = PageConfig.PAGE_START) int page,
            @RequestParam(value = "size", defaultValue = PageConfig.PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size);
        return admindUserRepository.fetchUserAndUserInfo(pageable);
    }

    @PostMapping("/create")
    public UserInfoADTO createUser(@RequestBody @Validated RegisterRequest user) throws DuplicateUsernameException {
        val userDTO = userAuthService.registerUser(user);
        return admindUserRepository.fetchUserAndUserInfoById(userDTO.getId()).orElseThrow();
    }

    @GetMapping("/getUser")
    public UserInfoADTO getUserById(@RequestParam Long id) {
        return admindUserRepository.fetchUserAndUserInfoById(id).orElseThrow();
    }

    @PostMapping("/update")
    public UserInfoADTO updateUser(@RequestParam Long id, @RequestBody UpdateProfileRequest updateInfo) throws NotFoundException {
        val userInfoDTO = userAuthService.updateUserProfile(id, updateInfo);
        return admindUserRepository.fetchUserAndUserInfoById(id).orElseThrow();
    }

    @PostMapping("/delete")
    public Long deleteUser(@RequestParam Long id) {
        userRepository.deleteById(id);
        userInfoRepository.deleteById(id);
        return id;
    }

}
