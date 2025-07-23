package br.com.rodartenogueira.backend.service.impl;

import br.com.rodartenogueira.backend.dto.AlunoDTO;
import br.com.rodartenogueira.backend.enums.Sexo;
import br.com.rodartenogueira.backend.model.Aluno;
import br.com.rodartenogueira.backend.repository.AlunoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AlunoServiceImplTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoServiceImpl alunoService;

    @Test
    void listarEstatisticas_deveRetornarDTOsOrdenadosPorIdadeComMediaCorreta() {
        Aluno a1 = new Aluno(
                "123456",
                "Maria",
                Sexo.F,
                LocalDate.of(1990, 5, 10),
                70.0, 80.0, 90.0
        );
        Aluno a2 = new Aluno(
                "789101",
                "João",
                Sexo.M,
                LocalDate.of(2000, 1, 1),
                15.0, 30.0, 30.0
        );

        // Simula retorno do repositório
        when(alunoRepository.findAll()).thenReturn(Arrays.asList(a2, a1));

        List<AlunoDTO> dtos = alunoService.listarEstatisticas();

        assertThat(dtos).hasSize(2);

        // Valida o mais novo primeiro
        assertThat(dtos.get(0).getIdentificacao()).isEqualTo("789101");
        assertThat(dtos.get(0).getNome()).isEqualTo("João");
        assertThat(dtos.get(0).getMediaNotas())
                .isEqualTo((15.0 + 30.0 + 30.0) / 3.0);


        assertThat(dtos.get(1).getIdentificacao()).isEqualTo("123456");
        assertThat(dtos.get(1).getNome()).isEqualTo("Maria");
        assertThat(dtos.get(1).getMediaNotas())
                .isEqualTo((70.0 + 80.0 + 90.0) / 3.0);
    }

    @Test
    void listarEstatisticas_deveRetornarListaVaziaQuandoNaoHaAlunos() {
        when(alunoRepository.findAll()).thenReturn(Collections.emptyList());

        List<AlunoDTO> dtos = alunoService.listarEstatisticas();

        assertThat(dtos).isEmpty();
    }

    @Test
    void importarExcel_deveLancarExcecaoQuandoArquivoVazio() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        assertThatThrownBy(() -> alunoService.importarExcel(file))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Arquivo vazio");
    }

    @Test
    void exportarExcel_deveRetornarArquivoNaoVazioComAlunos() throws Exception {
        Aluno a1 = new Aluno(
                "123456",
                "Ana",
                Sexo.F,
                LocalDate.of(1991, 7, 15),
                75.0, 85.0, 95.0
        );
        when(alunoRepository.findAll()).thenReturn(List.of(a1));

        var result = alunoService.exportarExcel();

        assertThat(result).isNotNull();
        assertThat(result.read()).isNotEqualTo(-1);
    }
}
