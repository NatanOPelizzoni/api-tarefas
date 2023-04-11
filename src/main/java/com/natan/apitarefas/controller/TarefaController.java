package com.natan.apitarefas.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.natan.apitarefas.dto.TarefaDto;
import com.natan.apitarefas.service.TarefaService;
import com.natan.apitarefas.utils.TokenJWT;

@RestController
@RequestMapping(value = "/api-tarefas/tarefa")
public class TarefaController {
    
    @Autowired
    private TarefaService tarefaService;

    
    @Autowired
	private ModelMapper modelMapper;

    @PostMapping(value = "/inserir")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> inserir(@RequestBody TarefaDto tarefa, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isEmpty()) {
            return ResponseEntity.badRequest().body("O título da tarefa é obrigatório!");
        }
        if (tarefa.getUsuario() == null) {
            return ResponseEntity.badRequest().body("O id do usuário que está inserindo a tarefa é obrigatório!");
        }
        try {
            TokenJWT.validarToken(token);
            tarefaService.salvar(tarefa);
            return ResponseEntity.ok().body("Tarefa criada com sucesso!");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar tarefa: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar tarefa: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletar/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> deletar(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            tarefaService.buscarPorId(id)
            .map(tarefa -> {
                tarefaService.deletarPorId(tarefa.getId());
                return Void.TYPE;
            }).orElseThrow(() -> new EmptyResultDataAccessException(1));
            return ResponseEntity.ok().body("Tarefa deletada com sucesso");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar tarefa: " + e.getMessage());
        }
    }

    @PutMapping(value = "/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public Object atualizar(@RequestBody TarefaDto tarefa, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            return tarefaService.buscarPorId(tarefa.getId()).map(tarefaBase -> {
                modelMapper.map(tarefa, tarefaBase);
                TarefaDto tarefaSalva = tarefaService.salvar(tarefaBase);
                return ResponseEntity.ok(tarefaSalva);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tarefa não encontrado."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar tarefa: " + e.getMessage());
        }
    }

    @GetMapping(value = "/listar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TarefaDto>> listar(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            TokenJWT.validarToken(token);
            List<TarefaDto> tarefas = tarefaService.listar();
            return ResponseEntity.ok().body(tarefas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping(value = "/buscar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> buscar(@PathVariable("id") Long id, 
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            TarefaDto tarefa = tarefaService.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tarefa não encontrada."));
            return ResponseEntity.ok().body(tarefa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar tarefa: " + e.getMessage());
        }
    }
}
