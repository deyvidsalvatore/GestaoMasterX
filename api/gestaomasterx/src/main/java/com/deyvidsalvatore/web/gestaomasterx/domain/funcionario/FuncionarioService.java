package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.exceptions.FuncionarioNotFoundException;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.RegistroHora;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.RegistroHoraRepository;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraRequest;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.HoraResponse;
import com.deyvidsalvatore.web.gestaomasterx.domain.horas.dtos.ListaHorasResponse;
import com.deyvidsalvatore.web.gestaomasterx.utils.DataUtils;

@Service
public class FuncionarioService {

	private static final Logger LOG = LoggerFactory.getLogger(FuncionarioService.class);

	private final FuncionarioRepository funcionarioRepository;
	private final RegistroHoraRepository horasRepository;

	public FuncionarioService(FuncionarioRepository funcionarioRepository, RegistroHoraRepository horasRepository) {
		this.funcionarioRepository = funcionarioRepository;
		this.horasRepository = horasRepository;
	}

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

	private RegistroHora findHorasById(Integer registroHoraId) {
		return horasRepository.findById(registroHoraId)
	        .orElseThrow(() -> new IllegalArgumentException("Registro de horas não encontrado"));
	}


	
	private HoraResponse horasToResponse(Funcionario funcionario, RegistroHora registroHora) {
		return new HoraResponse(registroHora.getId(), funcionario.getId(), DataUtils.formatDate(registroHora.getData()),
				DataUtils.formatTime(registroHora.getHoraEntrada()), DataUtils.formatTime(registroHora.getHoraSaida()),
				registroHora.getHorasTrabalhadas());
	}

	public Funcionario findFuncionarioById(Integer id) {
		return this.funcionarioRepository.findById(id).orElseThrow(FuncionarioNotFoundException::new);
	}

}
