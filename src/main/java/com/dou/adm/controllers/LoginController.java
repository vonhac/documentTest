package com.dou.adm.controllers;

import com.dou.adm.models.*;
import com.dou.adm.security.JwtProvider;
import com.dou.adm.security.JwtUser;
import com.dou.adm.services.UserService;
import com.dou.adm.shared.DepartmentConstant;
import com.dou.adm.shared.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LoginController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Value("${web.client.inactive.minutes}")
    private int clientInActiveMinutes;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String index(){
        return "index.html";
    }

    @PostMapping("/login/auth")
    public ResponseEntity<?> authenticateUser(@RequestBody JwtUser requestObject) {
        ResponseObject responseObject = new ResponseObject();
        User user = this.getValidUser(requestObject.getUsername(), requestObject.getPassword());
        if(user != null){
            JwtUser jwtUser = JwtUser.create(user);
            jwtUser.setProfiles(userService.retrieveUserProfile(user));
            jwtUser.setFiltering(this.retrieveSelector(jwtUser));
            String jwt = jwtProvider.generateToken(jwtUser);

            LoginResponse respData = new LoginResponse();
            respData.setAccount(jwtUser.getProfiles());
            respData.setPermissions(userService.retrieveAccountPermission(user.getAccountId()));
            respData.setAccessToken(jwt);
            respData.setExpireHours(this.clientInActiveMinutes);

            responseObject.setData(respData);
        }
        else {
            responseObject.setFailMessage("User Name or Password is incorrect!");
        }
        return ResponseEntity.ok(responseObject);
    }

    @PutMapping("/user-password")
    @ResponseBody
    public ResponseObject changePassword(@RequestBody ChangePasswordRequest reqObject) {
        try {
            User user = this.getValidUser(reqObject.getUsername(), reqObject.getPassword());
            if(user != null) {
                String hashPassword = jwtProvider.generatePassword(reqObject.getNewPassword());
                int result = userService.changePassword(user.getAccountId(), hashPassword);
                if (result > 0) {
                    return ResponseObject.SUCCESS_WITHOUT_DATA;
                } else {
                    return ResponseObject.failResponse("Can not update new password to DB");
                }
            }
        }catch (Exception e){
            LOGGER.error(String.format("Error occurred while trying to change password of user [%s].", reqObject.getUsername()), e);
            return ResponseObject.FAILURE;
        }
        return ResponseObject.failResponse("Please input correct current username and password.");
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public ResponseObject authenticate(@RequestBody String token) {
        try {
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                JwtUser jwtUser = jwtProvider.getJwtUser(token);
                User user = userService.loginUser(jwtUser.getUsername());
                if (user != null) {
                    LoginResponse respData = new LoginResponse();
                    respData.setAccount(userService.retrieveUserProfile(user));
                    respData.setPermissions(userService.retrieveAccountPermission(user.getAccountId()));
                    respData.setAccessToken(token);
                    respData.setExpireHours(this.clientInActiveMinutes);
                    return new ResponseObject(respData);
                }
            }
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while trying to verify access token [%s].", token), e);
        }
        return ResponseObject.failResponse("Invalid access token");
    }

    private User getValidUser(String username, String password) {
        User user = userService.loginUser(username);
        if (user != null && jwtProvider.validatePassword(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    private DataFiltering retrieveSelector(JwtUser user) {
        DataFiltering selector = new DataFiltering(user.getUsername());
        UserProfiles profiles = user.getProfiles();
        String department = profiles.getDepartmentId() == null ? "" : profiles.getDepartmentId();
        long isAdmin = profiles.getIsAdmin();

        try {
            int managerLevel = userService.managerLevel(profiles.getAccountId());
            List lsBranchSip = userService.listBranchSip(profiles.getAccountId());
            List result = userService.lsAccSubervisor(user.getTargetProfileTable(), profiles);
            result.add(user.getUsername());
            selector.setDepartmentId(department);
            selector.setLsBranchSip(lsBranchSip);
            selector.setBranchSip(profiles.getBranchId());
            if (managerLevel == 0) {
                if (isAdmin == 1) {
                    if (DepartmentConstant.isCSRStaff(department)) {
                        selector.setAuthor("ADMIN_CSR");
                    } else {
                        selector.setAuthor("ADMIN_CHANNEL_SALES");
                    }
                } else {
                    if (DepartmentConstant.isCSRStaff(department)) {
                        selector.setAuthor("USER_OF_CSR");
                        selector.setLsAcc(result);
                    } else {
                        selector.setAuthor("USER_OF_CHANNEL_SALES");
                        selector.setLsAcc(result);
                    }
                }
            } else {
                selector.setAuthor("USER_OF_MANAGER_LEVEL");
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while trying to retrieve ");
        }
        return selector;
    }
}
