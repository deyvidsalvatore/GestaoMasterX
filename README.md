# GestaoMasterX

**GestaoMasterX** é uma aplicação de gerenciamento de feedbacks e horas de trabalho para funcionários, gestores e administradores. A aplicação oferece funcionalidades para a gestão de feedbacks, atribuição e atualização de horas, com uma estrutura para diferentes tipos de usuários com permissões específicas.

## Sumário

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Endpoints da API](#endpoints-da-api)
  - [Autenticação](#autenticação)
  - [Funcionários](#funcionários)
  - [Gestores](#gestores)
  - [Administradores](#administradores)
- [Executando a Aplicação](#executando-a-aplicação)
- [Contribuindo](#contribuindo)
- [Licença](#licença)

## Visão Geral

**GestaoMasterX** permite a gestão eficiente de feedbacks e horas de trabalho através de uma plataforma acessível para funcionários, gestores e administradores. 

## Funcionalidades

- **Funcionários**:
  - Consultar e atualizar suas horas trabalhadas.
  - Visualizar feedbacks recebidos.

- **Gestores**:
  - Atribuir, atualizar e remover feedbacks.
  - Consultar feedbacks atribuídos.

- **Administradores**:
  - Gerenciar usuários e permissões (criar, atualizar e remover usuários).

## Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Spring Security**: Autenticação e autorização.
- **Spring Data JPA**: Persistência de dados.
- **Spring HATEOAS**: Suporte para a criação de APIs REST com links HATEOAS.
- **Swagger (OpenAPI)**: Documentação da API.

## Configuração do Ambiente

1. **Clone o Repositório**

   ```bash
   git clone https://github.com/seu-usuario/gestao-masterx.git
   ```

2. **Navegue para o Diretório do Projeto**

   ```bash
   cd gestao-masterx
   ```

3. **Configure o Banco de Dados**

   Edite o arquivo `src/main/resources/application.properties` para definir as configurações do banco de dados.

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gestao_masterx
   spring.datasource.username=root
   spring.datasource.password=sua-senha
   ```

4. **Compile e Execute o Projeto**

   ```bash
   ./mvnw spring-boot:run
   ```

## Arquivo dotenv

```markdown
# Configuração do Ambiente
# Para rodar o aplicativo, crie um arquivo `.env` no diretório raiz do seu 
# projeto ou na pasta resources com o seguinte conteúdo:

# Banco de Dados
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/gestaomasterx_db?createDatabaseIfNotExist=true
SPRING_DATASOURCE_USERNAME=user
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_CLASSNAME=com.mysql.cj.jdbc.Driver

# Email
SPRING_MAIL_HOST=smtp.example.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=example@example.com
SPRING_MAIL_PASSWORD=emailappkey

# Segurança
SPRING_SECURITY_USERNAME=admin
SPRING_SECURITY_PASSWORD=adminpassword
```

Substitua os valores de exemplo pelos seus próprios. Mantenha o arquivo seguro e não compartilhe suas credenciais publicamente. 

## Endpoints da API

### Autenticação

**Autenticação é feita via formulário. Use a interface web para autenticar-se.**

- **Login**
  - **URL**: `/login`
  - **Method**: `POST`
  - **Form Fields**:
    - `username`: Nome de usuário.
    - `password`: Senha.
  - **Response**: Redireciona para a página inicial após o login bem-sucedido.

- **Logout**
  - **URL**: `/logout`
  - **Method**: `POST`
  - **Response**: Redireciona para a página de login após o logout.

### Funcionários

- **Buscar Horas do Funcionário (Paginado)**
  - **URL**: `/api/v1/funcionarios/{funcionarioId}/horas`
  - **Method**: `GET`
  - **Parâmetros**:
    - `page` (opcional): Número da página, padrão é 0.
    - `size` (opcional): Número de registros por página, padrão é 10.
    - `sort` (opcional): Ordenação, pode ser "asc" ou "desc", padrão é "asc".
  - **Response**:
    ```json
    {
      "content": [
        {
          "id": 1,
          "data": "2024-08-08",
          "horas": 8
        }
      ],
      "totalElements": 1,
      "totalPages": 1
    }
    ```

- **Buscar Total de Horas do Funcionário**
  - **URL**: `/api/v1/funcionarios/{funcionarioId}/horas/totais`
  - **Method**: `GET`
  - **Response**:
    ```json
    {
      "totalHoras": 160
    }
    ```

- **Atribuir Horas**
  - **URL**: `/api/v1/funcionarios/{funcionarioId}/horas`
  - **Method**: `POST`
  - **Request Body**:
    ```json
    {
      "data": "2024-08-08",
      "horas": 8
    }
    ```
  - **Response**:
    ```json
    {
      "message": "Horas atribuídas com sucesso"
    }
    ```

- **Atualizar Horas**
  - **URL**: `/api/v1/funcionarios/{funcionarioId}/horas/{registroHoraId}`
  - **Method**: `PUT`
  - **Request Body**:
    ```json
    {
      "data": "2024-08-08",
      "horas": 8
    }
    ```
  - **Response**:
    ```json
    {
      "message": "Horas atualizadas com sucesso"
    }
    ```

- **Remover Horas**
  - **URL**: `/api/v1/funcionarios/{funcionarioId}/horas/{registroHoraId}`
  - **Method**: `DELETE`
  - **Response**:
    ```json
    {
      "message": "Horas removidas com sucesso"
    }
    ```

- **Listar Feedbacks Atribuídos**
  - **URL**: `/api/v1/funcionarios/{funcionarioId}/feedbacks`
  - **Method**: `GET`
  - **Parâmetros**:
    - `page` (opcional): Número da página, padrão é 0.
    - `size` (opcional): Número de registros por página, padrão é 10.
    - `sort` (opcional): Ordenação, pode ser "asc" ou "desc", padrão é "asc".
  - **Response**:
    ```json
    {
      "content": [
        {
          "id": 1,
          "data": "2024-08-08",
          "comentario": "Ótimo trabalho!"
        }
      ],
      "totalElements": 1,
      "totalPages": 1
    }
    ```

### Gestores

- **Listar Feedbacks Atribuídos**
  - **URL**: `/api/v1/gestores/{gestorId}/feedbacks`
  - **Method**: `GET`
  - **Parâmetros**:
    - `page` (opcional): Número da página, padrão é 0.
    - `size` (opcional): Número de registros por página, padrão é 10.
    - `sort` (opcional): Ordenação, pode ser "asc" ou "desc", padrão é "asc".
  - **Response**:
    ```json
    {
      "content": [
        {
          "id": 1,
          "data": "2024-08-08",
          "comentario": "Feedback positivo"
        }
      ],
      "totalElements": 1,
      "totalPages": 1
    }
    ```

- **Obter Feedback por ID**
  - **URL**: `/api/v1/gestores/{gestorId}/feedbacks/feedback/{feedbackId}`
  - **Method**: `GET`
  - **Response**:
    ```json
    {
      "id": 1,
      "data": "2024-08-08",
      "comentario": "Excelente desempenho!"
    }
    ```

- **Atribuir Feedback**
  - **URL**: `/api/v1/gestores/{gestorId}/feedbacks/{funcionarioId}`
  - **Method**: `POST`
  - **Request Body**:
    ```json
    {
      "comentario": "Ótimo trabalho!",
      "data": "2024-08-08"
    }
    ```
  - **Response**:
    ```json
    {
      "message": "Feedback atribuído com sucesso"
    }
    ```

- **Atualizar Feedback**
  - **URL**: `/api/v1/gestores/{gestorId}/feedbacks/feedback/{feedbackId}`
  - **Method**: `PUT`
  - **Request Body**:
    ```json
    {
      "comentario": "Trabalho muito bom",
      "data": "2024-08-08"
    }
    ```
  - **Response**:
    ```json
    {
      "message": "Feedback atualizado com sucesso"
    }
    ```

- **Remover Feedback**
  - **URL**: `/api/v1/gestores/{gestorId}/feedbacks/feedback/{feedbackId}`
  - **Method**: `DELETE`
  - **Response**:
    ```json
    {
      "message

": "Feedback removido com sucesso"
    }
    ```

### Administradores

Claro! Vou criar um README para a sua classe `AdministracaoController`, que é a parte da API responsável pela administração dos funcionários e departamentos. Esse README vai detalhar as operações CRUD (Create, Read, Update, Delete) para funcionários e departamentos, além de algumas operações adicionais.

---

# Documentação da API - AdministracaoController

## Introdução

O `AdministracaoController` é um controlador REST no projeto **GestaoMasterX** que gerencia operações relacionadas a funcionários e departamentos. Ele oferece endpoints para criar, ler, atualizar e excluir funcionários e departamentos, bem como promover funcionários e gerenciar gestores de departamentos.

## Endpoints

### Funcionários

1. **Listar Funcionários**

   - **URL**: `/api/v1/administrativo/funcionarios`
   - **Método**: `GET`
   - **Parâmetros de Consulta**:
     - `page` (opcional, default: 0): Número da página para paginação.
     - `size` (opcional, default: 10): Número de funcionários por página.
     - `sort` (opcional, default: asc): Ordenação dos funcionários (`asc` para crescente e `desc` para decrescente).
   - **Resposta**:
     ```json
     {
       "content": [
         {
           "id": 1,
           "nomeCompleto": "João da Silva",
           "email": "joao.silva@example.com",
           "cargo": "Analista"
         }
       ],
       "totalElements": 1,
       "totalPages": 1
     }
     ```

2. **Obter Funcionário por ID**

   - **URL**: `/api/v1/administrativo/funcionarios/{id}`
   - **Método**: `GET`
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nomeCompleto": "João da Silva",
       "email": "joao.silva@example.com",
       "cargo": "Analista"
     }
     ```

3. **Criar Novo Funcionário**

   - **URL**: `/api/v1/administrativo/funcionarios`
   - **Método**: `POST`
   - **Corpo da Requisição**:
     ```json
     {
       "nomeCompleto": "João da Silva",
       "email": "joao.silva@example.com",
       "cargo": "Analista"
     }
     ```
   - **Resposta**:
     ```json
     "Uma nova conta foi criada e enviada ao e-mail requisitado."
     ```

4. **Atualizar Funcionário**

   - **URL**: `/api/v1/administrativo/funcionarios/{id}`
   - **Método**: `PUT`
   - **Corpo da Requisição**:
     ```json
     {
       "nomeCompleto": "João da Silva Atualizado",
       "email": "joao.silva@novodominio.com",
       "cargo": "Coordenador"
     }
     ```
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nomeCompleto": "João da Silva Atualizado",
       "email": "joao.silva@novodominio.com",
       "cargo": "Coordenador"
     }
     ```

5. **Excluir Funcionário**

   - **URL**: `/api/v1/administrativo/funcionarios/{id}`
   - **Método**: `DELETE`
   - **Resposta**: `204 No Content`

6. **Promover Funcionário**

   - **URL**: `/api/v1/administrativo/funcionarios/{funcionarioId}/promover`
   - **Método**: `PATCH`
   - **Resposta**: `204 No Content`

### Departamentos

1. **Listar Departamentos**

   - **URL**: `/api/v1/administrativo/departamentos`
   - **Método**: `GET`
   - **Parâmetros de Consulta**:
     - `page` (opcional, default: 0): Número da página para paginação.
     - `size` (opcional, default: 10): Número de departamentos por página.
     - `sort` (opcional, default: asc): Ordenação dos departamentos (`asc` para crescente e `desc` para decrescente).
   - **Resposta**:
     ```json
     {
       "content": [
         {
           "id": 1,
           "nome": "TI",
           "descricao": "Tecnologia da Informação"
         }
       ],
       "totalElements": 1,
       "totalPages": 1
     }
     ```

2. **Obter Departamento por ID**

   - **URL**: `/api/v1/administrativo/departamentos/{id}`
   - **Método**: `GET`
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nome": "TI",
       "descricao": "Tecnologia da Informação"
     }
     ```

3. **Criar Novo Departamento**

   - **URL**: `/api/v1/administrativo/departamentos`
   - **Método**: `POST`
   - **Corpo da Requisição**:
     ```json
     {
       "nome": "TI",
       "descricao": "Tecnologia da Informação"
     }
     ```
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nome": "TI",
       "descricao": "Tecnologia da Informação"
     }
     ```

4. **Atualizar Departamento**

   - **URL**: `/api/v1/administrativo/departamentos/{id}`
   - **Método**: `PUT`
   - **Corpo da Requisição**:
     ```json
     {
       "nome": "TI Atualizado",
       "descricao": "Departamento de Tecnologia da Informação"
     }
     ```
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nome": "TI Atualizado",
       "descricao": "Departamento de Tecnologia da Informação"
     }
     ```

5. **Excluir Departamento**

   - **URL**: `/api/v1/administrativo/departamentos/{id}`
   - **Método**: `DELETE`
   - **Resposta**: `204 No Content`

6. **Adicionar Gestor a um Departamento**

   - **URL**: `/api/v1/administrativo/departamentos/{departamentoId}/gestores/{funcionarioId}`
   - **Método**: `POST`
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nome": "TI",
       "descricao": "Tecnologia da Informação"
     }
     ```

7. **Remover Todos os Gestores de um Departamento**

   - **URL**: `/api/v1/administrativo/departamentos/{id}/gestores`
   - **Método**: `DELETE`
   - **Resposta**:
     ```json
     {
       "id": 1,
       "nome": "TI",
       "descricao": "Tecnologia da Informação"
     }
     ```

## Requisitos de Autorização

Todos os endpoints requerem que o usuário tenha o papel `ADMINISTRADOR`. A segurança é garantida através da anotação `@PreAuthorize("hasRole('ADMINISTRADOR')")`.


## Executando a Aplicação

1. **Compile e Execute o Projeto**

   ```bash
   ./mvnw spring-boot:run
   ```

2. **Acesse a Aplicação**

   - **URL**: `http://localhost:8080`
   - **Páginas**:
     - **Login**: `/login`
     - **Página Principal**: Após login, você será redirecionado para a página principal baseada no seu papel.

## Contribuindo

Se desejar contribuir para o **GestaoMasterX**, siga estes passos:

1. Faça um fork do repositório.
2. Crie uma branch para a sua feature (`git checkout -b minha-feature`).
3. Faça commit das suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Envie para o repositório remoto (`git push origin minha-feature`).
5. Abra um pull request.

## Licença

Este projeto é licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

Espero que este README seja útil e que cubra todos os aspectos do seu projeto! Se precisar de mais alguma coisa, é só avisar.