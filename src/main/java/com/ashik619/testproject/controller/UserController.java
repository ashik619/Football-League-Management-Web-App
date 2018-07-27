package com.ashik619.testproject.controller;

import com.ashik619.testproject.models.HttpRespMessage;
import com.ashik619.testproject.models.User;
import com.ashik619.testproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> userLLogin(@RequestBody Map<String, String> body,  HttpServletResponse response) {
        String _userName = body.get("u_name");
        String _pass = body.get("pass");
        List<User> userList = userRepository.findUserByName(_userName);
        if(userList!=null&&userList.size()>0){
            User user  =  userList.get(0);
            List<User> authUserList = userRepository.findUserByNameAndPass(_userName,_pass);
            if(authUserList!=null&&authUserList.size()>0){
                Cookie cookie  = new Cookie("sc-login", "Yurafghtj");
                cookie.setMaxAge(86400);
                response.addCookie(cookie);
                HttpRespMessage respMessage = new HttpRespMessage("User Authenticated");
                respMessage.setData("true");
                ResponseEntity<HttpRespMessage> responseEntity = new ResponseEntity<>(respMessage,HttpStatus.OK);
                return responseEntity;
            }else {
                return new ResponseEntity<>(new HttpRespMessage("Password not valid"),HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(new HttpRespMessage("User not found! enter valid username"),HttpStatus.OK);
        }

    }


    @GetMapping("/userAuth")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@CookieValue(value = "sc-login",
            defaultValue = "NA") String cookie) {
        if(cookie.equals("Yurafghtj")){
            return new ResponseEntity<>(new HttpRespMessage("success"),HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new HttpRespMessage("fail"),HttpStatus.OK);
        }

    }

}