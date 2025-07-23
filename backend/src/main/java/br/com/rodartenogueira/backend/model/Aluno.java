package br.com.rodartenogueira.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import br.com.rodartenogueira.backend.enums.Sexo;


import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "alunos")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A identificação é obrigatória.")
    @Column(nullable = false, length = 50)
    private String identificacao;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "O sexo é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private Sexo sexo;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser no passado.")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @NotNull(message = "A nota 1 é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota deve ser maior ou igual a 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "A nota deve ser menor ou igual a 100.")
    @Column(nullable = false)
    private Double nota1;

    @NotNull(message = "A nota 2 é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota deve ser maior ou igual a 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "A nota deve ser menor ou igual a 100.")
    @Column(nullable = false)
    private Double nota2;

    @NotNull(message = "A nota 3 é obrigatória.")
    @DecimalMin(value = "0.0", inclusive = true, message = "A nota deve ser maior ou igual a 0.")
    @DecimalMax(value = "100.0", inclusive = true, message = "A nota deve ser menor ou igual a 100.")
    @Column(nullable = false)
    private Double nota3;

    // Construtor padrao
    public Aluno() {
    }

    // Construtor completo
    public Aluno(String identificacao, String nome, Sexo sexo, LocalDate dataNascimento, Double nota1, Double nota2, Double nota3) {
        this.identificacao = identificacao;
        this.nome = nome;
        this.sexo = sexo;
        this.dataNascimento = dataNascimento;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
    }
}

