package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.Feedback;
import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.FeedbackRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos.FeedbackRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.feedback.dtos.FeedbackResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.exceptions.FuncionarioNotFoundException;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.RegistroHora;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.RegistroHoraRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.ListaHorasResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRole;
import com.deyvidsalvatore.web.gestaomasterx.rest.FuncionarioController;
import com.deyvidsalvatore.web.gestaomasterx.rest.GestorController;
import com.deyvidsalvatore.web.gestaomasterx.utils.DataUtils;

@Service
@Transactional
public class FuncionarioService {

	private static final Logger LOG = LoggerFactory.getLogger(FuncionarioService.class);

	private final FuncionarioRepository funcionarioRepository;
	private final RegistroHoraRepository horasRepository;
	private final FeedbackRepository feedbackRepository;

	public FuncionarioService(
			FuncionarioRepository funcionarioRepository, 
			RegistroHoraRepository horasRepository,
			FeedbackRepository feedbackRepository
	) {
		this.funcionarioRepository = funcionarioRepository;
		this.horasRepository = horasRepository;
		this.feedbackRepository = feedbackRepository;
	}

	/** Horas do funcionário **/
	public Page<HoraResponse> buscarHorasDoFuncionarioPaginado(Integer funcionarioId, Pageable pageable) {
        Funcionario funcionario = findFuncionarioById(funcionarioId);
        Page<RegistroHora> registrosPage = horasRepository.findByFuncionario(funcionario, pageable);
        return registrosPage.map(registroHora -> horasToResponse(funcionario, registroHora));
    }
	
	public HoraResponse atribuirHorasDoFuncionario(Integer funcionarioId, HoraRequest horas) {
		Funcionario funcionario = findFuncionarioById(funcionarioId);
		RegistroHora registroHora = new RegistroHora();
		registroHora.setData(DataUtils.parseDate(horas.getData()));
		registroHora.setHoraEntrada(DataUtils.parseTime(horas.getHoraEntrada()));
		registroHora.setHoraSaida(DataUtils.parseTime(horas.getHoraSaida()));
		registroHora.setFuncionario(funcionario);
		this.horasRepository.save(registroHora);
		LOG.info("Funcionario ::: Atribuindo horas ao funcionario de id {}", funcionarioId);
		return horasToResponse(funcionario, registroHora);
	}

	public ListaHorasResponse buscarHorasDoFuncionarioTotais(Integer funcionarioId) {
		LOG.info("Funcionario ::: Buscando horas do funcionario de id {}", funcionarioId);
		Funcionario funcionario = findFuncionarioById(funcionarioId);
		List<RegistroHora> registros = horasRepository.findByFuncionario(funcionario);

		BigDecimal totalHoras = registros.stream().map(RegistroHora::getHorasTrabalhadas).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new ListaHorasResponse(registros.size(), totalHoras, funcionarioId);
	}

	public HoraResponse atualizarHorasDoFuncionario(Integer funcionarioId, Integer registroHoraId, HoraRequest horasAtualizadas) {
	    Funcionario funcionario = findFuncionarioById(funcionarioId);
	    RegistroHora registroHora = findHorasById(registroHoraId);

	    registroHora.setData(DataUtils.parseDate(horasAtualizadas.getData()));
	    registroHora.setHoraEntrada(DataUtils.parseTime(horasAtualizadas.getHoraEntrada()));
	    registroHora.setHoraSaida(DataUtils.parseTime(horasAtualizadas.getHoraSaida()));

	    horasRepository.save(registroHora);
	    LOG.info("Funcionario ::: Atualizando horas do funcionario de id {}", funcionarioId);

	    return horasToResponse(funcionario, registroHora);
	}
	
	public void removerHorasDoFuncionario(Integer funcionarioId, Integer registroHoraId) {
	    findFuncionarioById(funcionarioId);
	    RegistroHora registroHora = findHorasById(registroHoraId);
	    this.horasRepository.delete(registroHora);
	    LOG.info("Funcionario ::: Removendo horas do funcionario de id {}", funcionarioId);
	}
	/** Fim horas do funcionário **/
	
