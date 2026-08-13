package br.com.uniamerica.Logidutra.entity;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity

@Getter
@Setter
public class Usuario {
    private Long id;

    private String nome;
    private String senha;
    private Integer ano;
    private Boolean perm;
}
