package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deyvidsalvatore.web.gestaomasterx.domain.administracao.DepartamentoCRUDService;
import com.deyvidsalvatore.web.gestaomasterx.domain.administracao.FuncionarioCRUDService;
import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.dtos.DepartamentoRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.dtos.DepartamentoResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioResponse;
import com.deyvidsalvatore.web.gestaomasterx.mappers.FuncionarioMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
@Tag(name = "Administração Endpoint")
@RestController
@RequestMapping("/api/v1/administrativo")
@Validated
public class AdministracaoController {

	private final FuncionarioCRUDService funcionarioCRUDService;
	private final DepartamentoCRUDService departamentoCRUDService;

	public AdministracaoController(FuncionarioCRUDService funcionarioCRUDService,
			DepartamentoCRUDService departamentoCRUDService) {
		this.funcionarioCRUDService = funcionarioCRUDService;
		this.departamentoCRUDService = departamentoCRUDService;
	}

	/** Funcionários CRUD */

	@Operation(summary = "Pega todos os funcionários por páginação", description = "Retorna os funcionários especificados pelo número da página, tamanho e ordem.", tags = {
			"Funcionários" }, parameters = {
					@Parameter(name = "page", description = "Número da página", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
					@Parameter(name = "size", description = "Número de funcionários por página", required = false, schema = @Schema(type = "integer", defaultValue = "10")),
					@Parameter(name = "sort", description = "Ordenação dos funcionários (asc/desc)", required = false, schema = @Schema(type = "string", defaultValue = "asc")) }, responses = {
							@ApiResponse(description = "Funcionários encontrados com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FuncionarioResponse.class)))),
							@ApiResponse(description = "Parâmetros inválidos na requisição", responseCode = "400", content = @Content),
							@ApiResponse(description = "Não autorizado", responseCode = "401", content = @Content),
							@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping("/funcionarios")
	public ResponseEntity<PagedModel<EntityModel<FuncionarioResponse>>> findAllFuncionarios(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "asc") String sort) {
		Sort sorting = sort.equals("asc") ? Sort.by("nomeCompleto").ascending() : Sort.by("nomeCompleto").descending();
		Pageable pageable = PageRequest.of(page, size, sorting);

		PagedModel<EntityModel<FuncionarioResponse>> funcionarios = this.funcionarioCRUDService
				.findAllFuncionarios(pageable);

		return ResponseEntity.ok(funcionarios);
	}

	@Operation(summary = "Pega um funcionário pelo ID", description = "Retorna as informações do funcionário especificado pelo ID.", tags = {
			"Funcionários" }, responses = {
					@ApiResponse(description = "Funcionário encontrado com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioResponse.class))),
					@ApiResponse(description = "Funcionário não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping("/funcionarios/{id}")
	public ResponseEntity<EntityModel<FuncionarioResponse>> findFuncionarioById(@PathVariable Integer id) {
		try {
			EntityModel<FuncionarioResponse> funcionario = this.funcionarioCRUDService.findFuncionarioById(id);
			return ResponseEntity.ok(funcionario);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Cria uma nova conta de funcionário", description = "Cria uma nova conta de funcionário a partir dos dados fornecidos.", tags = {
			"Funcionários" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do novo funcionário", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioRequest.class))), responses = {
					@ApiResponse(description = "Conta criada com sucesso", responseCode = "201", content = @Content),
					@ApiResponse(description = "Parâmetros inválidos na requisição", responseCode = "400", content = @Content),
					@ApiResponse(description = "Não autorizado", responseCode = "401", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping("/funcionarios")
	public ResponseEntity<String> criarNovaConta(@RequestBody FuncionarioRequest funcionarioRequest) {
		this.funcionarioCRUDService.criarContaParaFuncionario(FuncionarioMapper.newRequestToEntity(funcionarioRequest));
		return ResponseEntity.status(201).body("Uma nova conta foi criada e enviada ao e-mail requisitado.");
	}

	@Operation(summary = "Atualiza um funcionário", description = "Atualiza as informações do funcionário especificado pelo ID.", tags = {
			"Funcionários" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do funcionário", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioRequest.class))), responses = {
					@ApiResponse(description = "Funcionário atualizado com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioResponse.class))),
					@ApiResponse(description = "Funcionário não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Parâmetros inválidos na requisição", responseCode = "400", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PutMapping("/funcionarios/{id}")
	public ResponseEntity<FuncionarioResponse> atualizarFuncionario(@PathVariable Integer id,
			@RequestBody FuncionarioRequest request) {
		try {
			FuncionarioResponse funcionarioResponse = this.funcionarioCRUDService.atualizarFuncionario(id, request);
			return ResponseEntity.ok(funcionarioResponse);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Exclui um funcionário", description = "Remove o funcionário especificado pelo ID.", tags = {
			"Funcionários" }, responses = {
					@ApiResponse(description = "Funcionário excluído com sucesso", responseCode = "204", content = @Content),
					@ApiResponse(description = "Funcionário não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/funcionarios/{id}")
	public ResponseEntity<Void> excluirFuncionario(@PathVariable Integer id) {
		try {
			this.funcionarioCRUDService.deleteFuncionario(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	/** Departamentos CRUD */

	@Operation(summary = "Lista todos os departamentos com paginação", description = "Retorna uma lista de departamentos paginados com suporte a ordenação. Inclui links HATEOAS para navegação e detalhes dos departamentos.", tags = {
			"Departamentos" }, parameters = {
					@Parameter(name = "page", description = "Número da página", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
					@Parameter(name = "size", description = "Número de departamentos por página", required = false, schema = @Schema(type = "integer", defaultValue = "10")),
					@Parameter(name = "sort", description = "Ordenação dos departamentos (asc/desc)", required = false, schema = @Schema(type = "string", defaultValue = "asc")) }, responses = {
							@ApiResponse(description = "Departamentos encontrados com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DepartamentoResponse.class)))),
							@ApiResponse(description = "Parâmetros inválidos na requisição", responseCode = "400", content = @Content),
							@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping("/departamentos")
	public ResponseEntity<PagedModel<EntityModel<DepartamentoResponse>>> listAllDepartamentos(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "asc") String sort) {
		PagedModel<EntityModel<DepartamentoResponse>> pagedModel = this.departamentoCRUDService
				.listAllDepartamentos(page, size, sort);
		return ResponseEntity.ok(pagedModel);
	}

	@Operation(summary = "Pega um departamento pelo ID", description = "Retorna as informações do departamento especificado pelo ID.", tags = {
			"Departamentos" }, responses = {
					@ApiResponse(description = "Departamento encontrado com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoResponse.class))),
					@ApiResponse(description = "Departamento não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@GetMapping("/departamentos/{id}")
	public ResponseEntity<DepartamentoResponse> findDepartamentoById(@PathVariable Integer id) {
		try {
			DepartamentoResponse departamentoResponse = this.departamentoCRUDService.getDepartamentoById(id);
			return ResponseEntity.ok(departamentoResponse);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Cria um novo departamento", description = "Cria um novo departamento a partir dos dados fornecidos.", tags = {
			"Departamentos" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do novo departamento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoRequest.class))), responses = {
					@ApiResponse(description = "Departamento criado com sucesso", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoResponse.class))),
					@ApiResponse(description = "Parâmetros inválidos na requisição", responseCode = "400", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping("/departamentos")
	public ResponseEntity<DepartamentoResponse> createDepartamento(@RequestBody DepartamentoRequest request) {
		DepartamentoResponse departamentoResponse = this.departamentoCRUDService.saveDepartamento(request);
		return ResponseEntity.status(201).body(departamentoResponse);
	}

	@Operation(summary = "Atualiza um departamento", description = "Atualiza as informações do departamento especificado pelo ID.", tags = {
			"Departamentos" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados atualizados do departamento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoRequest.class))), responses = {
					@ApiResponse(description = "Departamento atualizado com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoResponse.class))),
					@ApiResponse(description = "Departamento não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Parâmetros inválidos na requisição", responseCode = "400", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PutMapping("/departamentos/{id}")
	public ResponseEntity<DepartamentoResponse> updateDepartamento(@PathVariable Integer id,
			@RequestBody DepartamentoRequest request) {
		try {
			DepartamentoResponse departamentoResponse = this.departamentoCRUDService.updateDepartamento(id, request);
			return ResponseEntity.ok(departamentoResponse);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Exclui um departamento", description = "Remove o departamento especificado pelo ID.", tags = {
			"Departamentos" }, responses = {
					@ApiResponse(description = "Departamento excluído com sucesso", responseCode = "204", content = @Content),
					@ApiResponse(description = "Departamento não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/departamentos/{id}")
	public ResponseEntity<Void> deleteDepartamento(@PathVariable Integer id) {
		try {
			this.departamentoCRUDService.deleteDepartamento(id);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException | IllegalStateException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Adiciona um gestor a um departamento", description = "Associa um funcionário ao departamento como gestor.", tags = {
			"Departamentos" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ID do funcionário a ser adicionado como gestor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioRequest.class))), responses = {
					@ApiResponse(description = "Gestor adicionado com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoResponse.class))),
					@ApiResponse(description = "Departamento ou funcionário não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Funcionário não é um gestor", responseCode = "400", content = @Content),
					@ApiResponse(description = "Funcionário já associado ao departamento", responseCode = "400", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PostMapping("/departamentos/{departamentoId}/gestores/{funcionarioId}")
	public ResponseEntity<DepartamentoResponse> addGestorToDepartamento(@PathVariable Integer departamentoId,
			@PathVariable Integer funcionarioId) {
		try {
			DepartamentoResponse departamentoResponse = this.departamentoCRUDService
					.addGestorToDepartamento(departamentoId, funcionarioId);
			return ResponseEntity.ok(departamentoResponse);
		} catch (IllegalArgumentException | IllegalStateException ex) {
			return ResponseEntity.badRequest().body(new DepartamentoResponse());
		}
	}

	@Operation(summary = "Remove todos os gestores de um departamento", description = "Desassocia todos os gestores do departamento especificado pelo ID.", tags = {
			"Departamentos" }, responses = {
					@ApiResponse(description = "Gestores removidos com sucesso", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DepartamentoResponse.class))),
					@ApiResponse(description = "Departamento não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@DeleteMapping("/departamentos/{id}/gestores")
	public ResponseEntity<DepartamentoResponse> removeAllGestoresFromDepartamento(@PathVariable Integer id) {
		try {
			DepartamentoResponse departamentoResponse = this.departamentoCRUDService
					.removeAllGestoresFromDepartamento(id);
			return ResponseEntity.ok(departamentoResponse);
		} catch (IllegalArgumentException ex) {
			return ResponseEntity.notFound().build();
		} catch (Exception ex) {
			return ResponseEntity.status(500).build();
		}
	}

	@Operation(summary = "Promove um funcionário para gestor", description = "Promove um funcionário para o papel de gestor.", tags = {
			"Funcionários" }, responses = {
					@ApiResponse(description = "Funcionário promovido com sucesso", responseCode = "204", content = @Content),
					@ApiResponse(description = "Funcionário não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	@PatchMapping("/funcionarios/{funcionarioId}/promover")
	public ResponseEntity<Void> promoverFuncionario(@PathVariable Integer funcionarioId) {
		try {
			funcionarioCRUDService.promoverParaGestor(funcionarioId);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}
