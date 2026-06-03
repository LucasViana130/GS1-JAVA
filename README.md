# AgroOrbit API - Global Solution 2026

## Integrantes

| Nome | RM |
|---|---|
| Lucas Gonçalves Viana | 563254 |
| Deryk de Souza Queiroz | 563412 |
| Vinicius Paschoeto da Silva | 563089 |
| Felipe Wiclif Leal da Silva| 563901 |

---

## Links da entrega

| Item | Link |
|---|---|
| Repositório GitHub | https://github.com/LucasViana130/GS1-JAVA |
| Deploy da API | https://gs1-java-production.up.railway.app |
| Swagger/OpenAPI | https://gs1-java-production.up.railway.app/swagger-ui.html |
| H2 Console | https://gs1-java-production.up.railway.app/h2-console |
| Vídeo de apresentação | Preencher |
| Vídeo pitch | Preencher |

> Observação: se o Swagger não abrir em `/swagger-ui.html`, acesse `/swagger-ui/index.html`.

---

# 1. Visão geral da solução

O **AgroOrbit** é uma API REST desenvolvida em **Java com Spring Boot** para monitoramento agrícola inteligente. A solução combina dados simulados de sensores de campo com dados satelitais de vegetação, como **NDVI**, para identificar riscos climáticos e operacionais em talhões agrícolas.

A proposta principal é auxiliar produtores e técnicos agrícolas na tomada de decisão, permitindo:

- cadastro de usuários produtores, administradores e técnicos;
- cadastro de fazendas;
- cadastro de talhões/áreas de plantio;
- armazenamento do polígono real do talhão em formato GeoJSON;
- cadastro de sensores simulados;
- registro de leituras de temperatura, umidade do ar e umidade do solo;
- registro de dados satelitais(futuramente pode ser integrado a api de satélites), como NDVI médio, mínimo e máximo;
- análise de risco climático e vegetativo;
- geração de alertas;
- geração de recomendações;
- consulta de dados via Swagger e Postman.

---

# 2. Problema abordado

A agricultura está cada vez mais exposta a eventos climáticos extremos, como seca, aumento de temperatura, baixa umidade e estresse da vegetação. Pequenos e médios produtores muitas vezes não possuem ferramentas acessíveis para acompanhar esses riscos de forma organizada.

O AgroOrbit busca resolver esse problema criando uma plataforma que centraliza dados da propriedade agrícola e permite acompanhar a saúde do talhão a partir de indicadores ambientais e satelitais.

---

# 3. Tecnologias utilizadas

## Backend

- Java 17
- Spring Boot 3.4.0
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Spring Validation
- Spring HATEOAS
- Spring Cache
- Lombok
- Maven

## Banco de dados

- H2 Database em arquivo
- JPA/Hibernate para criação e atualização automática das tabelas

## Documentação e testes

- Swagger/OpenAPI
- Postman
- H2 Console

## Deploy

- Railway
- Dockerfile
- Volume Railway para persistência do H2

---

# 4. Como rodar localmente

## 4.1 Pré-requisitos

Antes de executar o projeto, instale:

- Java 17
- Maven
- Git
- IntelliJ IDEA, VS Code ou outra IDE
- Postman, opcional

## 4.2 Clonar o projeto

```bash
git clone https://github.com/LucasViana130/GS1-JAVA.git
cd GS1-JAVA
```

## 4.3 Rodar a API

```bash
mvn spring-boot:run
```

A API será iniciada em:

```text
http://localhost:8080
```

---

# 5. Configuração do banco H2

O projeto utiliza H2 em arquivo para manter os dados entre execuções locais.

Arquivo:

```text
src/main/resources/application.properties
```

Configuração principal:

```properties
spring.datasource.url=jdbc:h2:file:./data/agroorbitdb;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
```

## 5.1 Acessar H2 Console local

URL:

```text
http://localhost:8080/h2-console
```

Configuração:

```text
JDBC URL: jdbc:h2:file:./data/agroorbitdb
User: sa
Password: deixar vazio
```

## 5.2 Acessar H2 Console no deploy

