package com.deyvidsalvatore.web.gestaomasterx.domain.horas;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_horas")
public class RegistroHora implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_registro")
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "id_funcionario", nullable = false)
	private Funcionario funcionario;

	@Column(name = "data", nullable = false)
	private LocalDate data;

	@Column(name = "hora_entrada", nullable = false)
	private LocalTime horaEntrada;

	@Column(name = "hora_saida", nullable = false)
	private LocalTime horaSaida;

	@Column(name = "horas_trabalhadas", nullable = false, precision = 5, scale = 2)
	private BigDecimal horasTrabalhadas;

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

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHoraEntrada() {
		return horaEntrada;
	}

	public void setHoraEntrada(LocalTime horaEntrada) {
		this.horaEntrada = horaEntrada;
	}

	public LocalTime getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(LocalTime horaSaida) {
		this.horaSaida = horaSaida;
	}

	public BigDecimal getHorasTrabalhadas() {
		return horasTrabalhadas;
	}
	
	/**
     * Calcula a quantidade total de horas trabalhadas com base na hora de entrada e saída.
     * @return O total de horas trabalhadas como BigDecimal.
     */
    @PrePersist
    @PreUpdate
	public void calcularHorasTrabalhadas() {
        if (horaEntrada != null && horaSaida != null) {
            Duration duration = Duration.between(horaEntrada, horaSaida);
            BigDecimal hours = BigDecimal.valueOf(duration.toSeconds())
                .divide(BigDecimal.valueOf(3600), RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
            this.horasTrabalhadas = hours;
        } else {
            this.horasTrabalhadas = BigDecimal.ZERO;
        }
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
		RegistroHora other = (RegistroHora) obj;
		return Objects.equals(id, other.id);
	}
    
}