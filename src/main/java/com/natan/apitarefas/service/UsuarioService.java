package com.natan.apitarefas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.natan.apitarefas.dto.UsuarioDto;
import com.natan.apitarefas.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public UsuarioDto salvar(UsuarioDto dto){
        return usuarioRepository.save(dto);
    }
}
