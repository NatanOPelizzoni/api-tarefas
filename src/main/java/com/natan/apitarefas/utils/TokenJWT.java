package com.natan.apitarefas.utils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

public class TokenJWT {
    private static Key chave;

    /**
	 * método construtor que recebe a chave
	 * @param chave
	 */
    public TokenJWT(Key chave) {
		TokenJWT.chave = chave;
	}

	/**
	 * Gerar uma chave a ser utilizada na assinatura do token.
	 *
	 * @return
	 */
	private static Key gerarChave() {
		/*
		 * A string "senhasupersecreta" após a execução de SHA-256 
         * 393990aba6129c02909de51b449c8f269972397e1807ccc4403fc7525cf901ec
         * após isso criptografado em EncodeBase64
		 */
		String keyString = "MzkzOTkwYWJhNjEyOWMwMjkwOWRlNTFiNDQ5YzhmMjY5OTcyMzk3ZTE4MDdjY2M0NDAzZmM3NTI1Y2Y5MDFlYw==";
		Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "HmacSHA256");
		return key;
	}

    /**
	 * Método que gera uma data de expiração que posteriormente vai definir a
	 * validade de um token em minutos...
	 *
	 * @param tempoEmMinutos
	 * @return
	 */
	public static Date definirDataDeExpiracao(long tempoEmMinutos) {
		LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(tempoEmMinutos);
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

    /**
	 * Método responsável por processar a geração do token jwt
	 *
	 * @param usuarioEmail
	 * @return
	 */
	public static String processarTokenJWT(String usuarioEmail) {
		Key chave = gerarChave();
		TokenJWT token = new TokenJWT(chave);
		Date dataExpiracao = definirDataDeExpiracao(30L);
		return token.gerarToken(usuarioEmail, dataExpiracao);
	}

    /**
	 * Método responsável por validar o token
	 * 
	 * @param token
	 */
	public static boolean validarToken(String token) {
		chave = gerarChave();
		boolean tokenValido = false;
		if (token != null) {
			try {
				// Gerando a Claims
				Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token);
				tokenValido = true;
				// exception referente ao token expirado...
			} catch (ExpiredJwtException e) {
				// retorna para o App a exceção
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token expirado!");
				// exeption referente a problema no token...
			} catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
				// retorna para o App a exceção
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido!");
			}
		} else {
			// retorna para o App a exceção
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token inválido!!");
		}
		return tokenValido;
	}

    /**
	 * Gerando token JWT
	 *
	 * @param usuarioEmail
	 * @param dataExpiracao
	 * @return
	 */
	public String gerarToken(String usuarioEmail, Date dataExpiracao) {
		return Jwts.builder().setHeaderParam("typ", "JWT") // definindo cabeçalho
				.setSubject(usuarioEmail) // assunto do token
				.setIssuer("api-tarefas") // quem é o emissor do token
				.setIssuedAt(new Date()) // data de criação
				.claim("password", "senhasupersecreta")
				.setExpiration(dataExpiracao) // data de expiração do token
				.signWith(chave, SignatureAlgorithm.HS256) // assinatura do token
				.compact(); // contruir o JWT;
	}

    
	/**
	 * Recuperando assunto do Token
	 *
	 * @return
	 */
	public String recuperarSubjectDoToken(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token);
		return claimsJws.getBody().getSubject();
	}

	/**
	 * Recuperando emissor do Token
	 *
	 * @return
	 */
	public String recuperarIssuerDoToken(String token) {
		Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token);
		return claimsJws.getBody().getIssuer();
	}

	/**
	 * Método que lê um token e retorna o claim especifico.
	 *
	 * @param token
	 * @param claim
	 * @return
	 */
	public String resuperarClaim(String token, String claim) {
		String subject = "";
		if (token != null && !token.equals("")) {
			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(chave).build().parseClaimsJws(token);
			subject = claimsJws.getBody().get(claim).toString();
		}
		return subject;
	}
}
