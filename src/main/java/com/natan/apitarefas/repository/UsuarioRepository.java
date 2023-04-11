package com.natan.apitarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.natan.apitarefas.dto.UsuarioDto;

public interface UsuarioRepository extends JpaRepository<UsuarioDto, Long>{

}
