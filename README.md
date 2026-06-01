# AgroOrbit API - Global Solution 2026/1

API REST desenvolvida em **Java 17 + Spring Boot 3.4.6** para a solução **AgroOrbit**, uma plataforma de monitoramento agrícola inteligente que usa dados abertos de satélite, leituras simuladas de sensores de campo e análise de risco para gerar alertas e recomendações ao produtor rural.

## Equipe

| Nome | RM |
|------|----|
| Lucas Viana | 563254 |
| Integrante 2 | RM |
| Integrante 3 | RM |

**Turma:** 2TDS

---

## Sobre a solução

A AgroOrbit conecta o tema da Economia Espacial ao agronegócio. A API registra fazendas, talhões, sensores, leituras simuladas de campo, dados satelitais como NDVI, alertas climáticos e recomendações agrícolas.

O funcionamento principal é:

```text
Produtor cadastra fazenda e talhões
        ↓
Sistema registra dados satelitais do talhão, como NDVI
        ↓
Sistema registra leituras simuladas de sensores de campo
        ↓
API cruza dados de satélite + sensores
        ↓
Sistema gera alertas de seca, queimada ou estresse vegetativo
        ↓
Front-end/mobile exibe alertas e recomendações
```

No MVP, as leituras de sensores são simuladas e cadastradas via API/Swagger/Postman. A disciplina de IoT possui um protótipo separado em ESP32/Wokwi, mas a API Java não depende do Wokwi online para funcionar.

---

## Tecnologias utilizadas

- Java 17
- Spring Boot 3.4.6
- Spring Web
- Spring Data JPA
- H2 Database
- Spring Validation
- Spring HATEOAS
- Spring Security
- JWT com `java-jwt`
- Spring Cache com `@Cacheable` e `@CacheEvict`
- Swagger/OpenAPI com SpringDoc
- Lombok
- Maven

---

## Arquitetura do projeto

O projeto segue o padrão de camadas usado no projeto base:

```text
src/main/java/br/com/fiap/agroorbit/
├── config/
├── controllers/
├── dtos/
│   ├── request/
│   └── response/
├── exceptions/
├── models/
│   ├── embedded/
│   └── enums/
├── repositories/
├── security/
├── services/
└── AgroOrbitApplication.java
```

### Camadas

- **Controllers:** expõem os endpoints REST.
- **Services:** concentram regras de negócio, CRUDs, análise de risco e conversão para DTOs.
- **Repositories:** acesso ao banco via Spring Data JPA.
- **Models:** entidades JPA da AgroOrbit.
- **DTOs:** records de request e classes de response com HATEOAS.
- **Security:** autenticação/autorização com JWT.
- **Exceptions:** tratamento global de erros.

---

## Modelagem avançada atendida

A entrega de Java pede modelagem avançada com herança, chave composta, Embedded e múltiplas tabelas. O projeto atende assim:

| Requisito | Implementação |
|---|---|
| Múltiplas tabelas | User, Farm, CropArea, Sensor, SensorReading, SatelliteData, ClimateAlert, Recommendation e CropAreaSatelliteSnapshot |
| Embedded | `GeoLocation` embutido em `CropArea`, usando latitude e longitude |
| Chave composta | `CropAreaSatelliteSnapshotId`, com `cropAreaId + captureDate` |
| Herança | `Sensor` como classe base, com `WeatherStationSensor`, `SoilMoistureSensor`, `TemperatureSensor` e `HumiditySensor` |
| Segurança | Spring Security + JWT protegendo endpoints |


---

## Cache

O projeto utiliza Spring Cache para evitar consultas repetidas em listagens e buscas por ID. O cache foi habilitado com `@EnableCaching` na classe principal da aplicação.

Foram aplicados:

- `@Cacheable` em consultas como listagem, busca por ID, dashboard, alertas abertos, alertas críticos e dados relacionados por talhão.
- `@CacheEvict` em operações que alteram dados, como criar, atualizar, excluir, resolver alerta e executar análise de risco.

Essa estratégia mantém os dados atualizados após operações de escrita e demonstra o uso de cache exigido no padrão do Challenge.

---

## Banco de dados

A API usa H2 em memória para desenvolvimento e avaliação rápida.

Acesse o console:

```text
http://localhost:8080/h2-console
```

Configuração:

```text
JDBC URL: jdbc:h2:mem:agroorbitdb
User: sa
Password: deixe em branco
```

O projeto já possui um `DataInitializer` com dados iniciais:

```text
Usuário: lucas@agroorbit.com
Senha: 123456
Role: ROLE_PRODUCER
```

---

## Como executar

### 1. Clonar o repositório

```bash
git clone <url-do-repositorio>
cd agroorbit-java
```

### 2. Rodar o projeto

```bash
mvn spring-boot:run
```

### 3. Acessar o Swagger

```text
http://localhost:8080/swagger-ui.html
```

### 4. Fazer login

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

Copie o token retornado e clique em **Authorize** no Swagger usando:

```text
Bearer SEU_TOKEN_AQUI
```

---

## Principais endpoints

