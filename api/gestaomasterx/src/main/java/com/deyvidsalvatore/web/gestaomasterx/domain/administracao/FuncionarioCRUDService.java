package com.deyvidsalvatore.web.gestaomasterx.domain.administracao;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public FuncionarioCRUDService(FuncionarioRepository funcionarioRepository, UsuarioRepository usuarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.usuarioRepository = usuarioRepository;
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
            usuario.setUsername(dominio);
            usuario.setPassword(dominio + "@p4ssw0rd");
            usuario.setRole(UsuarioRole.FUNCIONARIO);
            usuario.setFuncionario(funcionario);

            this.usuarioRepository.save(usuario);
            LOG.info("Nova conta de usuário criada com username: {}", dominio);
        }

        funcionario.setUsuario(usuarioRepository.findByUsername(dominio));
        this.funcionarioRepository.save(funcionario);
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
