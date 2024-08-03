package com.deyvidsalvatore.web.gestaomasterx.domain.funcionario;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	public ListaHorasResponse buscarHorasDoFuncionario(Integer funcionarioId) {
		LOG.info("Funcionario ::: Buscando horas do funcionario de id {}", funcionarioId);
		Funcionario funcionario = findFuncionarioById(funcionarioId);
		List<RegistroHora> registros = horasRepository.findByFuncionario(funcionario);

		BigDecimal totalHoras = registros.stream().map(RegistroHora::getHorasTrabalhadas).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		return new ListaHorasResponse(registros.size(), totalHoras, funcionarioId);
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