URL:

```text
https://gs1-java-production.up.railway.app/h2-console
```

Configuração:

```text
JDBC URL: jdbc:h2:file:./data/agroorbitdb
User: sa
Password: deixar vazio
```

Se necessário, no ambiente Railway também pode ser usado:

```text
jdbc:h2:file:/app/data/agroorbitdb
```

---

# 6. Usuário inicial

O projeto possui um `DataInitializer`, responsável por cadastrar dados iniciais quando o banco está vazio.

Usuário inicial:

```json
{
  "email": "lucas@agroorbit.com",
  "password": "123456"
}
```

Esse usuário pode ser usado para autenticação no Swagger e no Postman.

---

# 7. Autenticação e segurança

A API utiliza **Spring Security com JWT**.

Fluxo de autenticação:

```text
1. Usuário envia e-mail e senha em /auth/login
2. API valida as credenciais
3. API retorna um token JWT
4. O cliente envia o token nas próximas requisições
5. A API valida o token no SecurityFilter
```

## 7.1 Login

Endpoint:

```http
POST /auth/login
```

Body:

```json
{
  "email": "lucas@agroorbit.com",
  "password": "123456"
}
```

Resposta esperada:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "name": "Lucas Viana",
    "email": "lucas@agroorbit.com",
    "role": "ROLE_PRODUCER"
  }
}
```

## 7.2 Usar token nas requisições

Header:

```http
Authorization: Bearer SEU_TOKEN_AQUI
```

---

# 8. Swagger/OpenAPI

## 8.1 Swagger local

```text
http://localhost:8080/swagger-ui.html
```

ou:

```text
http://localhost:8080/swagger-ui/index.html
```

## 8.2 Swagger em produção

```text
https://gs1-java-production.up.railway.app/swagger-ui.html
```

ou:

```text
https://gs1-java-production.up.railway.app/swagger-ui/index.html
```

## 8.3 Como testar no Swagger

1. Acesse o Swagger.
2. Execute `POST /auth/login`.
3. Copie o token retornado.
4. Clique em `Authorize`.
5. Cole:

```text
Bearer SEU_TOKEN_AQUI
```

6. Teste os endpoints protegidos.

---

# 9. Principais entidades do sistema

## User

Representa o usuário da plataforma.

Campos principais:

- id
- name
- email
- password
- role
- createdAt

Papéis possíveis:

- ROLE_PRODUCER
- ROLE_ADMIN
- ROLE_TECHNICIAN

## Farm

Representa uma fazenda cadastrada por um usuário.

Campos principais:

- id
- user
- name
- owner
- city
- state
- country
- createdAt

## CropArea

Representa um talhão ou área de plantio.

Campos principais:

- id
- farm
- name
- cropType
- areaSize
- areaUnit
- location
- boundaryGeoJson
- status
- createdAt

O campo `boundaryGeoJson` armazena o polígono desenhado no mapa pelo usuário.

O campo `areaUnit` preserva a unidade da área, podendo ser:

- HA
- M2
- ACRE

## Sensor

Representa sensores agrícolas simulados.

Campos principais:

- id
- cropArea
- name
- sensorType
- status
- installationDate

## SensorReading

Representa leituras simuladas de sensores.

Campos principais:

- id
- sensor
- temperature
- airHumidity
- soilHumidity
- manualAlert
- readingDate

## SatelliteData

Representa dados satelitais simulados cadastrados na API.

Campos principais:

- id
- cropArea
- source
- ndviAverage
- ndviMin
- ndviMax
- surfaceTemperature
- cloudCoverage
- captureDate
- createdAt

## ClimateAlert

Representa alertas climáticos e de estresse gerados pela análise de risco.

Campos principais:

- id
- cropArea
- alertType
- severity
- title
- description
- status
- createdAt
- resolvedAt

## Recommendation

Representa recomendações associadas aos alertas.

Campos principais:

- id
- climateAlert
- message
- priority
- createdAt

---

# 10. Modelagem avançada

A modelagem avançada contempla os pontos solicitados na documentação da disciplina:

- múltiplas tabelas;
- herança;
- chave composta;
- Embedded;
- relacionamentos;
- segurança com JWT.

## 10.1 Múltiplas tabelas

A aplicação possui várias entidades persistidas:

- TB_USER
- TB_FARM
- TB_CROP_AREA
- TB_SENSOR
- TB_SENSOR_READING
- TB_SATELLITE_DATA
- TB_CLIMATE_ALERT
- TB_RECOMMENDATION
- TB_CROP_AREA_SATELLITE_SNAPSHOT

## 10.2 Herança

A herança foi aplicada na entidade `Sensor`.

Classe:

```text
Sensor
```

Subclasses:

```text
SoilMoistureSensor
WeatherStationSensor
```

A ideia é representar sensores especializados mantendo atributos comuns na classe base.

## 10.3 Embedded

A classe `GeoLocation` foi modelada como `@Embeddable` e utilizada dentro de `CropArea` com `@Embedded`.

Ela possui:

- latitude
- longitude

Isso permite organizar a localização central do talhão sem criar uma tabela separada.

## 10.4 Chave composta

A chave composta foi aplicada na entidade `CropAreaSatelliteSnapshot`.

Classe da chave:

```text
CropAreaSatelliteSnapshotId
```

Campos da chave:

```text
cropAreaId
captureDate
```

Essa estrutura representa um snapshot satelital de um talhão em uma data específica.

## 10.5 Relacionamentos principais

```text
User 1:N Farm
Farm 1:N CropArea
CropArea 1:N Sensor
Sensor 1:N SensorReading
CropArea 1:N SatelliteData
CropArea 1:N ClimateAlert
ClimateAlert 1:N Recommendation
CropArea 1:N CropAreaSatelliteSnapshot
```

---

# 11. Dados satelitais e NDVI

O projeto utiliza o conceito de **NDVI** como indicador de vigor da vegetação.

NDVI significa **Normalized Difference Vegetation Index**, ou Índice de Vegetação por Diferença Normalizada.

Na API, os dados são registrados na entidade `SatelliteData`.

Campos principais:

- ndviAverage
- ndviMin
- ndviMax
- surfaceTemperature
- cloudCoverage
- captureDate
- source

Fontes possíveis:

- COPERNICUS_SENTINEL_2
- SENTINEL_HUB_STATISTICAL_API
- MANUAL_IMPORT

Na entrega, os dados são simulados ou inseridos manualmente na API, representando uma futura integração com dados abertos de satélite.

---

# 12. Funcionamento da análise de risco

A análise de risco cruza informações de:

- última leitura simulada de sensor;
- último dado satelital do talhão;
- NDVI;
- temperatura;
- umidade do ar;
- umidade do solo.

Com base nesses dados, a API pode:

- alterar o status do talhão;
- criar um alerta climático;
- criar uma recomendação para o produtor.

Exemplo de situação crítica:

```text
NDVI baixo + temperatura alta + baixa umidade do solo
```

Resultado esperado:

```text
Risco de seca / estresse da vegetação
```

---

# 13. Endpoints principais

## Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/login` | Autentica usuário e retorna JWT |
| POST | `/auth/register` | Registra novo usuário |

## Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/users` | Lista usuários |
| GET | `/users/{id}` | Busca usuário por ID |
| PUT | `/users/{id}` | Atualiza usuário |
| DELETE | `/users/{id}` | Remove usuário |

## Fazendas

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/farms` | Cria fazenda |
| GET | `/farms` | Lista fazendas |
| GET | `/farms/{id}` | Busca fazenda |
| PUT | `/farms/{id}` | Atualiza fazenda |
| DELETE | `/farms/{id}` | Remove fazenda |
| GET | `/farms/{id}/crop-areas` | Lista talhões da fazenda |

## Talhões

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/crop-areas` | Cria talhão |
| GET | `/crop-areas` | Lista talhões |
| GET | `/crop-areas/{id}` | Busca talhão |
| PUT | `/crop-areas/{id}` | Atualiza talhão |
| DELETE | `/crop-areas/{id}` | Remove talhão |
| GET | `/crop-areas/{id}/sensors` | Lista sensores do talhão |
| GET | `/crop-areas/{id}/readings` | Lista leituras do talhão |
| GET | `/crop-areas/{id}/satellite-data` | Lista dados satelitais do talhão |
| GET | `/crop-areas/{id}/alerts` | Lista alertas do talhão |
| GET | `/crop-areas/{id}/recommendations` | Lista recomendações do talhão |

