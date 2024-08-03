package com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos;

import java.math.BigDecimal;

public class ListaHorasResponse {
	
	private Integer quantidadeArmazenada;
	private BigDecimal totalDeHoras;
	private Integer funcionarioId;
	
	public ListaHorasResponse() {};
	
	public ListaHorasResponse(Integer quantidadeArmazenada, BigDecimal totalDeHoras, Integer funcionarioId) {
		this.quantidadeArmazenada = quantidadeArmazenada;
		this.totalDeHoras = totalDeHoras;
		this.funcionarioId = funcionarioId;
	}
	public Integer getQuantidadeArmazenada() {
		return quantidadeArmazenada;
	}
	public void setQuantidadeArmazenada(Integer quantidadeArmazenada) {
		this.quantidadeArmazenada = quantidadeArmazenada;
	}
	public BigDecimal getTotalDeHoras() {
		return totalDeHoras;
	}
	public void setTotalDeHoras(BigDecimal totalDeHoras) {
		this.totalDeHoras = totalDeHoras;
	}
	public Integer getFuncionarioId() {
		return funcionarioId;
	}
	public void setFuncionarioId(Integer funcionarioId) {
		this.funcionarioId = funcionarioId;
	}
	
	
}
