package br.com.rodartenogueira.backend.service.impl.integration;

import br.com.rodartenogueira.backend.model.Aluno;
import br.com.rodartenogueira.backend.repository.AlunoRepository;
import br.com.rodartenogueira.backend.service.impl.AlunoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class AlunoServiceIntegrationTest {

    @Autowired
    private AlunoServiceImpl alunoService;

    @Autowired
    private AlunoRepository alunoRepository;

    @Test
    void listarEstatisticas_deveRetornarAlunosDoBanco() {
        alunoRepository.save(new Aluno(
                "999999", "Teste", br.com.rodartenogueira.backend.enums.Sexo.F, LocalDate.of(1999,1,1), 70.0, 80.0, 90.0
        ));

        var dtos = alunoService.listarEstatisticas();

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getNome()).isEqualTo("Teste");
        assertThat(dtos.get(0).getMediaNotas()).isEqualTo((70.0+80.0+90.0)/3.0);
    }
}
