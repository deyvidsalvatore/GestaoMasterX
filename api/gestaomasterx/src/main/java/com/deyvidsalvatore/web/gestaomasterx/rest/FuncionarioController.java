package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
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

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioService;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.ListaHorasResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/funcionarios")
@Tag(name = "Funcionários Horas/Feedback Endpoint", description = "Endpoints para gerenciamento das horas dos funcionários.")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final PagedResourcesAssembler<HoraResponse> pagedResourcesAssembler;

    public FuncionarioController(FuncionarioService funcionarioService, PagedResourcesAssembler<HoraResponse> pagedResourcesAssembler) {
        this.funcionarioService = funcionarioService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{funcionarioId}/horas")
    @Operation(summary = "Busca horas do funcionário com paginação",
               description = "Retorna uma lista paginada de horas registradas para um funcionário específico, agrupadas por data em ordem crescente.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Horas do funcionário encontradas"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
                   @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                   @ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
               })
    public ResponseEntity<PagedModel<EntityModel<HoraResponse>>> buscarHorasDoFuncionarioPaginado(
            @PathVariable Integer funcionarioId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("data")));
        Page<HoraResponse> pageResponse = funcionarioService.buscarHorasDoFuncionarioPaginado(funcionarioId, pageRequest);
        PagedModel<EntityModel<HoraResponse>> pagedModel = pagedResourcesAssembler.toModel(pageResponse);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{funcionarioId}/horas/totais")
    @Operation(summary = "Busca total de horas do funcionário",
               description = "Retorna o total de horas trabalhadas por um funcionário específico.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Horas totais do funcionário encontradas"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
                   @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                   @ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
               })
    public ResponseEntity<ListaHorasResponse> buscarHorasDoFuncionarioTotais(@PathVariable Integer funcionarioId) {
        ListaHorasResponse response = funcionarioService.buscarHorasDoFuncionarioTotais(funcionarioId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{funcionarioId}/horas")
    @Operation(summary = "Atribui horas a um funcionário",
               description = "Registra um novo período de horas trabalhadas para um funcionário específico.",
               responses = {
                   @ApiResponse(responseCode = "201", description = "Horas registradas com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
                   @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                   @ApiResponse(responseCode = "404", description = "Funcionário não encontrado"),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
               })
    public ResponseEntity<HoraResponse> atribuirHoras(@PathVariable Integer funcionarioId,
            @RequestBody HoraRequest horaRequest) {
        HoraResponse horaResponse = funcionarioService.atribuirHorasDoFuncionario(funcionarioId, horaRequest);
        return ResponseEntity.status(201).body(horaResponse);
    }

    @PutMapping("/{funcionarioId}/horas/{registroHoraId}")
    @Operation(summary = "Atualiza horas de um funcionário",
               description = "Atualiza um registro de horas específico para um funcionário.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Horas atualizadas com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
                   @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                   @ApiResponse(responseCode = "404", description = "Registro de horas ou funcionário não encontrado"),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
               })
    public ResponseEntity<HoraResponse> atualizarHorasDoFuncionario(@PathVariable Integer funcionarioId,
            @PathVariable Integer registroHoraId, @RequestBody HoraRequest horasAtualizadas) {

        try {
            HoraResponse resposta = funcionarioService.atualizarHorasDoFuncionario(funcionarioId, registroHoraId,
                    horasAtualizadas);
            return ResponseEntity.ok(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{funcionarioId}/horas/{registroHoraId}")
    @Operation(summary = "Remove um registro de horas de um funcionário",
               description = "Remove um registro de horas específico de um funcionário.",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Horas removidas com sucesso"),
                   @ApiResponse(responseCode = "400", description = "Parâmetro inválido fornecido"),
                   @ApiResponse(responseCode = "401", description = "Usuário não autorizado"),
                   @ApiResponse(responseCode = "404", description = "Registro de horas ou funcionário não encontrado"),
                   @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
               })
    public ResponseEntity<Void> removerHorasDoFuncionario(@PathVariable Integer funcionarioId,
            @PathVariable Integer registroHoraId) {

        try {
            funcionarioService.removerHorasDoFuncionario(funcionarioId, registroHoraId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
