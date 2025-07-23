package br.com.rodartenogueira.backend.service.impl;

import br.com.rodartenogueira.backend.dto.AlunoDTO;
import br.com.rodartenogueira.backend.enums.Sexo;
import br.com.rodartenogueira.backend.model.Aluno;
import br.com.rodartenogueira.backend.repository.AlunoRepository;
import br.com.rodartenogueira.backend.service.AlunoService;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlunoServiceImpl implements AlunoService {

    private final AlunoRepository alunoRepository;

    @Autowired
    public AlunoServiceImpl(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @Override
    public void importarExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo vazio");
        }

        List<Aluno> alunos = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            boolean first = true;

            while (it.hasNext()) {
                Row row = it.next();
                if (first) { first = false; continue; }

                String identificacao = getCellAsString(row.getCell(0));
                String nome = getCellAsString(row.getCell(1));
                String sexoStr = getCellAsString(row.getCell(2));

                LocalDate dataNasc;
                try {
                    dataNasc = row.getCell(3).getLocalDateTimeCellValue().toLocalDate();
                } catch (Exception e) {
                    continue;
                }

                Cell cellNota1 = row.getCell(4);
                Cell cellNota2 = row.getCell(5);
                Cell cellNota3 = row.getCell(6);
                // Se qualquer nota for ausente ou blank, pula
                if (cellNota1 == null || cellNota1.getCellType() == CellType.BLANK ||
                        cellNota2 == null || cellNota2.getCellType() == CellType.BLANK ||
                        cellNota3 == null || cellNota3.getCellType() == CellType.BLANK) {
                    continue;
                }
                double nota1 = cellNota1.getNumericCellValue();
                double nota2 = cellNota2.getNumericCellValue();
                double nota3 = cellNota3.getNumericCellValue();

                // Validacoes
                if (nome == null || nome.isBlank() || nome.length() > 100) continue;

                Sexo sexo;
                try {
                    sexo = Sexo.valueOf(sexoStr.trim().toUpperCase());
                } catch (Exception e) {
                    continue;
                }
                if (dataNasc.isAfter(LocalDate.now())) continue;
                if (nota1 < 0 || nota1 > 100 || nota2 < 0 || nota2 > 100 || nota3 < 0 || nota3 > 100) continue;

                Aluno aluno = new Aluno(identificacao, nome, sexo, dataNasc, nota1, nota2, nota3);
                alunos.add(aluno);
            }
            System.out.println("Qtde de alunos lidos do excel: " + alunos.size());
            alunoRepository.saveAll(alunos);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao importar Excel: " + e.getMessage(), e);
        }
    }

    @Override
    public List<AlunoDTO> listarEstatisticas() {
        List<Aluno> alunos = alunoRepository.findAll();

        return alunos.stream()
                .map(aluno -> {
                    int idade = Period.between(aluno.getDataNascimento(), LocalDate.now()).getYears();
                    double media = (aluno.getNota1() + aluno.getNota2() + aluno.getNota3()) / 3.0;
                    AlunoDTO dto = new AlunoDTO();
                    dto.setId(aluno.getId());
                    dto.setNome(aluno.getNome());
                    dto.setIdade(idade);
                    dto.setMediaNotas(media);
                    return dto;
                })
                .sorted(Comparator.comparing(AlunoDTO::getIdade))
                .collect(Collectors.toList());
    }

    @Override
    public ByteArrayInputStream exportarExcel() {
        try (
                Workbook workbook = WorkbookFactory.create(true);
                ByteArrayOutputStream out = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet("Alunos");

            // Monta cabecalho
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Identificação");
            header.createCell(1).setCellValue("Nome");
            header.createCell(2).setCellValue("Idade");
            header.createCell(3).setCellValue("Média das Notas");

            // Busca alunos e monta linhas
            List<Aluno> alunos = alunoRepository.findAll();
            int rowIdx = 1;
            Locale localeBR = Locale.forLanguageTag("pt-BR");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(localeBR);
            DecimalFormat df = new DecimalFormat("0.00", symbols);

            for (Aluno aluno : alunos) {
                int idade = Period.between(aluno.getDataNascimento(), LocalDate.now()).getYears();
                double media = (aluno.getNota1() + aluno.getNota2() + aluno.getNota3()) / 3.0;

                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(aluno.getIdentificacao());
                row.createCell(1).setCellValue(aluno.getNome());
                row.createCell(2).setCellValue(idade);
                row.createCell(3).setCellValue(df.format(media));
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Falha ao exportar Excel: " + e.getMessage(), e);
        }
    }

    // método utilitário
    private String getCellAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double d = cell.getNumericCellValue();
                if (d == (long) d) return String.valueOf((long) d);
                return String.valueOf(d);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}

