package com.natan.apitarefas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public UsuarioDto inserir(@RequestBody UsuarioDto usuario){
        return UsuarioService.salvar(usuario);
    }
}
