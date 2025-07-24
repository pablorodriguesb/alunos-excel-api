package br.com.rodartenogueira.backend.controller;

import br.com.rodartenogueira.backend.dto.AlunoDTO;
import br.com.rodartenogueira.backend.service.AlunoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlunoService alunoService;

    @Test
    @DisplayName("POST /importar deve retornar 200 OK ao importar excel")
    void importarDeveRetornarOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "alunos.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[]{1, 2, 3}
        );
        Mockito.doNothing().when(alunoService).importarExcel(any());

        mockMvc.perform(multipart("/api/alunos/importar").file(file))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /estatisticas deve retornar lista de estatísticas")
    void estatisticasDeveRetornarLista() throws Exception {
        AlunoDTO aluno = new AlunoDTO();
        aluno.setIdentificacao("123456");
        aluno.setNome("Maria");
        aluno.setIdade(30);
        aluno.setMediaNotas(29.67);

        Mockito.when(alunoService.listarEstatisticas())
                .thenReturn(List.of(aluno));

        mockMvc.perform(get("/api/alunos/estatisticas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].identificacao").value("123456"))
                .andExpect(jsonPath("$[0].nome").value("Maria"))
                .andExpect(jsonPath("$[0].idade").value(30))
                .andExpect(jsonPath("$[0].mediaNotas").value(29.67));
    }

    @Test
    @DisplayName("GET /exportar deve baixar o arquivo XLSX")
    void exportarDeveBaixarArquivo() throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        Mockito.when(alunoService.exportarExcel()).thenReturn(bais);

        mockMvc.perform(get("/api/alunos/exportar"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=alunos.xlsx"))
                .andExpect(content().contentType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ));
    }
}
