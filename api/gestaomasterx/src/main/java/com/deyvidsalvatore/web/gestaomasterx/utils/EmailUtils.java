package com.deyvidsalvatore.web.gestaomasterx.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

public class EmailUtils {

    public static String loadAndProcessTemplate(String name, String username, String password) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/email-criacao.html");
            String templateContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            return templateContent
            		.replace("{{nome}}", name)
                    .replace("{{username}}", username)
                    .replace("{{password}}", password);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar o template de e-mail", e);
        }
    }
}
