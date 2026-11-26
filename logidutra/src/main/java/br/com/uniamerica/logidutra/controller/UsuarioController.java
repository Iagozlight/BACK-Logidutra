package br.com.uniamerica.logidutra.controller;

import br.com.uniamerica.logidutra.controller.dto.UsuarioRequest;
import br.com.uniamerica.logidutra.controller.dto.AlterarSenhaRequest;
import br.com.uniamerica.logidutra.controller.dto.UsuarioResponse;
import br.com.uniamerica.logidutra.entity.Usuario;
import br.com.uniamerica.logidutra.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/logidutra/usuario")
//@CrossOrigin(origins = "http://localhost:4200") // Não é necessário pois já está definido o security
public class UsuarioController {

    //Retirado o @Autowired pois isso permite o campo ser final, então a dependencia nunca fica nula nem é reatribuida.
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") //Só o admin pode registrar os casos, porque é ele quem cria os usuários no projeto.
    public ResponseEntity<?> salvar(@RequestBody @Valid UsuarioRequest usuarioRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            Usuario usuario = this.usuarioService.salvar(usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @GetMapping("/login")
//    public ResponseEntity<UsuarioResponse> login(@RequestParam String nome, @RequestParam String senha) {
//        try {
//            Usuario usuario = this.usuarioService.login(nome, senha);
//            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
//        } catch (ResponseStatusException e) {
//            return new ResponseEntity<>(e.getStatusCode());
//        }
//    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        try {
            List<UsuarioResponse> usuarioList =
                    usuarioService.listar()
                            .stream()
                            .map(UsuarioResponse::de)
                            .toList();

            return new ResponseEntity<>(usuarioList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable long id) {
        try {
            Usuario usuario = this.usuarioService.buscarPorId(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }



    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> atualizar(
            @RequestParam long id, @RequestBody @Valid UsuarioRequest usuarioRequest, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            Usuario usuario = usuarioService.atualizar(id, usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> atualizarParcial(
            @RequestParam long id,
            @RequestBody UsuarioRequest usuarioRequest,
            @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            Usuario usuario = usuarioService.atualizarParcial(id, usuarioRequest, usuarioLogado);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/minha-senha")
    public ResponseEntity<?> alterarMinhaSenha(@RequestBody @Valid AlterarSenhaRequest request, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            usuarioService.alterarMinhaSenha(usuarioLogado.getId(), request);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        }
    }

    @PatchMapping("/recuperar-senha")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> redefinirSenha(@RequestParam String nome, @RequestParam String novaSenha) {
        try {
            usuarioService.redefinirSenha(nome, novaSenha);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao redefinir senha.");
        }
    }

    @PatchMapping("/{id}/em-rota")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UsuarioResponse> marcarEmRota(@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.marcarEmRota(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}/disponivel")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<UsuarioResponse> marcarDisponivel(@PathVariable long id) {
        try {
            Usuario usuario = usuarioService.marcarDisponivel(id);
            return new ResponseEntity<>(UsuarioResponse.de(usuario), HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatusCode());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable long id, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            usuarioService.deletarPorId(id, usuarioLogado);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
