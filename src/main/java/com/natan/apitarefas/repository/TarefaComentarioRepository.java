package com.natan.apitarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.apitarefas.dto.TarefaComentarioDto;

public interface TarefaComentarioRepository extends JpaRepository<TarefaComentarioDto, Long>{
}
