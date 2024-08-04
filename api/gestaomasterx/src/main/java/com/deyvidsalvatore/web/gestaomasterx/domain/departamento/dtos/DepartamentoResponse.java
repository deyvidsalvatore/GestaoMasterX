package com.deyvidsalvatore.web.gestaomasterx.domain.departamento.dtos;

import java.util.ArrayList;
import java.util.List;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioResponse;

public class DepartamentoResponse {

	private Integer id;
	private String nome;
	private String descricao;
	private String recursos;
	private List<FuncionarioResponse> gestores = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getRecursos() {
		return recursos;
	}

	public void setRecursos(String recursos) {
		this.recursos = recursos;
	}

	public List<FuncionarioResponse> getGestores() {
		return gestores;
	}

	public void setGestores(List<FuncionarioResponse> gestores) {
		this.gestores = gestores;
	}

}
