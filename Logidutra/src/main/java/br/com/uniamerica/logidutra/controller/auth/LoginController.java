package br.com.uniamerica.logidutra.controller.auth;

import br.com.uniamerica.logidutra.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class LoginController {

    @Autowired
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        if (loginRequest == null) {
            throw new IllegalArgumentException("A solicitação de login não pode ser vazia");
        }
        return loginService.login(loginRequest);
    }
}
