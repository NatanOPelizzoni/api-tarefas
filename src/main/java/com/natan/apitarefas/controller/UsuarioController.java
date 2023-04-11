package com.natan.apitarefas.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natan.apitarefas.dto.UsuarioDto;
import com.natan.apitarefas.service.UsuarioService;

@RestController
@RequestMapping(value = "/api-tarefas/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService UsuarioService;


    @PostMapping(value = "/inserir")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> inserir(@RequestBody UsuarioDto usuario) {
        try {
            UsuarioDto usuarioSalvo = UsuarioService.salvar(usuario);
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("id", usuarioSalvo.getId());
            responseBody.put("nome", usuarioSalvo.getNome());
            responseBody.put("email", usuarioSalvo.getEmail());
            responseBody.put("token", usuarioSalvo.getToken());
            return ResponseEntity.ok().body(responseBody);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("uk_")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado!");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar usuário: " + e.getMessage());
        }
    }
    
}