### Autenticação

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/auth/register` | Criar usuário |
| POST | `/auth/login` | Login e geração do JWT |

### Usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/users` | Listar usuários |
| GET | `/users/{id}` | Buscar usuário |
| PUT | `/users/{id}` | Atualizar usuário |
| DELETE | `/users/{id}` | Remover usuário |

### Dashboard

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/dashboard` | Resumo geral para o front-end |

### Fazendas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/farms` | Listar fazendas |
| GET | `/farms/{id}` | Buscar fazenda |
| POST | `/farms` | Criar fazenda |
| PUT | `/farms/{id}` | Atualizar fazenda |
| DELETE | `/farms/{id}` | Remover fazenda |
| GET | `/farms/{id}/crop-areas` | Listar talhões da fazenda |

### Talhões

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/crop-areas` | Listar talhões |
| GET | `/crop-areas/{id}` | Buscar talhão |
| POST | `/crop-areas` | Criar talhão |
| PUT | `/crop-areas/{id}` | Atualizar talhão |
| DELETE | `/crop-areas/{id}` | Remover talhão |
| GET | `/crop-areas/{id}/sensors` | Sensores do talhão |
| GET | `/crop-areas/{id}/readings` | Leituras do talhão |
| GET | `/crop-areas/{id}/satellite-data` | Dados satelitais do talhão |
| GET | `/crop-areas/{id}/alerts` | Alertas do talhão |
| GET | `/crop-areas/{id}/recommendations` | Recomendações do talhão |

### Sensores e leituras simuladas

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/sensors` | Listar sensores |
| POST | `/sensors` | Criar sensor |
| GET | `/sensors/{id}/readings` | Leituras de um sensor |
| GET | `/sensor-readings` | Listar leituras |
| POST | `/sensor-readings` | Cadastrar leitura simulada |

### Dados satelitais

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/satellite-data` | Listar dados satelitais |
| POST | `/satellite-data` | Cadastrar dado de NDVI |
| GET | `/satellite-data/{id}` | Buscar dado satelital |

### Alertas e recomendações

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/climate-alerts` | Listar alertas |
| GET | `/climate-alerts/open` | Alertas abertos |
| GET | `/climate-alerts/critical` | Alertas críticos |
| PUT | `/climate-alerts/{id}/resolve` | Resolver alerta |
| GET | `/recommendations` | Listar recomendações |
| GET | `/recommendations/alert/{alertId}` | Recomendações por alerta |

### Análise de risco

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/risk-analysis/{cropAreaId}` | Cruza NDVI + leituras simuladas e gera alerta/recomendação |

---

## Exemplo de teste completo

### 1. Login

```json
{
  "email": "lucas@agroorbit.com",
  "password": "123456"
}
```

### 2. Criar leitura simulada

```http
POST /sensor-readings
```

```json
{
  "sensorId": 1,
  "temperature": 35.2,
  "airHumidity": 27.5,
  "soilHumidity": 22.0,
  "manualAlert": false
}
```

### 3. Criar dado satelital

```http
POST /satellite-data
```

```json
{
  "cropAreaId": 1,
  "source": "SENTINEL_HUB_STATISTICAL_API",
  "ndviAverage": 0.38,
  "ndviMin": 0.25,
  "ndviMax": 0.61,
  "surfaceTemperature": 36.5,
  "cloudCoverage": 18,
  "captureDate": "2026-06-01"
}
```

### 4. Rodar análise de risco

```http
POST /risk-analysis/1
```

Resposta esperada:

```json
{
  "cropAreaId": 1,
  "cropAreaName": "Talhão A",
  "status": "DROUGHT_RISK",
  "alertGenerated": true,
  "alertType": "DROUGHT",
  "severity": "HIGH",
  "recommendation": "Verificar irrigação nas próximas horas e acompanhar novo NDVI."
}
```

### 5. Consultar alertas

```http
GET /crop-areas/1/alerts
```

---

## Regras de negócio da análise de risco

### Risco de seca

```text
NDVI < 0.45
+ umidade do solo < 30
+ temperatura > 32°C
= alerta de seca
```

### Risco de queimada

```text
temperatura > 35°C
+ umidade do ar < 30
+ NDVI < 0.40
= alerta de risco de queimada
```

### Estresse vegetativo

```text
NDVI < 0.45
= alerta de estresse vegetativo
```

---

## Integração com front-end/mobile

A API já possui endpoints úteis para o front-end:

- `/dashboard` para resumo geral;
- `/crop-areas/{id}/satellite-data` para NDVI;
- `/crop-areas/{id}/readings` para leituras simuladas;
- `/crop-areas/{id}/alerts` para alertas;
- `/crop-areas/{id}/recommendations` para recomendações;
- `/risk-analysis/{cropAreaId}` para gerar análise.

---

## Observação sobre IoT

A API Java não depende do simulador IoT estar online. As leituras de sensores são cadastradas e persistidas diretamente na API, garantindo que o front-end e o backend publicados funcionem de forma estável. O protótipo IoT em ESP32/Wokwi demonstra como esses dados seriam coletados em campo em uma evolução real da solução.
