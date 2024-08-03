package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.exceptions;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FuncionarioNotFoundException extends RuntimeException {
	
	@Serial private static final long serialVersionUID = 1L;

	public FuncionarioNotFoundException() {
        super("Funcionário não encontrado com ID especificado!");
    }
}