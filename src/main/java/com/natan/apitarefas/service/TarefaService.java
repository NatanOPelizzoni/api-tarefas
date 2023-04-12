package com.natan.apitarefas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natan.apitarefas.dto.TarefaDto;
import com.natan.apitarefas.repository.TarefaRepository;

@Service
public class TarefaService {
  @Autowired
  private TarefaRepository tarefaRepository;

  public TarefaDto salvar(TarefaDto dto) {
    return tarefaRepository.save(dto);
  }

  public void deletarPorId(Long id) {
    tarefaRepository.deleteById(id);
  }

  public Optional<TarefaDto> buscarPorId(Long id) {
    return tarefaRepository.findById(id);
  }

  public List<TarefaDto> listar() {
    return tarefaRepository.findAll();
  }

  public List<TarefaDto> listarTarefasPorUsuario(Long id){
    return tarefaRepository.listarTarefasPorUsuario(id);
  }
}
