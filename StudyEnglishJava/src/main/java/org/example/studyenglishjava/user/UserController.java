package org.example.studyenglishjava.user;

import lombok.val;
import org.example.studyenglishjava.auth.JwtService;
import org.example.studyenglishjava.auth.UserPrincipal;
import org.example.studyenglishjava.dto.*;
import org.example.studyenglishjava.entity.UserRole;
import org.example.studyenglishjava.exception.DuplicateUsernameException;
import org.example.studyenglishjava.exception.NotFoundException;
import org.example.studyenglishjava.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/user")
public class UserController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("login")
    public LoginResponse login(@RequestBody @Validated LoginRequest loginRequest) throws UnauthorizedException, NotFoundException {
        UserDTO user = userAuthService.loginUser(loginRequest);
        if (user.getRole() == UserRole.USER.value()) {
            String token = jwtService.generateJwtToken(user.getUsername());
            return new LoginResponse(token, user);
        } else {
            throw new UnauthorizedException("You are not authorized to login as admin");
        }
    }

    @GetMapping("userInfo")
    public UserInfoDTO userInfo() throws UnauthorizedException, NotFoundException {
        // Get the authenticated user from SecurityContext
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Use username or other details from currentUser to fetch additional info from userService
        val userInfoDTO = userAuthService.getUserDetails(currentUser.getUsername());
        userInfoDTO.orElseThrow(() -> new NotFoundException("User not found"));
        // If not found, throw NotFoundException
        return userInfoDTO.get();
    }

    @PostMapping("updateUser")
    public UserInfoDTO updateUserProfile(@RequestBody UpdateProfileRequest updateInfo) throws NotFoundException {
        // Get the authenticated user from SecurityContext
        UserPrincipal currentUser = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Use username or other details from currentUser to fetch additional info from userService
        val userInfoDTO = userAuthService.updateUserProfile(currentUser.userId(), updateInfo);
        // If not found, throw NotFoundException
        return userInfoDTO;
    }

    @PostMapping("register")
    public UserDTO register(@RequestBody @Validated RegisterRequest username) throws DuplicateUsernameException {
        UserDTO user = userAuthService.registerUser(username);
        return user;
    }

}