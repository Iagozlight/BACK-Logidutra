package br.com.uniamerica.logidutra.entity;

import br.com.uniamerica.logidutra.enums.Role;
import br.com.uniamerica.logidutra.enums.StatusOperacional;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column(name = "idade")
    private Integer idade;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "login", nullable = false)
    private String login;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOperacional status;

    @OneToMany(mappedBy = "usuario")
    private List<Romaneios> romaneiosComoMotorista;

}
