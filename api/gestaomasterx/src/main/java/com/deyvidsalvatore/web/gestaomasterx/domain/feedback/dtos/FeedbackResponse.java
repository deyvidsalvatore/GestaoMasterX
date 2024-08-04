package com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos;

import java.time.LocalDate;

public class FeedbackResponse {
    private Integer id;
    private Integer gestorId;
    private Integer funcionarioId;
    private LocalDate data;
    private String comentario;
    private String meta;

    public FeedbackResponse(Integer id, Integer gestorId, Integer funcionarioId, LocalDate data, String comentario, String meta) {
        this.id = id;
        this.gestorId = gestorId;
        this.funcionarioId = funcionarioId;
        this.data = data;
        this.comentario = comentario;
        this.meta = meta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGestorId() {
        return gestorId;
    }

    public void setGestorId(Integer gestorId) {
        this.gestorId = gestorId;
    }

    public Integer getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Integer funcionarioId) {
        this.funcionarioId = funcionarioId;
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
}
