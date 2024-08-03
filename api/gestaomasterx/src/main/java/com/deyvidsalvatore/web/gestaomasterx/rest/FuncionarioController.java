package com.deyvidsalvatore.web.gestaomasterx.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioService;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.ListaHorasResponse;

@RestController
@RequestMapping("/api/v1/funcionarios")
public class FuncionarioController {

	private final FuncionarioService funcionarioService;

	public FuncionarioController(FuncionarioService funcionarioService) {
		this.funcionarioService = funcionarioService;
	}

	@GetMapping("/{funcionarioId}/horas")
	public ResponseEntity<ListaHorasResponse> buscarHorasDoFuncionario(@PathVariable Integer funcionarioId) {
		ListaHorasResponse response = funcionarioService.buscarHorasDoFuncionario(funcionarioId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{funcionarioId}/horas")
	public ResponseEntity<HoraResponse> atribuirHoras(@PathVariable Integer funcionarioId,
			@RequestBody HoraRequest horaRequest) {
		HoraResponse horaResponse = funcionarioService.atribuirHorasDoFuncionario(funcionarioId, horaRequest);
		return ResponseEntity.status(201).body(horaResponse);
	}
}
