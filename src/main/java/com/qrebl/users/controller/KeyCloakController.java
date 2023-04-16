package com.qrebl.users.controller;

import com.qrebl.users.dto.AuthDto;
import com.qrebl.users.dto.LoginDto;
import com.qrebl.users.dto.UserDto;
import com.qrebl.users.service.KeyCloakService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/auth")
public class KeyCloakController {

    @Autowired
    KeyCloakService service;

    @PostMapping
    public String addUser(@RequestBody UserDto userDTO){
        service.addUser(userDTO);
        return "User Added Successfully.";
    }

    @GetMapping(path = "/{userName}")
    public List<UserRepresentation> getUsers(@PathVariable("userName") String userName){
        List<UserRepresentation> user = service.getUser(userName);
        return user;
    }

    @PostMapping(path = "/get-token")
    public AuthDto getToken(@RequestBody LoginDto loginDto){
        return service.getToken(loginDto);
    }

    @PutMapping(path = "/update/{userId}")
    public String updateUser(@PathVariable("userId") String userId, @RequestBody UserDto userDTO){
        service.updateUser(userId, userDTO);
        return "User Details Updated Successfully.";
    }

    @DeleteMapping(path = "/delete/{userId}")
    public String deleteUser(@PathVariable("userId") String userId){
        service.deleteUser(userId);
        return "User Deleted Successfully.";
    }

    @GetMapping(path = "/verification-link/{userId}")
    public String sendVerificationLink(@PathVariable("userId") String userId){
        service.sendVerificationLink(userId);
        return "Verification Link Send to Registered E-mail Id.";
    }

    @GetMapping(path = "/reset-password/{userId}")
    public String sendResetPassword(@PathVariable("userId") String userId){
        service.sendResetPassword(userId);
        return "Reset Password Link Send Successfully to Registered E-mail Id.";
    }
}