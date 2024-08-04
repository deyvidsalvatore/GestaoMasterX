package com.deyvidsalvatore.web.gestaomasterx.domain.administracao;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deyvidsalvatore.web.gestaomasterx.config.EmailServiceConfig;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRole;
import com.deyvidsalvatore.web.gestaomasterx.mappers.FuncionarioMapper;
import com.deyvidsalvatore.web.gestaomasterx.rest.AdministracaoController;
import com.deyvidsalvatore.web.gestaomasterx.utils.EmailUtils;
import com.deyvidsalvatore.web.gestaomasterx.utils.FuncionarioUtils;
import com.deyvidsalvatore.web.gestaomasterx.utils.UsuarioUtils;

@Service
@Transactional
public class FuncionarioCRUDService {

    private static final Logger LOG = LoggerFactory.getLogger(FuncionarioCRUDService.class);

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailServiceConfig emailServiceConfig;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PagedResourcesAssembler<FuncionarioResponse> funcionarioAssembler;
    
    public FuncionarioCRUDService(FuncionarioRepository funcionarioRepository, UsuarioRepository usuarioRepository,
			EmailServiceConfig emailServiceConfig, BCryptPasswordEncoder passwordEncoder,
			PagedResourcesAssembler<FuncionarioResponse> funcionarioAssembler) {
		this.funcionarioRepository = funcionarioRepository;
		this.usuarioRepository = usuarioRepository;
		this.emailServiceConfig = emailServiceConfig;
		this.passwordEncoder = passwordEncoder;
		this.funcionarioAssembler = funcionarioAssembler;
	}

    public PagedModel<EntityModel<FuncionarioResponse>> findAllFuncionarios(Pageable pageable) {
    	LOG.info("Administrador ::: Achando {} funcionários da página {}", pageable.getPageSize(), pageable.getPageNumber());
        Page<Funcionario> funcionarioPage = funcionarioRepository.findAll(pageable);

        Page<FuncionarioResponse> funcionarioResponsePage = funcionarioPage.map(funcionario -> {
            FuncionarioResponse response = FuncionarioMapper.entityToResponse(funcionario);
            response.add(Link.of("/api/v1/administrativo/funcionarios/" + funcionario.getId()).withSelfRel());
            return response;
        });

        Link selfLink = Link.of("/api/v1/administrativo/funcionarios/?page=" + pageable.getPageNumber() +
                "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString());
                
        return funcionarioAssembler.toModel(funcionarioResponsePage, selfLink);
    }
    
	public EntityModel<FuncionarioResponse> findFuncionarioById(Integer id) {
		LOG.info("Administrador ::: Achando funcionário pelo id: {}", id);
	    Funcionario funcionario = acharFuncionarioExistente(id);
	    
	    FuncionarioResponse funcionarioResponse = FuncionarioMapper.entityToResponse(funcionario);

	    EntityModel<FuncionarioResponse> funcionarioModel = EntityModel.of(funcionarioResponse);
	    funcionarioModel.add(linkTo(methodOn(AdministracaoController.class)
	            .findFuncionarioById(id)).withSelfRel());
	    
	    return funcionarioModel;
	}

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
    
    public FuncionarioResponse atualizarFuncionario(Integer id, FuncionarioRequest dto) {
        LOG.info("Administrador ::: Atualizando funcionário com ID: {}", id);
        Funcionario funcionarioExistente = acharFuncionarioExistente(id);
        funcionarioExistente.setNomeCompleto(dto.getNomeCompleto());
        funcionarioExistente.setEmail(dto.getEmail());
        funcionarioExistente.setCargo(dto.getCargo());
        Funcionario funcionarioSalvo = funcionarioRepository.save(funcionarioExistente);
        return FuncionarioMapper.entityToResponse(funcionarioSalvo);
    }

    @Transactional
    public void deleteFuncionario(Integer id) {
    	LOG.info("Administrador ::: Apagando um funcionário existente");
        Funcionario funcionarioExistente = acharFuncionarioExistente(id);
        this.funcionarioRepository.delete(funcionarioExistente);
    }

	private Funcionario acharFuncionarioExistente(Integer id) {
		return funcionarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Funcionário não encontrado com ID: " + id));
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
