package com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos;

import java.io.Serial;
import java.io.Serializable;

import jakarta.validation.constraints.NotNull;

public class HoraRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "A data não pode ser nula.")
    private String data;

    @NotNull(message = "A hora de entrada não pode ser nula.")
    private String horaEntrada;

    @NotNull(message = "A hora de saída não pode ser nula.")
    private String horaSaida;

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

    @Override
    public String toString() {
        return "HoraRequest{" +
                "data='" + data + '\'' +
                ", horaEntrada='" + horaEntrada + '\'' +
                ", horaSaida='" + horaSaida + '\'' +
                '}';
    }
}
