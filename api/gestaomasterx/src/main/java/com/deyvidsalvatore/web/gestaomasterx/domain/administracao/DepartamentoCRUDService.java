package com.deyvidsalvatore.web.gestaomasterx.domain.administracao;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.Departamento;
import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.DepartamentoRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.dtos.DepartamentoRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.departamento.dtos.DepartamentoResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.FuncionarioRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.dtos.FuncionarioResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRole;
import com.deyvidsalvatore.web.gestaomasterx.rest.AdministracaoController;
@Service
@Transactional
public class DepartamentoCRUDService {

    private static final Logger logger = LoggerFactory.getLogger(DepartamentoCRUDService.class);

    private final DepartamentoRepository departamentoRepository;
    private final FuncionarioRepository funcionarioRepository;

    public DepartamentoCRUDService(DepartamentoRepository departamentoRepository,
                                   FuncionarioRepository funcionarioRepository) {
        this.departamentoRepository = departamentoRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    public PagedModel<EntityModel<DepartamentoResponse>> listAllDepartamentos(int page, int size, String sort) {
        logger.info("Administrador ::: listAllDepartamentos ~> Buscando todos os departamentos com paginação");

        Sort sorting = sort.equalsIgnoreCase("asc") ? Sort.by("nome").ascending() : Sort.by("nome").descending();
        Pageable pageable = PageRequest.of(page, size, sorting);

        Page<Departamento> departamentosPage = this.departamentoRepository.findAll(pageable);
        logger.info("Administrador ::: listAllDepartamentos ~> {} departamentos encontrados na página {} com tamanho {}", 
                    departamentosPage.getTotalElements(), page, size);

        PagedModel<EntityModel<DepartamentoResponse>> pagedModel = PagedModel.of(
            departamentosPage.getContent().stream()
                .map(this::convertToEntityModel)
                .collect(Collectors.toList()),
            new PagedModel.PageMetadata(size, page, departamentosPage.getTotalElements())
        );

        logger.info("Administrador ::: listAllDepartamentos ~> Respostas dos departamentos preparadas com {} departamentos", pagedModel.getContent().size());

        return pagedModel;
    }

    public DepartamentoResponse getDepartamentoById(Integer id) {
        logger.info("Administrador ::: getDepartamentoById ~> Buscando departamento com ID: {}", id);
        Departamento departamento = this.departamentoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Administrador ::: getDepartamentoById ~> Departamento com ID: {} não encontrado", id);
                    return new IllegalArgumentException("Departamento não encontrado!");
                });

        logger.info("Administrador ::: getDepartamentoById ~> Departamento encontrado: {}", departamento);

        DepartamentoResponse response = entityDepartamentoToResponse(departamento);

        response.setGestores(departamento.getGestores().stream()
                .map(this::convertToFuncionarioResponse)
                .collect(Collectors.toList()));

        logger.info("Administrador ::: getDepartamentoById ~> Resposta do departamento preparada com {} gestores", response.getGestores().size());

