package com.deyvidsalvatore.web.gestaomasterx.domain.usuario.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UsuarioNaoEncontradoException extends RuntimeException {
	
	@Serial private static final long serialVersionUID = 1L;

	public UsuarioNaoEncontradoException(String username) {
        super("Usuário com username '" + username + "' não encontrado.");
    }
}