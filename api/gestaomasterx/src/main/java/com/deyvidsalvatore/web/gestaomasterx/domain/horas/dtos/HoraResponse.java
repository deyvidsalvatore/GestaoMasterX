package com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class HoraResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer funcionarioId;
    private String data;
    private String horaEntrada;
    private String horaSaida;
    private BigDecimal horasTrabalhadas;

    public HoraResponse(Integer id, Integer funcionarioId, String data, String horaEntrada, String horaSaida, BigDecimal horasTrabalhadas) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.data = data;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public BigDecimal getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(BigDecimal horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    @Override
    public String toString() {
        return "HoraResponse{" +
                "id=" + id +
                ", funcionarioId=" + funcionarioId +
                ", data='" + data + '\'' +
                ", horaEntrada='" + horaEntrada + '\'' +
                ", horaSaida='" + horaSaida + '\'' +
                ", horasTrabalhadas=" + horasTrabalhadas +
                '}';
    }
}