## Sensores

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/sensors` | Cria sensor |
| GET | `/sensors` | Lista sensores |
| GET | `/sensors/{id}` | Busca sensor |
| PUT | `/sensors/{id}` | Atualiza sensor |
| DELETE | `/sensors/{id}` | Remove sensor |
| GET | `/sensors/{id}/readings` | Lista leituras do sensor |

## Leituras de sensores

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/sensor-readings` | Cria leitura |
| GET | `/sensor-readings` | Lista leituras |
| GET | `/sensor-readings/{id}` | Busca leitura |
| DELETE | `/sensor-readings/{id}` | Remove leitura |
| GET | `/sensor-readings/latest/crop-area/{cropAreaId}` | Busca última leitura do talhão |

## Dados satelitais

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/satellite-data` | Cria dado satelital |
| GET | `/satellite-data` | Lista dados satelitais |
| GET | `/satellite-data/{id}` | Busca dado satelital |
| PUT | `/satellite-data/{id}` | Atualiza dado satelital |
| DELETE | `/satellite-data/{id}` | Remove dado satelital |
| GET | `/satellite-data/latest/crop-area/{cropAreaId}` | Busca último dado satelital do talhão |

## Alertas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/climate-alerts` | Lista alertas |
| GET | `/climate-alerts/{id}` | Busca alerta |
| GET | `/climate-alerts/open` | Lista alertas abertos |
| GET | `/climate-alerts/critical` | Lista alertas críticos |
| PUT | `/climate-alerts/{id}/resolve` | Resolve alerta |

## Recomendações

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/recommendations` | Lista recomendações |
| GET | `/recommendations/{id}` | Busca recomendação |
| GET | `/alerts/{id}/recommendations` | Lista recomendações de um alerta |
| GET | `/crop-areas/{id}/recommendations` | Lista recomendações de um talhão |

## Análise de risco

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/risk-analysis/{cropAreaId}` | Executa análise de risco do talhão |

## Dashboard

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/dashboard` | Retorna resumo geral da plataforma |

---
### Observação sobre exclusões

Os endpoints de exclusão de fazendas, talhões e sensores utilizam exclusão controlada na camada de service com `@Transactional`.

Essa regra foi adicionada para evitar erro de integridade referencial quando existem dados relacionados, como leituras de sensores, dados satelitais, alertas e recomendações.

Fluxo adotado:

```text
DELETE /sensors/{id}
SensorReading → Sensor

DELETE /crop-areas/{id}
Recommendation → ClimateAlert → SensorReading → Sensor → SatelliteData → CropArea

