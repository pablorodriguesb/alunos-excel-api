package br.com.rodartenogueira.backend.service;

import br.com.rodartenogueira.backend.dto.AlunoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface AlunoService {
    void importarExcel(MultipartFile file);
    List<AlunoDTO> listarEstatisticas();
    ByteArrayInputStream exportarExcel();
}
