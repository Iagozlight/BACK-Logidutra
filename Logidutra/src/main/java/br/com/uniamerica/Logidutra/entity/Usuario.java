package br.com.uniamerica.Logidutra.entity;


import jakarta.persistence.*;
import lombok.*;

import javax.management.relation.Role;

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
