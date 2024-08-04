package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FuncionarioRequest {

	@NotNull(message = "O nome completo não pode ser nulo.")
	@NotBlank(message = "O nome completo não pode estar em branco.")
	@Size(max = 150, message = "O nome completo deve ter no máximo 150 caracteres.")
	private String nomeCompleto;

	@NotNull(message = "O cargo não pode ser nulo.")
	@NotBlank(message = "O cargo não pode estar em branco.")
	@Size(max = 40, message = "O cargo deve ter no máximo 40 caracteres.")
	private String cargo;

	@NotNull(message = "O email não pode ser nulo.")
	@NotBlank(message = "O email não pode estar em branco.")
	@Size(max = 200, message = "O email deve ter no máximo 200 caracteres.")
	private String email;

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
