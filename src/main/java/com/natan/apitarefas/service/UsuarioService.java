package com.natan.apitarefas.service;

import java.util.Optional;

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

    public void deletarPorId(Long id) {
		usuarioRepository.deleteById(id);
	}

    public Optional<UsuarioDto> buscarPorId(Long id) {
		return usuarioRepository.findById(id);
	}

    public UsuarioDto buscarPorEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}
}
