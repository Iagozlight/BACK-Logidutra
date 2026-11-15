package br.com.uniamerica.logidutra.service;

import br.com.uniamerica.logidutra.controller.auth.LoginRequest;
import br.com.uniamerica.logidutra.infra.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

}
