# Sistema de Gerenciamento de Alunos

Sistema Spring Boot para importação, processamento, cálculo de estatísticas e exportação de dados de alunos via planilhas Excel e API REST.

> **Nota:** O frontend Angular será adicionado em breve. Atualmente, este repositório contempla o backend completo.

---

## 1. Contexto Geral

Este projeto implementa um sistema backend para manipulação de dados de alunos, incluindo leitura de planilhas Excel, cálculo de estatísticas e exposição de dados via API REST. O backend foi desenvolvido em Spring Boot, com frontend Angular planejado para desenvolvimento futuro.

---

## 2. Requisitos Funcionais e Técnicos

### 2.1. Backend (Java + Spring Boot)

- **Leitura de arquivo Excel** (.xlsx) enviado via API
- **Persistência dos dados** dos alunos (identificação, nome, data de nascimento, notas) em MySQL via JPA/Hibernate
- **Cálculo de estatísticas:**
  - Nome
  - Idade (calculada)
  - Média das notas (aritmética)
- **Disponibilização dos dados** via endpoints REST (JSON)
- **Exportação em Excel** com mesma estrutura da visualização (diferencial implementado)
- **API documentada** com Swagger/OpenAPI
- **Validação, tratamento e manipulação** de entradas

### 2.2. Frontend (Angular)

- **Visualização dos dados** em tabela: identificação, nome, idade, média das notas
- **Upload de arquivo Excel** para importação automática
- **Botão de download/exportação** dos dados processados (.xlsx)
- **Indicadores de loading, erro e sucesso** em integrações

---

## 3. Stack Tecnológico

- **Java 21+**
- **Spring Boot 3.5.x** (Web, Data JPA, Validation)
- **Hibernate** (JPA)
- **MySQL 8+**
- **Lombok**
- **Apache POI** (leitura/escrita Excel)
- **JUnit 5** (testes)
- **Swagger/OpenAPI**
- **Maven**
- **Angular** (em desenvolvimento)

---

## 4. Estrutura do Projeto

```
br.com.rodartenogueira.backend
├── controller
├── dto
├── enums
├── exception
├── model
├── repository
├── service
│ 
└── test
    └── java
        └── br.com.rodartenogueira.backend
            ├── controller
            │   └── integration
            └── service
                └── impl
                    └── integration
```

---

## 5. Instruções de Execução

### 5.1. Pré-requisitos

- **Java 21+**
- **MySQL 8+** (rodando)
- **Maven 3.8+**
- **Node.js** (frontend Angular)

### 5.2. Clonando e Configurando

```bash
git clone <link-do-seu-repo>
cd rodartenogueira/backend
```

**Configure o banco MySQL** em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rodarte_db?serverTimezone=America/Sao_Paulo&useSSL=false
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

**Execute a aplicação:**

```bash
./mvnw spring-boot:run
```

---

## 6. Endpoints da API

### `POST /api/alunos/importar`
Recebe um arquivo Excel (campo: `file`) e realiza importação.

### `GET /api/alunos/estatisticas`
Retorna, em JSON, a lista de alunos: identificação, nome, idade (calculada), média das notas — ordenados por idade crescente.

### `GET /api/alunos/exportar`
Download de planilha em Excel, idêntica à visualização da tela.

### Exemplos de Uso

**Requisição cURL para importação:**
```bash
curl -F 'file=@/caminho/para/alunos.xlsx' http://localhost:8080/api/alunos/importar
```

**Exemplo de resposta de estatísticas:**
```json
[
  {
    "identificacao": "123456",
    "nome": "Maria da Silva",
    "idade": 29,
    "mediaNotas": 25.00
  }
]
```

---

## 7. Validações e Regras de Negócio

- **Notas permitidas:** 0 a 100
- **Data de nascimento:** obrigatória, precisa ser válida no Excel
- **Estrutura da planilha:** deve conter colunas na ordem: Identificação, Nome, Sexo, Data de Nascimento, Nota 1, Nota 2, Nota 3
- **Importação:** dados importados sobrescrevem os existentes (re-importação substitui)
- **Cálculo da idade:** ano atual menos ano de nascimento
- **Média:** aritmética das três notas, exibida com duas casas decimais
- **Exportação/exibição:** Identificação, Nome, Idade, Média das Notas

> ⚠️ **Atenção:** O arquivo Excel a ser importado precisa seguir rigorosamente esta ordem de colunas (ver modelo).

---

## 8. Testes Automatizados

- **Testes unitários** (JUnit 5, Mockito) para service e controller
- **Teste de integração** para todos os endpoints principais
- **Teste de carga** do contexto Spring Boot

**Para executar os testes:**
```bash
./mvnw test
```

---

## 9. Diferenciais Entregues

- **Testes unitários e de integração** cobrindo validação e APIs REST
- **API totalmente documentada** via Swagger (`http://localhost:8080/swagger-ui.html`)

---

## 10. Documentação e Entregáveis

### Artefatos Incluídos
- **Código-fonte completo** (Java backend)
- **Dump do banco de dados** MySQL (formato .sql)
- **Amostra do arquivo Excel** exportado
- **README detalhado** (este arquivo)
- **API documentada** via SwaggerUI

### Observações Técnicas
- **Timezone do banco:** utiliza `America/Sao_Paulo`

---