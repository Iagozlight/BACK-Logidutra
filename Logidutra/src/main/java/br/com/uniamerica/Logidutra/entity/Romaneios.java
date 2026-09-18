package br.com.uniamerica.Logidutra.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Romaneios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column
    private LocalDate data;

    @OneToMany(mappedBy = "romaneios")
    private List<Produto> produtoList;

    @ManyToMany
    @JoinTable(
            name = "romaneio_cliente",
            joinColumns = @JoinColumn(name = "romaneio_id"),
            inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private List<ClienteEntity> clientes;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoEntity veiculo;
}
