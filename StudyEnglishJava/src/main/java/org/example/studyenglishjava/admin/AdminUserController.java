package org.example.studyenglishjava.admin;

import org.example.studyenglishjava.auth.JwtService;
import org.example.studyenglishjava.dto.LoginRequest;
import org.example.studyenglishjava.dto.LoginResponse;
import org.example.studyenglishjava.dto.UserDTO;
import org.example.studyenglishjava.entity.UserRole;
import org.example.studyenglishjava.exception.NotFoundException;
import org.example.studyenglishjava.exception.UnauthorizedException;
import org.example.studyenglishjava.user.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/admin")
public class AdminUserController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) throws UnauthorizedException, NotFoundException {
        UserDTO user = userAuthService.loginUser(loginRequest);
        if (user.getRole() == UserRole.ADMIN.value()) {
            String token = jwtService.generateJwtToken(user.getUsername());
            return new LoginResponse(token, user);
        } else {
            throw new UnauthorizedException("You are not authorized to login as admin");
        }
    }

}