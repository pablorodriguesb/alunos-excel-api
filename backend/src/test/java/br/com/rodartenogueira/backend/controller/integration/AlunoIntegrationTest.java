package br.com.rodartenogueira.backend.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AlunoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveImportarEListarAlunosComSucesso() throws Exception {
        // Importa arquivo Excel de exemplo
        var resource = new ClassPathResource("testes.xlsx");
        mockMvc.perform(multipart("/api/alunos/importar")
                        .file("file", resource.getInputStream().readAllBytes()))
                .andExpect(status().isOk());

        // Verifica listagem ja com dados importados
        mockMvc.perform(get("/api/alunos/estatisticas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].identificacao").exists())
                .andExpect(jsonPath("$[0].nome").exists())
                .andExpect(jsonPath("$[0].mediaNotas").exists());
    }
}
