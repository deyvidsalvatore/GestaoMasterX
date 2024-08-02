package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deyvidsalvatore.web.gestaomasterx.domain.administracao.FuncionarioCRUDService;
import com.deyvidsalvatore.web.gestaomasterx.dto.funcionario.FuncionarioResponse;
import com.deyvidsalvatore.web.gestaomasterx.dto.funcionario.FuncionarioRequest;
import com.deyvidsalvatore.web.gestaomasterx.mappers.FuncionarioMapper;

@RestController
@RequestMapping("/api/v1/administrativo")
public class AdministracaoController {

	private final FuncionarioCRUDService funcionarioCRUDService;

	public AdministracaoController(FuncionarioCRUDService funcionarioCRUDService) {
		this.funcionarioCRUDService = funcionarioCRUDService;
	}

	@GetMapping("/funcionarios")
	public ResponseEntity<PagedModel<EntityModel<FuncionarioResponse>>> findAllFuncionarios(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "asc") String sort) {

		Sort sorting = sort.equals("asc") ? Sort.by("nomeCompleto").ascending() : Sort.by("nomeCompleto").descending();
		Pageable pageable = PageRequest.of(page, size, sorting);

		PagedModel<EntityModel<FuncionarioResponse>> funcionarios = this.funcionarioCRUDService.findAllFuncionarios(pageable);

		return ResponseEntity.ok(funcionarios);
	}

	@GetMapping("/funcionarios/{id}")
	public ResponseEntity<EntityModel<FuncionarioResponse>> findFuncionarioById(@PathVariable Integer id) {
		try {
			EntityModel<FuncionarioResponse> funcionario = this.funcionarioCRUDService.findFuncionarioById(id);
			return ResponseEntity.ok(funcionario);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/funcionarios")
	public ResponseEntity<String> criarNovaConta(@RequestBody FuncionarioRequest funcionarioRequest) {
		this.funcionarioCRUDService.criarContaParaFuncionario(FuncionarioMapper.newRequestToEntity(funcionarioRequest));
		return ResponseEntity.status(201).body("Uma nova conta foi criada e enviada ao e-mail requisitado.");
	}
	
	 @PutMapping("/funcionarios/{id}")
	    public ResponseEntity<FuncionarioResponse> atualizarFuncionario(
	            @PathVariable Integer id, 
	            @RequestBody FuncionarioRequest request) {
	        try {
	            FuncionarioResponse funcionarioResponse = this.funcionarioCRUDService.atualizarFuncionario(id, request);
	            return ResponseEntity.ok(funcionarioResponse);
	        } catch (IllegalArgumentException ex) {
	            return ResponseEntity.notFound().build();
	        }
	    }

	    @DeleteMapping("/funcionarios/{id}")
	    public ResponseEntity<Void> excluirFuncionario(@PathVariable Integer id) {
	        try {
	            this.funcionarioCRUDService.deleteFuncionario(id);
	            return ResponseEntity.noContent().build();
	        } catch (IllegalArgumentException ex) {
	            return ResponseEntity.notFound().build();
	        }
	    }

}
