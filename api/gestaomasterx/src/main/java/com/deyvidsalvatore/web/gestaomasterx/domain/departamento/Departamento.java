package com.deyvidsalvatore.web.gestaomasterx.domain.departamento;

import java.util.List;
import java.util.Objects;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity @Table(name = "departamento")
public class Departamento implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "departamento_id")
	private Integer id;
	
	@Column(name = "nome", length = 50, nullable = false)
	private String nome;
	
	@Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
	private String descricao;

	@Column(name = "recursos", nullable = false, columnDefinition = "TEXT")
	private String recursos;

	/* Limitei apenas a 2 gestores */
	@ManyToMany(mappedBy = "departamentos")
    private List<Funcionario> gestores;
	
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

	public List<Funcionario> getGestores() {
		return gestores;
	}

	public void setGestores(List<Funcionario> gestores) {
		this.gestores = gestores;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Departamento other = (Departamento) obj;
		return Objects.equals(id, other.id);
	}

}
