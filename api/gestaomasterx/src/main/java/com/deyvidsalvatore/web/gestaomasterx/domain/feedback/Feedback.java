package com.deyvidsalvatore.web.gestaomasterx.domain.feedback;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity @Table(name = "feedback")
public class Feedback implements Serializable {
	
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id_feedback")
    private Integer id;
    
	@ManyToOne
	@JoinColumn(name = "id_funcionario", nullable = false)
    private Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(name = "id_gestor", nullable = false)
	private Funcionario gestor;
	
	@Column(name = "data", nullable = false)
	private LocalDate data;
	
	@Column(name = "comentario", nullable = false, columnDefinition = "TEXT")
	private String comentario;
	
	@Column(name = "meta", nullable = false, columnDefinition = "TEXT")
	private String meta;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcionario getGestor() {
		return gestor;
	}

	public void setGestor(Funcionario gestor) {
		this.gestor = gestor;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
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
		Feedback other = (Feedback) obj;
		return Objects.equals(id, other.id);
	}
	
}
