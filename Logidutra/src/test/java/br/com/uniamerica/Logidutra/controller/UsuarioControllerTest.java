package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.Logidutra.entity.Usuario;
import br.com.uniamerica.Logidutra.service.UsuarioService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    UsuarioController usuarioController;

    @MockitoBean
    UsuarioService usuarioService;

    @Test
    void cenario01() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Joao");

        when(usuarioService.buscarPorId(1L))
                .thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response =
                usuarioController.buscarPorId(1L);

        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );
    }

    @Test
    void cenario02() {

        Usuario usuario = new Usuario();

        usuario.setId(1L);
        usuario.setNome("Joao");
        usuario.setSenha("123");

        when(usuarioService.login("Joao", "123"))
                .thenReturn(usuario);

        ResponseEntity<UsuarioResponse> response =
                usuarioController.login("Joao", "123");

        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        Assertions.assertEquals(
                "Joao",
                response.getBody().nome()
        );
    }

}
