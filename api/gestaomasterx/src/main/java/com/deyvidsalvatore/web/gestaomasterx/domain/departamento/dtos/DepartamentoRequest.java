package com.deyvidsalvatore.web.gestaomasterx.domain.departamento.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartamentoRequest {

    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 50, message = "Nome não pode ter mais de 50 caracteres")
    private String nome;

    @NotBlank(message = "Descrição não pode estar vazia")
    @Size(max = 400, message = "Descrição não pode ter mais de 400 caracteres")
    private String descricao;

    @NotBlank(message = "Recursos não podem estar vazios")
    @Size(max = 500, message = "Recursos não podem ter mais de 500 caracteres")
    private String recursos;

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
}
