package br.com.uniamerica.Logidutra.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column
    private String senha;

    @Column(name = "age")
    private Integer idade;
    private Boolean perm;
}
