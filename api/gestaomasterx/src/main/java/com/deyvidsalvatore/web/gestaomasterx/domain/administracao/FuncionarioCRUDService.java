package com.deyvidsalvatore.web.gestaomasterx.domain.administracao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deyvidsalvatore.web.gestaomasterx.config.EmailServiceConfig;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRole;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.exceptions.UsuarioNaoEncontradoException;
import com.deyvidsalvatore.web.gestaomasterx.utils.FuncionarioUtils;
import com.deyvidsalvatore.web.gestaomasterx.utils.UsuarioUtils;
import com.deyvidsalvatore.web.gestaomasterx.utils.EmailUtils;

@Service
public class FuncionarioCRUDService {

    private static final Logger LOG = LoggerFactory.getLogger(FuncionarioCRUDService.class);

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailServiceConfig emailServiceConfig;
    private final BCryptPasswordEncoder passwordEncoder;

    public FuncionarioCRUDService(
            FuncionarioRepository funcionarioRepository, 
            UsuarioRepository usuarioRepository,
            EmailServiceConfig emailServiceConfig,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailServiceConfig = emailServiceConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void criarContaParaFuncionario(Funcionario funcionario) {
        funcionario.setId(FuncionarioUtils.generateFuncionarioId());

        String email = funcionario.getEmail();
        String partePrincipal = extrairPartePrincipalEmail(email);

        Usuario usuario = usuarioRepository.findByUsername(partePrincipal)
                .orElseGet(() -> {
                    Usuario novoUsuario = criarNovoUsuario(partePrincipal);
                    novoUsuario.setPassword(passwordEncoder.encode(novoUsuario.getUsername() + "@p4ssw0rd"));
                    usuarioRepository.save(novoUsuario);
                    enviarCredenciaisAsync(funcionario.getEmail(), funcionario.getNomeCompleto(), novoUsuario.getUsername(), novoUsuario.getUsername() + "@p4ssw0rd");
                    return novoUsuario;
                });

        funcionario.setUsuario(usuario);
        this.funcionarioRepository.save(funcionario);
        LOG.info("Administrador ::: Nova conta de usuário criada com username: {}", partePrincipal);
    }

    private Usuario criarNovoUsuario(String username) {
        Usuario usuario = new Usuario();
        usuario.setId(UsuarioUtils.generateUsuarioId());
        usuario.setUsername(username);
        usuario.setRole(UsuarioRole.FUNCIONARIO);
        return usuario;
    }

    @Async
    public void enviarCredenciaisAsync(String to, String name, String username, String password) {
        String subject = "Bem-vindo à Gestão MasterX - Suas Credenciais";
        String htmlContent = EmailUtils.loadAndProcessTemplate(name, username, password);
        emailServiceConfig.sendEmail(to, subject, htmlContent);
    }

    private String extrairPartePrincipalEmail(String email) {
        int index = email.indexOf('@');
        if (index > 0) {
            return email.substring(0, index);
        }
        throw new IllegalArgumentException("Email inválido: " + email);
    }
}
