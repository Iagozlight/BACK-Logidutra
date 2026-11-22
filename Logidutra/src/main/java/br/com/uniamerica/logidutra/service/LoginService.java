package br.com.uniamerica.logidutra.service;

import br.com.uniamerica.logidutra.controller.auth.LoginRequest;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.infra.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String login(LoginRequest data) {
        if (data == null || data.login().isBlank() || data.senha().isBlank()) {
            throw new IllegalArgumentException("Login e senha não podem ser vazios");
        }
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var authentication = authenticationManager.authenticate(usernamePassword);
        var usuario = (Usuario) authentication.getPrincipal();

        return jwtService.gerarToken(usuario);
    }
}
