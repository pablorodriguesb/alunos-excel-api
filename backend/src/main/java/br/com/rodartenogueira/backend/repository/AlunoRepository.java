package br.com.rodartenogueira.backend.repository;

import br.com.rodartenogueira.backend.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository <Aluno, Long> {
}
