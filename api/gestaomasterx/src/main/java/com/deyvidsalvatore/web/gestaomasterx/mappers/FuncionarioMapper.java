package com.deyvidsalvatore.web.gestaomasterx.mappers;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioResponse;

public class FuncionarioMapper {
	
	public static Funcionario newRequestToEntity(FuncionarioRequest request) {
		Funcionario funcionario = new Funcionario();
		funcionario.setNomeCompleto(request.getNomeCompleto());
		funcionario.setCargo(request.getCargo());
		funcionario.setEmail(request.getEmail());
		return funcionario;
	}
	
	public static FuncionarioResponse entityToResponse(Funcionario funcionario) {
		return new FuncionarioResponse(funcionario.getId(), funcionario.getNomeCompleto(), funcionario.getCargo(), funcionario.getEmail());
	}

}
