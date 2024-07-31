package com.deyvidsalvatore.web.gestaomasterx.domain.administracao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.deyvidsalvatore.web.gestaomasterx.config.EmailServiceConfig;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRole;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.exceptions.UsuarioNaoEncontradoException;

@Service
public class FuncionarioCRUDService {

    private static final Logger LOG = LoggerFactory.getLogger(FuncionarioCRUDService.class);

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailServiceConfig emailServiceConfig;
    
    public FuncionarioCRUDService(
    		FuncionarioRepository funcionarioRepository, 
    		UsuarioRepository usuarioRepository,
    		EmailServiceConfig emailServiceConfig
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
		this.emailServiceConfig = emailServiceConfig;
    }

    @Transactional
    public void criarContaParaFuncionario(Funcionario funcionario) {
        String email = funcionario.getEmail();
        String dominio = extrairDominioEmail(email);

        try {
            verificarSeJaUsuarioExiste(dominio);
            LOG.info("Conta de usuário já existe para o domínio: {}", dominio);
        } catch (UsuarioNaoEncontradoException e) {
            Usuario usuario = new Usuario();
            usuario.setUsername(generateFuncionarioUsername(funcionario, dominio));
            usuario.setPassword(dominio + "@p4ssw0rd");
            usuario.setRole(UsuarioRole.FUNCIONARIO);
            usuario.setFuncionario(funcionario);

            this.usuarioRepository.save(usuario);
            LOG.info("Nova conta de usuário criada com username: {}", dominio);
        }

        funcionario.setUsuario(usuarioRepository.findByUsername(dominio));
        this.funcionarioRepository.save(funcionario);
    }

    public void enviarCredenciaisFuncionario(String to, String username, String password) {
    	String subject = "Bem-vindo à Gestão MasterX - Suas Credenciais";
        String htmlContent = loadAndProcessTemplate(username, password);
        this.emailServiceConfig.sendEmail(to, subject, htmlContent);
    }
    
	private String loadAndProcessTemplate(String username, String password) {
		try {
			
            ClassPathResource resource = new ClassPathResource("templates/email-template.html");
            String templateContent = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

            return templateContent
                    .replace("{{username}}", username)
                    .replace("{{password}}", password);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível carregar o template de e-mail", e);
        }
	}

	private String generateFuncionarioUsername(Funcionario funcionario, String dominio) {
		return String.valueOf(dominio + ".func" + funcionario.getId()).toUpperCase();
	}

    private String extrairDominioEmail(String email) {
        int index = email.indexOf('@');
        if (index > 0 && index < email.length() - 1) {
            return email.substring(index + 1);
        }
        throw new IllegalArgumentException("Email inválido: " + email);
    }

    private void verificarSeJaUsuarioExiste(String username) {
        Optional<Usuario> usuario = Optional.ofNullable(this.usuarioRepository.findByUsername(username));
        if (usuario.isEmpty()) {
            throw new UsuarioNaoEncontradoException(username);
        }
    }
}
