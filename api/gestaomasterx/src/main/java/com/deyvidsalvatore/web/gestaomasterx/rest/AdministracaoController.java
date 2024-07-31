package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deyvidsalvatore.web.gestaomasterx.domain.administracao.FuncionarioCRUDService;
import com.deyvidsalvatore.web.gestaomasterx.dto.funcionario.NewFuncionarioRequest;
import com.deyvidsalvatore.web.gestaomasterx.mappers.FuncionarioMapper;

@RestController
@RequestMapping("/api/v1/administrativo")
public class AdministracaoController {
	
	private final FuncionarioCRUDService funcionarioCRUDService;

	public AdministracaoController(FuncionarioCRUDService funcionarioCRUDService) {
		this.funcionarioCRUDService = funcionarioCRUDService;
	}
	
	@PostMapping("/funcionarios")
	public ResponseEntity<String> criarNovaConta(@RequestBody NewFuncionarioRequest funcionarioRequest ) {
		this.funcionarioCRUDService.criarContaParaFuncionario(FuncionarioMapper.newRequestToEntity(funcionarioRequest));
		return ResponseEntity.status(201).body("Uma nova conta foi criada e enviada ao e-mail requisitado.");
		
	}
	
	

}
