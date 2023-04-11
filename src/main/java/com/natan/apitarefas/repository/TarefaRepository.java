package com.natan.apitarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.apitarefas.dto.TarefaDto;

public interface TarefaRepository extends JpaRepository<TarefaDto, Long>{
}
