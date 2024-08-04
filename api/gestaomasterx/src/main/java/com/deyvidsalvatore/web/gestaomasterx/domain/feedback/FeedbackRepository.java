package com.deyvidsalvatore.web.gestaomasterx.domain.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deyvidsalvatore.web.gestaomasterx.domain.funcionario.Funcionario;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer>{

	Page<Feedback> findByGestor(Funcionario gestor, Pageable pageable);
	Page<Feedback> findByFuncionario(Funcionario funcionario, Pageable pageable);

}