	/** Feedback [Funcionários] **/
	public PagedModel<EntityModel<FeedbackResponse>> listarFeedbacksPaginadosDoFuncionario(Integer funcionarioId, Pageable pageable) {
        Funcionario funcionario = findFuncionarioById(funcionarioId);
        Page<Feedback> feedbacks = feedbackRepository.findByFuncionario(funcionario, pageable);
        
        List<EntityModel<FeedbackResponse>> feedbackResponses = feedbacks.stream()
            .map(feedback -> {
                FeedbackResponse response = new FeedbackResponse(
                    feedback.getId(),
                    feedback.getGestor().getId(),
                    feedback.getFuncionario().getId(),
                    feedback.getData(),
                    feedback.getComentario(),
                    feedback.getMeta()
                );

                // Link para o feedback específico
                Link selfLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(FuncionarioController.class)
                    .obterFeedbackDoFuncionarioPorId(funcionarioId, feedback.getId()))
                    .withSelfRel();

                return EntityModel.of(response, selfLink);
            })
            .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(FuncionarioController.class)
            .listarFeedbacksPaginados(funcionarioId, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString()))
            .withSelfRel();

        return PagedModel.of(
            feedbackResponses, 
            new PagedModel.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), feedbacks.getTotalElements()), 
            selfLink
        );
    }
	
    public EntityModel<FeedbackResponse> getFeedbackById(Integer funcionarioId, Integer feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
            .orElseThrow(() -> new IllegalArgumentException("Feedback not found"));

        FeedbackResponse feedbackResponse = new FeedbackResponse(
            feedback.getId(),
            feedback.getGestor().getId(),
            feedback.getFuncionario().getId(),
            feedback.getData(),
            feedback.getComentario(),
            feedback.getMeta()
        );

        EntityModel<FeedbackResponse> feedbackEntityModel = EntityModel.of(feedbackResponse);

        Link selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(FuncionarioController.class)
            .obterFeedbackDoFuncionarioPorId(funcionarioId, feedbackId))
            .withSelfRel();

        feedbackEntityModel.add(selfLink.withSelfRel());

        return feedbackEntityModel;
    }
	
	/** Feedback [Gestores] **/
    public PagedModel<EntityModel<FeedbackResponse>> lerFeedbacksAtribuidos(Integer gestorId, Pageable pageable) {
        Funcionario gestor = findFuncionarioById(gestorId);

        if (!UsuarioRole.GESTOR.equals(gestor.getUsuario().getRole())) {
            throw new IllegalArgumentException("Apenas gestores podem ler feedbacks atribuídos");
        }

        Page<Feedback> feedbacks = feedbackRepository.findByGestor(gestor, pageable);

        List<EntityModel<FeedbackResponse>> feedbackResponses = feedbacks.stream()
            .map(feedback -> {
                FeedbackResponse response = new FeedbackResponse(
                    feedback.getId(),
                    feedback.getGestor().getId(),
                    feedback.getFuncionario().getId(),
                    feedback.getData(),
                    feedback.getComentario(),
                    feedback.getMeta()
                );

                Link selfLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(GestorController.class)
                    .getFeedbackById(gestorId, feedback.getId()))
                    .withSelfRel();

                return EntityModel.of(response, selfLink);
            })
            .collect(Collectors.toList());

        Link selfLink = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(GestorController.class)
            .listarFeedbacksAtribuidos(gestorId, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString()))
            .withSelfRel();

        PagedModel<EntityModel<FeedbackResponse>> pagedModel = PagedModel.of(
            feedbackResponses, 
            new PagedModel.PageMetadata(pageable.getPageSize(), pageable.getPageNumber(), feedbacks.getTotalElements()),
            selfLink);

        return pagedModel;
    }
    
    public FeedbackResponse obterFeedbackDoGestorPorId(Integer gestorId, Integer feedbackId) {
        Feedback feedback = feedbackPorId(feedbackId);

        if (!gestorId.equals(feedback.getGestor().getId())) {
            throw new IllegalArgumentException("Apenas o gestor associado pode acessar esse feedback");
        }

        return new FeedbackResponse(
            feedback.getId(),
            feedback.getGestor().getId(),
            feedback.getFuncionario().getId(),
            feedback.getData(),
            feedback.getComentario(),
            feedback.getMeta()
        );
    }

	public Feedback feedbackPorId(Integer feedbackId) {
		return feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback não encontrado"));
	}
    
	public String atribuirUmNovoFeedback(Integer funcionarioId, Integer gestorId, FeedbackRequest feedbackRequest) {
		Funcionario gestor = findFuncionarioById(gestorId);
		Funcionario funcionario = findFuncionarioById(funcionarioId);
		validateRoles(gestor, funcionario);
		this.feedbackRepository.save(createFeedback(feedbackRequest, funcionario, gestor));
		LOG.info("Gestor ::: Atribuindo feedback ao funcionário de id {}", funcionarioId);
		return "Feedback foi atribuido ao funcionário com sucesso!";
	}
	
    public void atualizarFeedback(Integer gestorId, Integer feedbackId, FeedbackRequest feedbackRequest) {
    	Funcionario gestor = findFuncionarioById(gestorId);
    	
    	if (!UsuarioRole.GESTOR.equals(gestor.getUsuario().getRole())) {
	        throw new IllegalArgumentException("Apenas gestores podem atribuir feedback a um funcionário");
	    }
    	
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback com ID " + feedbackId + " não encontrado"));

    	if (!gestorId.equals(feedback.getGestor().getId())) {
    		throw new IllegalArgumentException("Apenas o gestor associado pode atualizar esse feedback");
    	}
    	
        feedback.setComentario(feedbackRequest.getComentario());
        feedback.setMeta(feedbackRequest.getMeta());
        feedback.setData(LocalDate.now());

        feedbackRepository.save(feedback);
    }
	
    public void apagarFeedback(Integer gestorId, Integer feedbackId) {
        Funcionario gestor = findFuncionarioById(gestorId);
        
        if (!UsuarioRole.GESTOR.equals(gestor.getUsuario().getRole())) {
            throw new IllegalArgumentException("Apenas gestores podem apagar feedback");
        }

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new IllegalArgumentException("Feedback com ID " + feedbackId + " não encontrado"));

        if (!gestorId.equals(feedback.getGestor().getId())) {
            throw new IllegalArgumentException("Apenas o gestor associado pode apagar esse feedback");
        }

        feedbackRepository.delete(feedback);
        LOG.info("Gestor ::: Removendo feedback do funcionário de id {}", feedback.getFuncionario().getId());
    }
    
	private Feedback createFeedback(FeedbackRequest feedbackRequest, Funcionario funcionario, Funcionario gestor) {
	    Feedback feedback = new Feedback();
	    feedback.setComentario(feedbackRequest.getComentario());
	    feedback.setMeta(feedbackRequest.getMeta());
	    feedback.setData(LocalDate.now());
	    feedback.setFuncionario(funcionario);
	    feedback.setGestor(gestor);
	    return feedback;
	}
	
	private void validateRoles(Funcionario gestor, Funcionario funcionario) {
	    if (!UsuarioRole.GESTOR.equals(gestor.getUsuario().getRole())) {
	        throw new IllegalArgumentException("Apenas gestores podem atribuir feedback a um funcionário");
	    }
	    
	    if (!UsuarioRole.FUNCIONARIO.equals(funcionario.getUsuario().getRole())) {
	        throw new IllegalArgumentException("O gestor pode atribuir feedback apenas para funcionários");
	    }
	}

	private RegistroHora findHorasById(Integer registroHoraId) {
		return horasRepository.findById(registroHoraId)
	        .orElseThrow(() -> new IllegalArgumentException("Registro de horas não encontrado"));
	}


	
	private HoraResponse horasToResponse(Funcionario funcionario, RegistroHora registroHora) {
		return new HoraResponse(registroHora.getId(), funcionario.getId(), DataUtils.formatDate(registroHora.getData()),
				DataUtils.formatTime(registroHora.getHoraEntrada()), DataUtils.formatTime(registroHora.getHoraSaida()),
				registroHora.getHorasTrabalhadas());
	}

	private Funcionario findFuncionarioById(Integer id) {
		return this.funcionarioRepository.findById(id).orElseThrow(FuncionarioNotFoundException::new);
	}

}
