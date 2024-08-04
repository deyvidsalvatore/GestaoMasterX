package com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackRequest {
	
	@NotBlank(message = "Comentário não pode estar em branco")
    @Size(max = 500, message = "Comentário não pode ter mais de 500 caracteres")
	private String comentario;
	
	@Size(max = 200, message = "Meta não pode ter mais de 100 caracteres")
    private String meta;

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
