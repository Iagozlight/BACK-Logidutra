package br.com.uniamerica.Logidutra.controller;

import br.com.uniamerica.Logidutra.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logidutra/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
}
