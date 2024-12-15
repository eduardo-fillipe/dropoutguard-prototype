# Dropout Guard Prototype

Este repositório contém o código-fonte do protótipo da aplicação proposta no projeto entregue como requisito de avaliação da disciplina **GA031 - Arquitetura, Projeto e Implementação de Sistemas de Software**, do programa de Pós-Graduação do Laboratório Nacional de Computação Científica.

O projeto propõe a **especificação arquitetural** do sistema de software **DropoutGuard**, uma plataforma de apoio à decisão que pode ser utilizada por instituições de ensino para combater a evasão escolar por meio de modelos de inteligência artificial personalizáveis. Informações mais detalhadas estão disponíveis nos documentos de especificação.

O protótipo tem como objetivo demonstrar a aplicabilidade da arquitetura no nível de implementação para um módulo específico, responsável pelo gerenciamento da base de conhecimento utilizada pelos modelos de inteligência artificial. Além disso, ele também apresenta testes de unidade e de integração para este módulo.

Implementações simplificadas de interfaces de domínio foram realizadas para o projeto, uma vez que o enfoque está na validação da arquitetura. Tais implementações são anotadas com o qualificador `@Dummy` e podem ser substituídas a qualquer momento por implementações completas, pois seguem interfaces bem definidas.

Por fim, a especificação arquitetural define dois grandes módulos de alto nível: **DropoutGuard API** e o **Intelligence Hub**. Idealmente, esses módulos devem ser executados em servidores distintos. No entanto, por simplicidade, ambos estão implementados neste repositório. Essa integração pode ser separada facilmente graças às fronteiras bem definidas pela arquitetura.

## Documentação e Especificação Arquitetural

- [Especificação de Alto Nível]()
- [Projeto Detalhado]()
- [Repositório de Documentação como Código]()
- [Monografia Original: Uso de Redes Bayesianas Multinível para Prever a Evasão Estudantil]()

## Tecnologias Utilizadas

- **Banco de Dados:** [PostgreSQL](https://www.postgresql.org/)
- **Broker de mensagens:** [RabbitMQ](https://www.rabbitmq.com/)

### Bibliotecas
- **Spring Framework:**
  - [AMQP](https://spring.io/projects/spring-amqp)
  - [Aspect](https://docs.spring.io/spring-framework/reference/core/aop.html)
  - [Data JPA](https://spring.io/projects/spring-data-jpa)
  - [Web](https://docs.spring.io/spring-boot/reference/web/index.html)
- [Flyway](https://mvnrepository.com/artifact/org.flywaydb/flyway-core): migrações de esquema do banco de dados.
- [Lombok](https://projectlombok.org/): redução de código boilerplate, principalmente para criação de builders.
- [TestContainers](https://testcontainers.com/): controle de containers Docker durante os testes.
- [Mockito](https://site.mockito.org/) e [AssertJ](https://assertj.github.io/doc/): criação de mocks e validação de condições de teste.

## Executando o Projeto

### Requisitos
- [Java 21](https://www.azul.com/downloads/?version=java-21-lts&show-old-builds=true#zulu)
- Docker:
  - [Linux](https://docs.docker.com/engine/install/)
  - [Windows](https://docs.docker.com/desktop/setup/install/windows-install/)

### Executando os Testes

Certifique-se de que o **Docker daemon** está em execução. Em seguida, execute o comando abaixo na raiz do projeto. No primeiro uso, o tempo de execução pode ser maior devido ao download das dependências.

```shell
./mvnw test
```

### Executando a Aplicação

1. **Via Docker Compose**  
   Todas as dependências serão criadas e configuradas automaticamente. A aplicação estará disponível na porta **8080**.
   ```shell
   docker compose up
   ```

2. **Manual**  
   Configure um RabbitMQ na porta **5672** e um PostgreSQL na porta **5432**. As portas podem ser ajustadas no arquivo `application.yml`. Execute os comandos abaixo:
   ```shell
   ./mvnw install -D skipTests
   ```  
   ```shell
   ./mvnw spring-boot:run
   ```