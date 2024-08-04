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
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioResponse;
import com.deyvidsalvatore.web.gestaomasterx.mappers.FuncionarioMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Administração Endpoint")
@RestController
@RequestMapping("/api/v1/administrativo")
public class AdministracaoController {

	private final FuncionarioCRUDService funcionarioCRUDService;

	public AdministracaoController(FuncionarioCRUDService funcionarioCRUDService) {
		this.funcionarioCRUDService = funcionarioCRUDService;
	}

	@Operation(summary = "Pega todos os funcionários por páginação", description = "Retorna os funcionários especificados pelo número da página, tamanho e ordem", tags = {
			"Funcionários" }, responses = { @ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FuncionarioResponse.class))) }),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content) })
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
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Funcionário não encontrado", responseCode = "404", content = @Content),
					@ApiResponse(description = "Erro interno do servidor", responseCode = "500", content = @Content) })
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
