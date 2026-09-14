package br.com.uniamerica.Logidutra.entity;

import br.com.uniamerica.Logidutra.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "usuarios")
public class Usuario {
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
}
