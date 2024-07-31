package com.deyvidsalvatore.web.gestaomasterx.mappers;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;
import com.deyvidsalvatore.web.gestaomasterx.dto.funcionario.FuncionarioResponse;
import com.deyvidsalvatore.web.gestaomasterx.dto.funcionario.NewFuncionarioRequest;

public class FuncionarioMapper {
	
	public static Funcionario newRequestToEntity(NewFuncionarioRequest request) {
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