        return response;
    }

    public DepartamentoResponse saveDepartamento(DepartamentoRequest request) {
        logger.info("Administrador ::: saveDepartamento ~> Salvando novo departamento: {}", request);
        
        Departamento departamento = new Departamento();
        departamento.setNome(request.getNome());
        departamento.setDescricao(request.getDescricao());
        departamento.setRecursos(request.getRecursos());
        
        Departamento savedDepartamento = this.departamentoRepository.save(departamento);
        logger.info("Administrador ::: saveDepartamento ~> Departamento salvo com ID: {}", savedDepartamento.getId());

        return entityDepartamentoToResponse(savedDepartamento);
    }

    public DepartamentoResponse updateDepartamento(Integer id, DepartamentoRequest request) {
        logger.info("Administrador ::: updateDepartamento ~> Atualizando departamento com ID: {}", id);
        Departamento departamento = this.departamentoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Administrador ::: updateDepartamento ~> Departamento com ID: {} não encontrado", id);
                    return new IllegalArgumentException("Departamento não encontrado!");
                });

        departamento.setNome(request.getNome());
        departamento.setDescricao(request.getDescricao());
        departamento.setRecursos(request.getRecursos());

        Departamento updatedDepartamento = this.departamentoRepository.save(departamento);
        logger.info("Administrador ::: updateDepartamento ~> Departamento atualizado com ID: {}", updatedDepartamento.getId());

        return entityDepartamentoToResponse(updatedDepartamento);
    }

    public void deleteDepartamento(Integer id) {
        logger.info("Administrador ::: deleteDepartamento ~> Apagando departamento com ID: {}", id);
        Departamento departamento = this.departamentoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Administrador ::: deleteDepartamento ~> Departamento com ID: {} não encontrado", id);
                    return new IllegalArgumentException("Departamento não encontrado!");
                });

        if (!departamento.getGestores().isEmpty()) {
            logger.error("Administrador ::: deleteDepartamento ~> Departamento com ID: {} possui gestores associados. Não é possível apagar", id);
            throw new IllegalStateException("Não é possível apagar um departamento que possui gestores associados.");
        }

        this.departamentoRepository.delete(departamento);
        logger.info("Administrador ::: deleteDepartamento ~> Departamento com ID: {} removido com sucesso", id);
    }

    public DepartamentoResponse addGestorToDepartamento(Integer departamentoId, Integer funcionarioId) {
        logger.info("Administrador ::: addGestorToDepartamento ~> Associando gestor com ID: {} ao departamento com ID: {}", funcionarioId, departamentoId);

        Departamento departamento = this.departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> {
                    logger.error("Administrador ::: addGestorToDepartamento ~> Departamento com ID: {} não encontrado", departamentoId);
                    return new IllegalArgumentException("Departamento não encontrado!");
                });

        Funcionario funcionario = this.funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> {
                    logger.error("Administrador ::: addGestorToDepartamento ~> Funcionário com ID: {} não encontrado", funcionarioId);
                    return new IllegalArgumentException("Funcionário não encontrado!");
                });

        if (!funcionario.getUsuario().getRole().equals(UsuarioRole.GESTOR)) {
            logger.error("Administrador ::: addGestorToDepartamento ~> Funcionário com ID: {} não é um gestor", funcionarioId);
            throw new IllegalArgumentException("O funcionário não é um gestor.");
        }

        if (departamento.getGestores().contains(funcionario)) {
            logger.warn("Administrador ::: addGestorToDepartamento ~> Funcionário com ID: {} já está associado ao departamento com ID: {}", funcionarioId, departamentoId);
            throw new IllegalArgumentException("O funcionário já está associado a este departamento.");
        }

        if (departamento.getGestores().size() >= 2) {
            logger.error("Administrador ::: addGestorToDepartamento ~> Departamento com ID: {} já possui o limite de dois gestores.", departamentoId);
            throw new IllegalStateException("O departamento já possui o limite de dois gestores.");
        }

        departamento.getGestores().add(funcionario);

        if (funcionario.getDepartamentos() == null) {
            funcionario.setDepartamentos(new ArrayList<>());
        }
        funcionario.getDepartamentos().add(departamento);

        Departamento updatedDepartamento = this.departamentoRepository.save(departamento);
        this.funcionarioRepository.save(funcionario);

        logger.info("Administrador ::: addGestorToDepartamento ~> Funcionário com ID: {} associado ao departamento com ID: {}", funcionarioId, departamentoId);

        var response = entityDepartamentoToResponse(updatedDepartamento);
        response.setGestores(departamento.getGestores().stream()
                .map(this::convertToFuncionarioResponse)
                .collect(Collectors.toList()));
        return response;
    }

    public DepartamentoResponse removeAllGestoresFromDepartamento(Integer departamentoId) {
        logger.info("Administrador ::: removeAllGestoresFromDepartamento ~> Desassociando todos os gestores do departamento com ID: {}", departamentoId);

        Departamento departamento = this.departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> {
                    logger.error("Administrador ::: removeAllGestoresFromDepartamento ~> Departamento com ID: {} não encontrado", departamentoId);
                    return new IllegalArgumentException("Departamento não encontrado!");
                });

        for (Funcionario gestor : departamento.getGestores()) {
            gestor.getDepartamentos().remove(departamento);
            this.funcionarioRepository.save(gestor);
        }

        departamento.getGestores().clear();

        Departamento updatedDepartamento = this.departamentoRepository.save(departamento);

        logger.info("Administrador ::: removeAllGestoresFromDepartamento ~> Todos os gestores desassociados do departamento com ID: {}", departamentoId);

        return entityDepartamentoToResponse(updatedDepartamento);
    }


    private DepartamentoResponse entityDepartamentoToResponse(Departamento departamento) {
        DepartamentoResponse response = new DepartamentoResponse();
        response.setId(departamento.getId());
        response.setNome(departamento.getNome());
        response.setDescricao(departamento.getDescricao());
        response.setRecursos(departamento.getRecursos());
        return response;
    }

    private EntityModel<DepartamentoResponse> convertToEntityModel(Departamento departamento) {
        DepartamentoResponse response = entityDepartamentoToResponse(departamento);
        response.setGestores(departamento.getGestores().stream()
                .map(this::convertToFuncionarioResponse)
                .collect(Collectors.toList()));

        EntityModel<DepartamentoResponse> entityModel = EntityModel.of(response);
        entityModel.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AdministracaoController.class).findDepartamentoById(departamento.getId()))
                .withSelfRel());
        return entityModel;
    }

    private FuncionarioResponse convertToFuncionarioResponse(Funcionario funcionario) {
        FuncionarioResponse response = new FuncionarioResponse(
                funcionario.getId(),
                funcionario.getNomeCompleto(),
                funcionario.getCargo(),
                funcionario.getEmail()
        );
        response.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(AdministracaoController.class)
                        .findFuncionarioById(funcionario.getId()))
                .withSelfRel());
        return response;
    }
}
