package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos.FeedbackRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos.FeedbackResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioService;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/gestores")
@Tag(name = "Gestores Feedback", description = "Endpoints para gestão de feedbacks por gestores.")
@Validated
public class GestorController {

    private final FuncionarioService funcionarioService;

    public GestorController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/{gestorId}/feedbacks")
    @PreAuthorize("hasRole('GESTOR')")
    @Operation(summary = "Lista feedbacks atribuídos por um gestor",
               description = "Retorna uma lista paginada de feedbacks atribuídos por um gestor, ordenados por data.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Feedbacks retornados com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido. Verifique os dados do request."),
                   @ApiResponse(responseCode = "404", description = "Gestor não encontrado. Verifique o ID do gestor fornecido."),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor. Um problema inesperado ocorreu.")
               })
    public ResponseEntity<PagedModel<EntityModel<FeedbackResponse>>> listarFeedbacksAtribuidos(
            @PathVariable Integer gestorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @AuthenticationPrincipal Usuario usuario) {

        // Validação do ID do gestor
        validateGestorId(gestorId, usuario);

        Sort sorting = sort.equals("asc") ? Sort.by("data").ascending() : Sort.by("data").descending();
        Pageable pageable = PageRequest.of(page, size, sorting);

        try {
            PagedModel<EntityModel<FeedbackResponse>> feedbacks = funcionarioService.lerFeedbacksAtribuidos(gestorId, pageable);
            return ResponseEntity.ok(feedbacks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{gestorId}/feedbacks/feedback/{feedbackId}")
    @PreAuthorize("hasRole('GESTOR')")
    @Operation(summary = "Obtém um feedback específico atribuído por um gestor",
               description = "Recupera um feedback específico atribuído por um gestor.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Feedback recuperado com sucesso"),
                   @ApiResponse(responseCode = "404", description = "Feedback ou gestor não encontrado."),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor.")
               })
    public ResponseEntity<EntityModel<FeedbackResponse>> getFeedbackById(
            @PathVariable Integer gestorId,
            @PathVariable Integer feedbackId,
            @AuthenticationPrincipal Usuario usuario) {

        validateGestorId(gestorId, usuario);

        FeedbackResponse feedbackResponse;

        try {
            feedbackResponse = funcionarioService.obterFeedbackDoGestorPorId(gestorId, feedbackId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        EntityModel<FeedbackResponse> feedbackEntityModel = EntityModel.of(feedbackResponse);
        feedbackEntityModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GestorController.class).getFeedbackById(gestorId, feedbackId, usuario)).withSelfRel());

        return ResponseEntity.ok(feedbackEntityModel);
    }

    @PostMapping("/{gestorId}/feedbacks/{funcionarioId}")
    @PreAuthorize("hasRole('GESTOR')")
    @Operation(summary = "Atribui um novo feedback a um funcionário",
               description = "Permite que um gestor atribua um feedback a um funcionário específico. O feedback inclui comentários e metas e é registrado na data atual.",
               responses = {
                   @ApiResponse(responseCode = "201", description = "Feedback atribuído com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido. Verifique os dados do request."),
                   @ApiResponse(responseCode = "404", description = "Funcionário ou gestor não encontrado. Verifique os IDs fornecidos."),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor. Um problema inesperado ocorreu.")
               })
    public ResponseEntity<String> atribuirFeedback(@PathVariable Integer gestorId,
            @PathVariable Integer funcionarioId,
            @RequestBody FeedbackRequest feedbackRequest,
            @AuthenticationPrincipal Usuario usuario) {

        validateGestorId(gestorId, usuario);

        try {
            String responseMessage = funcionarioService.atribuirUmNovoFeedback(funcionarioId, gestorId, feedbackRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @PutMapping("/{gestorId}/feedbacks/feedback/{feedbackId}")
    @PreAuthorize("hasRole('GESTOR')")
    @Operation(summary = "Atualiza um feedback existente",
               description = "Permite que um gestor atualize um feedback existente para um funcionário. A atualização pode incluir comentários, metas e a data do feedback.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Feedback atualizado com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido. Verifique os dados do request."),
                   @ApiResponse(responseCode = "404", description = "Feedback não encontrado. Verifique o ID do feedback fornecido."),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor. Um problema inesperado ocorreu.")
               })
    public ResponseEntity<String> atualizarFeedback(@PathVariable Integer gestorId, @PathVariable Integer feedbackId,
            @RequestBody FeedbackRequest feedbackRequest,
            @AuthenticationPrincipal Usuario usuario) {

        validateGestorId(gestorId, usuario);

        try {
            funcionarioService.atualizarFeedback(gestorId, feedbackId, feedbackRequest);
            return ResponseEntity.ok("Feedback atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
        }
    }

    @DeleteMapping("/{gestorId}/feedbacks/feedback/{feedbackId}")
    @PreAuthorize("hasRole('GESTOR')")
    @Operation(summary = "Remove um feedback atribuído por um gestor",
               description = "Permite que um gestor remova um feedback específico atribuído a um funcionário.",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Feedback removido com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido. Verifique os dados do request."),
                   @ApiResponse(responseCode = "404", description = "Feedback ou gestor não encontrado."),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor. Um problema inesperado ocorreu.")
               })
    public ResponseEntity<Void> apagarFeedback(
            @PathVariable Integer gestorId,
            @PathVariable Integer feedbackId,
            @AuthenticationPrincipal Usuario usuario) {

        validateGestorId(gestorId, usuario);

        try {
            funcionarioService.apagarFeedback(gestorId, feedbackId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private void validateGestorId(Integer gestorId, Usuario usuario) {
        if (!usuario.getId().equals(gestorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