DELETE /farms/{id}
Dados dos talhões → CropArea → Farm
```
---

# 14. Exemplos de requisições

## 14.1 Criar fazenda

```json
{
  "userId": 1,
  "name": "Fazenda Railway",
  "owner": "Lucas Viana",
  "city": "São Paulo",
  "state": "SP",
  "country": "Brasil"
}
```

## 14.2 Criar talhão com GeoJSON

```json
{
  "farmId": 1,
  "name": "Talhão A",
  "cropType": "Milho",
  "areaSize": 4.5,
  "areaUnit": "HA",
  "latitude": -23.55052,
  "longitude": -46.633308,
  "boundaryGeoJson": "{\"type\":\"Polygon\",\"coordinates\":[[[-46.634,-23.551],[-46.632,-23.551],[-46.632,-23.549],[-46.634,-23.549],[-46.634,-23.551]]]}",
  "status": "NORMAL"
}
```

## 14.3 Criar sensor

```json
{
  "cropAreaId": 1,
  "name": "Sensor Climático",
  "sensorType": "WEATHER_STATION",
  "status": "ACTIVE",
  "installationDate": "2026-06-01"
}
```

## 14.4 Criar leitura simulada

```json
{
  "sensorId": 1,
  "temperature": 35.2,
  "airHumidity": 27.5,
  "soilHumidity": 22,
  "manualAlert": false
}
```

## 14.5 Criar dado satelital

```json
{
  "cropAreaId": 1,
  "source": "SENTINEL_HUB_STATISTICAL_API",
  "ndviAverage": 0.38,
  "ndviMin": 0.25,
  "ndviMax": 0.61,
  "surfaceTemperature": 36.5,
  "cloudCoverage": 18,
  "captureDate": "2026-06-02"
}
```

---

# 15. Testes com Postman

A collection Postman da API está disponível no repositório.

Ordem recomendada:

```text
1. Login usuário inicial
2. Usuários
3. Dashboard
4. Fazendas
5. Talhões / Áreas de plantio
6. Sensores
7. Leituras simuladas de sensores
8. Dados satelitais / NDVI
9. Análise de risco
10. Alertas climáticos
11. Recomendações
```

Antes de executar, configure a variável:

```text
baseUrl = https://gs1-java-production.up.railway.app
```

ou localmente:

```text
baseUrl = http://localhost:8080
```

---

# 16. Testes com Swagger

Fluxo recomendado:

```text
1. POST /auth/login
2. Copiar token
3. Clicar em Authorize
4. Informar Bearer token
5. GET /users
6. GET /dashboard
7. POST /farms
8. POST /crop-areas
9. POST /sensors
10. POST /sensor-readings
11. POST /satellite-data
12. POST /risk-analysis/{cropAreaId}
13. GET /climate-alerts
14. GET /recommendations
```

---

# 17. Cache

A aplicação utiliza Spring Cache em operações de consulta e limpeza de cache em operações de escrita.

Exemplos de uso:

- `@Cacheable` em buscas e listagens;
- `@CacheEvict` em criação, atualização e exclusão.

Isso melhora a organização da camada de serviço e atende ao requisito de utilização de cache.

---

# 18. HATEOAS

As respostas da API utilizam HATEOAS em responses específicas, adicionando links úteis de navegação entre recursos.

Exemplo conceitual:

```json
{
  "id": 1,
  "name": "Talhão A",
  "_links": {
    "self": {
      "href": "http://localhost:8080/crop-areas/1"
    },
    "crop-areas": {
      "href": "http://localhost:8080/crop-areas"
    }
  }
}
```

---

# 19. Validações e tratamento de erros

A API utiliza Bean Validation para validar dados de entrada.

Exemplos:

- campos obrigatórios com `@NotNull`;
- textos obrigatórios com `@NotBlank`;
- valores positivos com `@Positive`;
- limites numéricos para leituras e NDVI.

A API também possui tratamento global de exceções com respostas padronizadas.

Exemplo:

```json
{
  "timestamp": "2026-06-02T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso não encontrado",
  "path": "/crop-areas/999999",
  "details": []
}
```

---

# 20. Deploy no Railway

A API foi publicada no Railway com Docker.

## 20.1 Dockerfile

O projeto possui um `Dockerfile` na raiz.

Fluxo:

```text
Maven build → JAR → Container Java 17 → Railway
```

## 20.2 Porta

A aplicação usa a variável `PORT` fornecida pelo Railway:

```properties
server.port=${PORT:8080}
server.address=0.0.0.0
```

## 20.3 Volume

O H2 em arquivo utiliza volume Railway montado em:

```text
/app/data
```

Como a URL do banco é:

```properties
jdbc:h2:file:./data/agroorbitdb
```

dentro do container o banco é salvo em:

```text
/app/data/agroorbitdb
```

---

# 21. Estrutura de pacotes

Estrutura principal:

```text
src/main/java/br/com/fiap/agroorbit
├── config
├── controllers
├── dtos
│   ├── request
│   └── response
├── exceptions
├── models
│   ├── embedded
│   └── enums
├── repositories
├── security
└── services
```
---
