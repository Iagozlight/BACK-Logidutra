package br.com.uniamerica.logidutra.service;

import br.com.uniamerica.logidutra.controller.auth.LoginRequest;
import br.com.uniamerica.logidutra.controller.auth.LoginResponse;
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

    public LoginResponse login(LoginRequest data) { //Agora ele salva o Token
        var credenciais = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var authentication = authenticationManager.authenticate(credenciais);
        var usuario = (Usuario) authentication.getPrincipal();

        String token = jwtService.gerarToken(usuario);
        return new LoginResponse(token, usuario.getId(), usuario.getNome(), usuario.getRole());
    }
}
