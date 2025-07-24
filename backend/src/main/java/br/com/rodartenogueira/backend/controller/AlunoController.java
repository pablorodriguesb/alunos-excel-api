package br.com.rodartenogueira.backend.controller;

import br.com.rodartenogueira.backend.dto.AlunoDTO;
import br.com.rodartenogueira.backend.service.AlunoService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping("/importar")
    public ResponseEntity<Void> importar(@RequestParam("file") MultipartFile file) {
        alunoService.importarExcel(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/estatisticas")
    public List<AlunoDTO> estatisticas() {
        return alunoService.listarEstatisticas();
    }

    @GetMapping("/exportar")
    public ResponseEntity<InputStreamResource> exportar() {
        ByteArrayInputStream file = alunoService.exportarExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=alunos.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(file));
    }
}