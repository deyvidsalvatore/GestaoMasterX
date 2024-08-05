package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos.FeedbackResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioService;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.ListaHorasResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRole;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/funcionarios")
@Tag(name = "Funcionários Horas/Feedback Endpoint", description = "Endpoints para gerenciamento das horas e feedbacks dos funcionários.")
@Validated
public class FuncionarioController {

	private final FuncionarioService funcionarioService;
	private final PagedResourcesAssembler<HoraResponse> pagedResourcesAssembler;

	public FuncionarioController(FuncionarioService funcionarioService,
			PagedResourcesAssembler<HoraResponse> pagedResourcesAssembler) {
		this.funcionarioService = funcionarioService;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@GetMapping("/{funcionarioId}/horas")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	@Operation(summary = "Busca horas do funcionário com paginação", description = "Retorna uma lista paginada de horas registradas para um funcionário específico, agrupadas por data em ordem crescente.", responses = {
			@ApiResponse(responseCode = "200", description = "Horas do funcionário encontradas"),
			@ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
			@ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<PagedModel<EntityModel<HoraResponse>>> buscarHorasDoFuncionarioPaginado(
			@PathVariable Integer funcionarioId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "asc") String sort,
			@AuthenticationPrincipal Usuario usuario) {

		validateFuncionarioId(funcionarioId, usuario.getFuncionario().getId());

		PageRequest pageRequest = PageRequest.of(page, size,
				Sort.by(sort.equals("asc") ? Sort.Order.asc("data") : Sort.Order.desc("data")));
		Page<HoraResponse> pageResponse = funcionarioService.buscarHorasDoFuncionarioPaginado(funcionarioId,
				pageRequest);
		PagedModel<EntityModel<HoraResponse>> pagedModel = pagedResourcesAssembler.toModel(pageResponse);
		return ResponseEntity.ok(pagedModel);
	}

	@GetMapping("/{funcionarioId}/horas/totais")
	@PreAuthorize("hasRole('FUNCIONARIO') or hasRole('GESTOR')")
	@Operation(summary = "Busca total de horas do funcionário", description = "Retorna o total de horas trabalhadas por um funcionário específico.", responses = {
			@ApiResponse(responseCode = "200", description = "Horas totais do funcionário encontradas"),
			@ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
			@ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<ListaHorasResponse> buscarHorasDoFuncionarioTotais(@PathVariable Integer funcionarioId,
			@AuthenticationPrincipal Usuario usuario) {
		try {
			if (usuario.getRole().equals(UsuarioRole.GESTOR)
					|| funcionarioId.equals(usuario.getFuncionario().getId())) {
				ListaHorasResponse response = funcionarioService.buscarHorasDoFuncionarioTotais(funcionarioId);
				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/{funcionarioId}/horas")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	@Operation(summary = "Atribui horas a um funcionário", description = "Registra um novo período de horas trabalhadas para um funcionário específico.", responses = {
			@ApiResponse(responseCode = "201", description = "Horas registradas com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
			@ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<HoraResponse> atribuirHoras(@PathVariable Integer funcionarioId,
			@RequestBody HoraRequest horaRequest, @AuthenticationPrincipal Usuario usuario) {

		validateFuncionarioId(funcionarioId, usuario.getFuncionario().getId());

		HoraResponse horaResponse = funcionarioService.atribuirHorasDoFuncionario(funcionarioId, horaRequest);
		return ResponseEntity.status(201).body(horaResponse);
	}

	@PutMapping("/{funcionarioId}/horas/{registroHoraId}")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	@Operation(summary = "Atualiza horas de um funcionário", description = "Atualiza um registro de horas específico para um funcionário.", responses = {
			@ApiResponse(responseCode = "200", description = "Horas atualizadas com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
			@ApiResponse(responseCode = "404", description = "Registro de horas ou funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<HoraResponse> atualizarHorasDoFuncionario(@PathVariable Integer funcionarioId,
			@PathVariable Integer registroHoraId, @RequestBody HoraRequest horasAtualizadas,
			@AuthenticationPrincipal Usuario usuario) {

		validateFuncionarioId(funcionarioId, usuario.getFuncionario().getId());

		try {
			HoraResponse resposta = funcionarioService.atualizarHorasDoFuncionario(funcionarioId, registroHoraId,
					horasAtualizadas);
			return ResponseEntity.ok(resposta);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{funcionarioId}/horas/{registroHoraId}")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	@Operation(summary = "Remove um registro de horas de um funcionário", description = "Remove um registro de horas específico de um funcionário.", responses = {
			@ApiResponse(responseCode = "204", description = "Horas removidas com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
			@ApiResponse(responseCode = "404", description = "Registro de horas ou funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<Void> removerHorasDoFuncionario(@PathVariable Integer funcionarioId,
			@PathVariable Integer registroHoraId, @AuthenticationPrincipal Usuario usuario) {

		validateFuncionarioId(funcionarioId, usuario.getFuncionario().getId());

		try {
			funcionarioService.removerHorasDoFuncionario(funcionarioId, registroHoraId);
			return ResponseEntity.noContent().build();
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/{funcionarioId}/feedbacks")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	@Operation(summary = "Lista feedbacks atribuídos ao funcionário", description = "Retorna uma lista paginada de feedbacks atribuídos ao funcionário.", responses = {
			@ApiResponse(responseCode = "200", description = "Feedbacks retornados com sucesso"),
			@ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
			@ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<PagedModel<EntityModel<FeedbackResponse>>> listarFeedbacksPaginados(
			@PathVariable Integer funcionarioId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "asc") String sort,
			@AuthenticationPrincipal Usuario usuario) {

		validateFuncionarioId(funcionarioId, usuario.getFuncionario().getId());

		Pageable pageable = PageRequest.of(page, size,
				Sort.by(sort.equals("asc") ? Sort.Order.asc("data") : Sort.Order.desc("data")));
		PagedModel<EntityModel<FeedbackResponse>> feedbacks = funcionarioService
				.listarFeedbacksPaginadosDoFuncionario(funcionarioId, pageable);
		return ResponseEntity.ok(feedbacks);
	}

	@GetMapping("/{funcionarioId}/feedbacks/{feedbackId}")
	@PreAuthorize("hasRole('FUNCIONARIO')")
	@Operation(summary = "Obtém um feedback específico atribuído ao funcionário", description = "Retorna os detalhes de um feedback específico atribuído a um funcionário.", responses = {
			@ApiResponse(responseCode = "200", description = "Feedback retornado com sucesso"),
			@ApiResponse(responseCode = "404", description = "Feedback ou funcionário não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	public ResponseEntity<EntityModel<FeedbackResponse>> obterFeedbackDoFuncionarioPorId(
			@PathVariable Integer funcionarioId, @PathVariable Integer feedbackId,
			@AuthenticationPrincipal Usuario usuario) {

		validateFuncionarioId(funcionarioId, usuario.getFuncionario().getId());

		try {
			EntityModel<FeedbackResponse> feedbackEntityModel = funcionarioService.getFeedbackById(funcionarioId,
					feedbackId);
			return ResponseEntity.ok(feedbackEntityModel);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}
	}

	private boolean validateFuncionarioId(Integer funcionarioId, Integer compareId) {
		if (!funcionarioId.equals(compareId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		return true;
	}
}
