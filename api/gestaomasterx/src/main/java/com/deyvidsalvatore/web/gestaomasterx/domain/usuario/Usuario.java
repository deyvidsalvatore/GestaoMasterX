package com.deyvidsalvatore.web.gestaomasterx.domain.usuario;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "usuario_id")
	private Integer id;
	
	@Column(name = "username", nullable = false, unique = true, length = 32)
	private String username;
	
	@Column(name = "senha", nullable = false, length = 64)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private UsuarioRole role;
	
    @OneToOne(mappedBy = "usuario")
    private Funcionario funcionario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UsuarioRole getRole() {
		return role;
	}

	public void setRole(UsuarioRole role) {
		this.role = role;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
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
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}
}
