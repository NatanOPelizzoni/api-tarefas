package com.natan.apitarefas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natan.apitarefas.dto.TarefaComentarioDto;
import com.natan.apitarefas.repository.TarefaComentarioRepository;

@Service
public class TarefaComentarioService {
    @Autowired
    private TarefaComentarioRepository tarefaComentarioRepository;

    public TarefaComentarioDto salvar(TarefaComentarioDto dto) {
        return tarefaComentarioRepository.save(dto);
    }

    public void deletarPorId(Long id) {
        tarefaComentarioRepository.deleteById(id);
    }

    public Optional<TarefaComentarioDto> buscarPorId(Long id) {
        return tarefaComentarioRepository.findById(id);
    }

    public List<TarefaComentarioDto> listar() {
        return tarefaComentarioRepository.findAll();
    }
}
