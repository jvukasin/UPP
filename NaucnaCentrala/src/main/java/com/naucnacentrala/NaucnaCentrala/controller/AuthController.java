package com.naucnacentrala.NaucnaCentrala.controller;

import com.naucnacentrala.NaucnaCentrala.model.User;
import com.naucnacentrala.NaucnaCentrala.model.UserTokenState;
import com.naucnacentrala.NaucnaCentrala.security.CustomUserDetailsService;
import com.naucnacentrala.NaucnaCentrala.security.TokenUtils;
import com.naucnacentrala.NaucnaCentrala.security.auth.JwtAuthenticationRequest;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "auth")
public class AuthController {

    @Autowired
    public TokenUtils tokenUtils;

    @Autowired
    IdentityService identityService;

    @Autowired
    public AuthenticationManager manager;

    @Autowired
    public CustomUserDetailsService userDetailsService;


    @RequestMapping(value="/login",method = RequestMethod.POST)
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response, Device device, HttpServletRequest hr){
//
//        if(!inputValid(authenticationRequest.getUsername())) {
//            return new ResponseEntity<>(new UserTokenState("error",0), HttpStatus.NOT_FOUND);
//        }
        final Authentication authentication = manager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        User user =  (User) authentication.getPrincipal();
        // VRATI DRUGI STATUS KOD
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwt = tokenUtils.generateToken(user.getUsername(), device);
        int expiresIn = 3600;


        return ResponseEntity.ok(new UserTokenState(jwt,expiresIn));
    }

    // proveriti jos da li je ovako dobro - istrazi
    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null)
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

