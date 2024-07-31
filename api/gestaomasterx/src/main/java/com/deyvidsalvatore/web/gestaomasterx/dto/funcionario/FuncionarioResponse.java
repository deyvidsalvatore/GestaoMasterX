package com.deyvidsalvatore.web.gestaomasterx.dto.funcionario;

public class FuncionarioResponse {

    private Integer id;
    private String nomeCompleto;
    private String cargo;
    private String email;

    public FuncionarioResponse() {}

    public FuncionarioResponse(Integer id, String nomeCompleto, String cargo, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cargo = cargo;
        this.email = email;
    }

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
}
