
## 🐾 Desafio Itaú – Localizador de Pets

### 📖 Descrição do Desafio
Aplicação desenvolvida como parte do **Desafio Itaú – Localizador de Pets**.
O sistema recebe as coordenadas de um pet rastreado e consulta o serviço externo PositionStack para obter a localização
detalhada (país, estado, cidade, bairro e endereço).

### 🚀 Tecnologias Utilizadas
- **Java 17**: Linguagem de programação utilizada.
- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Spring Web / Spring Validation**: Criação de APIs REST e validação de entradas
- **Spring Cloud OpenFeign**: Client HTTP declarativo para integrar com a API externa PositionStack.
- **Maven**: Gerenciador de dependências e build do projeto.
- **JUnit / Mockito / WireMock**: Frameworks para testes unitários e de integração.
- **Lombok**: Biblioteca para reduzir boilerplate code.
- **Swagger**: Documentação da API para facilitar o entendimento e teste do endpoint.

### 📦 Como executar o projeto

#### Pré-requisitos
- Java 17 ou superior instalado.
- Maven instalado.
- Chave de API do [PositionStack](https://positionstack.com/).

#### ▶️ Passos para rodar localmente

1. Clone o repositório:
    ```powershell
    git clone https://github.com/ArianaRusso/Desafio-Itau---Localizador-de-Pets.git
    cd Desafio-Itau---Localizador-de-Pets
    cd localizador-pets
    ```
2. Instale as dependências:
    ```powershell
      mvn clean install
    ```
3. Configure a chave da API PositionStack:
    - No arquivo application.yml:
        ```yaml
        positionstack:
            api:
              key: sua_chave_aqui
        ``` 
    - Ou via variável de ambiente:
        - No Linux/Mac:
            ```bash
            export POSITIONSTACK_API_KEY=sua_chave_aqui
            ```
        - No Windows (PowerShell):
            ```powershell
            setx POSITIONSTACK_API_KEY "sua_chave_aqui"
            ```

### ▶️ Execução

1. Rode o comando:
   - Feche o terminal e abra novamente para reconhecer a variável de ambiente, se for o caso.
       ```powershell
        cd Desafio-Itau---Localizador-de-Pets
        cd localizador-pets
     
        mvn spring-boot:run
       ```
2. Aplicação está rodando na porta [localhost:8080](http://localhost:8080/)

3. Para testar a API utilize o Postman, Insomnia ou acesse a documentação Swagger.

### 📌 Endpoints principais
- `POST /v1/pet/localizacao`: Recebe as coordenadas do pet e retorna a localização detalhada.
- Exemplo de request:
    ```json
      {
          "idColeira": "COL-12345",
          "latitude": -23.561684,
          "longitude": -46.655981,
          "dataHora": "2025-09-28T10:00:00"
      }
  ```
  - Exemplo de response:
      ```json
     {
        "pais": "Brasil",
        "estado": "SP",
        "cidade": "São Paulo",
        "bairro": "Centro",
        "endereco": "Rua A, 123"
    }
    ```
  - Possíveis respostas de erro:
      - `400 Bad Request`→ Dados inválidos na requisição.
      - `404 Not Found`→ Localização não encontrada.
      - `500 Internal Server Error`→ Erro ao integrar com PositionStack.

### 📊 Documentação da API
Após rodar o projeto, acesse:
👉  [Documentação](http://localhost:8080/docs)

### 🔎 Observabilidade
A aplicação implementa observabilidade utilizando **Spring Boot Actuator** e **logs estruturados**.

#### Endpoints disponíveis
- `/actuator/health` → Verifica a saúde da aplicação e da integração com o serviço externo PositionStack.
- `/actuator/info` → Exibe informações sobre a aplicação (nome, versão, descrição).
- `/actuator/metrics` → Métricas padrão (uso de memória, threads, HTTP requests).
- `/actuator/httpexchanges` → Histórico de chamadas HTTP.

#### Logs
- **INFO**: Fluxo principal (início e fim de operações).
- **DEBUG**: Detalhes de dados retornados pela integração.
- **WARN**: Localizações não encontradas.
- **ERROR**: Falhas de integração com serviços externos.

#### Exemplo de Health Check
  ```bash
           curl http://localhost:8080/actuator/health
  ```
### 🧪 Testes
Para executar os testes, rode o comando:
```bash
    ./mvnw test 
```
Coverage: 
```bash
    ./mvnw clean verify
```

### Estratégia de Testes
- **Testes de Unidade**: Utilizados JUnit e Mockito para testar Services, Mappers, Clients e Controller.
- **Testes de Integração**: Utilizados Spring Boot Test e WireMock para simular respostas da API PositionStack.

### 📂 Estrutura do projeto
```text
    src
├── main
│   ├── java/com/itau/localizador_pets
│   │    ├── core        # entidades, exceções e regras de negócio
│   │    ├── infrastructure # Feign client + integração, DTOS, mappers, config
│   │    ├── presentation   # controllers, DTOs, handlers
│   │    └── LocalizadorPetsApplication.java
│   └── resources
│        └── application.yaml
└── test
├── java/com/itau/localizador_pets
│    ├── unit         # testes de unidade
│    └── integration  # testes de integração 
```

### 🧠 Decisões Técnicas e Racional da Solução
#### Arquitetura em camadas (Core, Infrastructure, Presentation):
Optei por separar regras de negócio, integração e apresentação. 
- Camada Core: Mantive o LocalizadorService e sua implementação (LocalizadorServiceImpl) 
dentro do core, porque é onde ficam as regras de negócio da aplicação. O serviço é responsável por receber a entidade InfoRastreioPet
, orquestrar a busca de localização via client e aplicar as regras de negócio de fallback e exceções. O LocalizadorServiceImpl depende de uma abstração, 
a interface BuscarLocalizacaoClient, a ser implementada na camada de infraestrutura, garantindo o princípio de inversão de dependência.
- Camada Infrastructure: Contém a classe BuscarLocalizacaoClientImpl que é a implementação concreta da integração com a API externa PositionStack.
  Essa classe usa o RastreioLocalizacaoClient (Feign) para fazer a chamada real à API externa PositionStack 
e o ClientToDomainMapper para traduzir os dados para o domínio.
- Camada Presentation: Contem o LocalizadorController que expõe o endpoint REST e o Handler para tratamento de exceções.
#### Uso do Spring Cloud OpenFeign:
Escolhi OpenFeign ao invés de RestTemplate/WebClient por ser declarativo, menos verboso e integrar nativamente com o Spring Boot.

#### Tratamento centralizado de erros:
Criei um GlobalExceptionHandler que converte exceções em respostas REST padronizadas:

* LocalizacaoNaoEncontradaException → 404 Not Found.

* ErroIntegracaoException → 502 Bad Gateway.

* MethodArgumentNotValidException → 400 Bad Request.

#### Mapeadores explícitos:
Preferi criar ClientToDomainMapper e DtoToDomainMapper para manter o domínio isolado.

#### Premissas adotadas:
* Como a API externa retorna múltiplos resultados, a aplicação considera o primeiro como a localização do pet.
* A chave da API é lida via variável de ambiente (POSITIONSTACK_API_KEY) para maior segurança.

### 📝 Próximos passos / Melhorias possíveis
- Implementar autenticação e autorização.
- Dockerizar a aplicação.
- Configurar CI/CD.
- Deploy em nuvem (AWS, Azure, GCP).

### 👩‍ Autora
Desenvolvido por **Ariana de Almeida Russo** no contexto do Desafio Itaú.

