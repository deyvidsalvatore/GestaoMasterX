package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos;

import java.io.Serial;
import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(value = {"id", "nomeCompleto", "cargo", "email"})
public class FuncionarioResponse extends RepresentationModel<FuncionarioResponse> implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
    private Integer key;
    private String nomeCompleto;
    private String cargo;
    private String email;

    public FuncionarioResponse() {}

    public FuncionarioResponse(Integer id, String nomeCompleto, String cargo, String email) {
        this.key = id;
        this.nomeCompleto = nomeCompleto;
        this.cargo = cargo;
        this.email = email;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer id) {
        this.key = id;
    }

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
