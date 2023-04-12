package com.natan.apitarefas.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.natan.apitarefas.dto.LoginDto;
import com.natan.apitarefas.dto.TarefaDto;
import com.natan.apitarefas.dto.UsuarioDto;
import com.natan.apitarefas.service.TarefaService;
import com.natan.apitarefas.service.UsuarioService;
import com.natan.apitarefas.utils.TokenJWT;

@RestController
@RequestMapping(value = "/api-tarefas/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService UsuarioService;

    @Autowired
    private TarefaService tarefaService;

    @Autowired
	private ModelMapper modelMapper;

    @PostMapping(value = "/inserir")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> inserir(@RequestBody UsuarioDto usuario, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
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
    
    @DeleteMapping("/deletar/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> deletar(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            UsuarioService.buscarPorId(id)
            .map(usuario -> {
                UsuarioService.deletarPorId(usuario.getId());
                return Void.TYPE;
            }).orElseThrow(() -> new EmptyResultDataAccessException(1));
            return ResponseEntity.ok().body("Usuário deletado com sucesso");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    @PutMapping(value = "/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public Object atualizar(@RequestBody UsuarioDto usuario, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            return UsuarioService.buscarPorId(usuario.getId()).map(usuarioBase -> {
                modelMapper.map(usuario, usuarioBase);
                UsuarioDto usuarioSalvo = UsuarioService.salvar(usuarioBase);
                return ResponseEntity.ok(usuarioSalvo);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
    
    @PostMapping(value = "/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        UsuarioDto usuarioDTO;
        if (loginDTO.getEmail() != null && !loginDTO.getEmail().isEmpty() && loginDTO.getSenha() != null && !loginDTO.getSenha().isEmpty()) {
            usuarioDTO = UsuarioService.buscarPorEmail(loginDTO.getEmail());
            if (usuarioDTO != null && usuarioDTO.getId() != null) {
                if (usuarioDTO.getSenha().equals(loginDTO.getSenha())) {
                    usuarioDTO.setToken(TokenJWT.processarTokenJWT(loginDTO.getEmail()));
                    return ResponseEntity.ok(usuarioDTO);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado");
            }
        } else { 
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios");
        }
    }

    @GetMapping(value = "/listar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UsuarioDto>> listar(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            TokenJWT.validarToken(token);
            List<UsuarioDto> usuarios = UsuarioService.listar();
            return ResponseEntity.ok().body(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping(value = "/buscar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> buscar(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            UsuarioDto usuario = UsuarioService.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado."));
            return ResponseEntity.ok().body(usuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar usuário: " + e.getMessage());
        }
    }
    
    @GetMapping(value = "/listar-tarefas/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> listarTarefas(@PathVariable("id") Long id, @RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Token é obrigatório");
        }
        try {
            TokenJWT.validarToken(token);
            UsuarioDto usuario = UsuarioService.buscarPorId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario não encontrado."));

            List<TarefaDto> tarefas = tarefaService.listarTarefasPorUsuario(usuario.getId());
            return ResponseEntity.ok().body(tarefas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar usuário: " + e.getMessage());
        }
    }
    
}
