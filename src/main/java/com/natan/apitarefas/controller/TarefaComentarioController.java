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

import com.natan.apitarefas.dto.TarefaComentarioDto;
import com.natan.apitarefas.service.TarefaComentarioService;
import com.natan.apitarefas.utils.TokenJWT;

@RestController
@RequestMapping(value = "/api-tarefas/tarefa-comentario")
public class TarefaComentarioController {
    
    @Autowired
    private TarefaComentarioService tarefaComentarioService;

    
    @Autowired
	private ModelMapper modelMapper;

    @PostMapping(value = "/inserir")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> inserir(@RequestBody TarefaComentarioDto comentario, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        if (comentario.getComentario() == null || comentario.getComentario().isEmpty()) {
            return ResponseEntity.badRequest().body("O comentário é obrigatório!");
        }
        if (comentario.getTarefa() == null) {
            return ResponseEntity.badRequest().body("O id da tarefa que está recebendo o comentário é obrigatório!");
        }
        if (comentario.getUsuario() == null) {
            return ResponseEntity.badRequest().body("O id do usuário que está inserindo o comentário é obrigatório!");
        }
        try {
            TokenJWT.validarToken(token);
            tarefaComentarioService.salvar(comentario);
            return ResponseEntity.ok().body("Comentário criado com sucesso!");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar comentário: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar comentário: " + e.getMessage());
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
            tarefaComentarioService.buscarPorId(id)
            .map(comentario -> {
                tarefaComentarioService.deletarPorId(comentario.getId());
                return Void.TYPE;
            }).orElseThrow(() -> new EmptyResultDataAccessException(1));
            return ResponseEntity.ok().body("Comentário deletado com sucesso");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comentário não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar comentário: " + e.getMessage());
        }
    }

    @PutMapping(value = "/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public Object atualizar(@RequestBody TarefaComentarioDto comentario, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            return tarefaComentarioService.buscarPorId(comentario.getId()).map(comentarioBase -> {
                modelMapper.map(comentario, comentarioBase);
                TarefaComentarioDto comentarioSalvo = tarefaComentarioService.salvar(comentarioBase);
                return ResponseEntity.ok(comentarioSalvo);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comentário não encontrado."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar comentário: " + e.getMessage());
        }
    }

    @GetMapping(value = "/listar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TarefaComentarioDto>> listar(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            TokenJWT.validarToken(token);
            List<TarefaComentarioDto> tarefas = tarefaComentarioService.listar();
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
            TarefaComentarioDto tarefa = tarefaComentarioService.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tarefa não encontrada."));
            return ResponseEntity.ok().body(tarefa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar tarefa: " + e.getMessage());
        }
    }
}
