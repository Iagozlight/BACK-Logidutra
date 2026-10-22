package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    UsuarioController usuarioController;

    @MockitoBean
    UsuarioService usuarioService;

}
