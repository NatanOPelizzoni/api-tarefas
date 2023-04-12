package com.natan.apitarefas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.natan.apitarefas.dto.TarefaDto;

public interface TarefaRepository extends JpaRepository<TarefaDto, Long>{

        
    @Query(
        nativeQuery = true, 
        value = "SELECT ta.* "
              + "FROM tb_usuario us "
              + "JOIN tb_tarefa ta ON ta.usuario_id = us.id "
              + "WHERE us.id = :usuario_id "
              + "ORDER BY ta.titulo;"
    )
    public List<TarefaDto> listarTarefasPorUsuario(@Param("usuario_id") Long usuario_id);
}
