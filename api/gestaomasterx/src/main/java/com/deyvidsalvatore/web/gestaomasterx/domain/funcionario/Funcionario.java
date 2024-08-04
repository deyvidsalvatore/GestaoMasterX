package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.Departamento;
import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.Feedback;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.RegistroHora;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "funcionario")
public class Funcionario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "funcionario_id")
    private Integer id;

    @Column(name = "nome_completo", nullable = false, length = 150)
    private String nomeCompleto;

    @Column(name = "cargo", nullable = false, length = 40)
    private String cargo;

    @Column(name = "email", nullable = false, unique = true, length = 200)
    private String email;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "funcionario")
    private List<RegistroHora> registrosHoras;
    
    @OneToMany(mappedBy = "funcionario")
    private List<Feedback> feedbacksRecebidos = new ArrayList<>();

    @OneToMany(mappedBy = "gestor")
    private List<Feedback> feedbacksDados = new ArrayList<>();
    
    @ManyToMany
    @JoinTable(
        name = "gestor_departamento",
        joinColumns = @JoinColumn(name = "funcionario_id"),
        inverseJoinColumns = @JoinColumn(name = "departamento_id")
    )
    private List<Departamento> departamentos = new ArrayList<>();
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<RegistroHora> getRegistrosHoras() {
		return registrosHoras;
	}

	public void setRegistrosHoras(List<RegistroHora> registrosHoras) {
		this.registrosHoras = registrosHoras;
	}
	
	public List<Feedback> getFeedbacksRecebidos() {
		return feedbacksRecebidos;
	}

	public void setFeedbacksRecebidos(List<Feedback> feedbacksRecebidos) {
		this.feedbacksRecebidos = feedbacksRecebidos;
	}

	public List<Feedback> getFeedbacksDados() {
		return feedbacksDados;
	}

	public void setFeedbacksDados(List<Feedback> feedbacksDados) {
		this.feedbacksDados = feedbacksDados;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	@Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Funcionario other = (Funcionario) obj;
        return Objects.equals(id, other.id);
    }
}
