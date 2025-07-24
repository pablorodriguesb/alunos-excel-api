package br.com.rodartenogueira.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoDTO {
    private String identificacao;
    private String nome;
    private Integer idade;
    private Double mediaNotas;
}