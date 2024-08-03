package com.deyvidsalvatore.web.gestaomasterx.domain.horas;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;

public interface RegistroHoraRepository extends JpaRepository<RegistroHora, Integer> {
	List<RegistroHora> findByFuncionario(Funcionario funcionario);
	Page<RegistroHora> findByFuncionario(Funcionario funcionario, Pageable pageable);

}
