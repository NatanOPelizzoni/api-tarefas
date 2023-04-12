package com.natan.apitarefas.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "tb_tarefa_comentario")
public class TarefaComentarioDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comentario", nullable = false)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    private TarefaDto tarefa;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioDto usuario;
}
