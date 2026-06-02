# Guia detalhado de testes via Swagger — AgroOrbit API Java

Este guia foi feito para testar a API AgroOrbit pelo Swagger sem depender do Postman.

## 1. Antes de começar

Confirme que a API está rodando.

No terminal, dentro da pasta do projeto:

```bash
mvn spring-boot:run
```

Quando aparecer algo parecido com:

```text
Tomcat started on port 8080
Started AgroOrbitApplication
```

acesse o Swagger:

```text
http://localhost:8080/swagger-ui.html
```

ou:

```text
http://localhost:8080/swagger-ui/index.html
```

## 2. Conferir o H2

Acesse:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:file:./data/agroorbitdb
User: sa
Password: deixe vazio
```

Depois rode:

```sql
SELECT * FROM TB_USER;
SELECT * FROM TB_FARM;
SELECT * FROM TB_CROP_AREA;
```

Se o `DataInitializer` rodou corretamente, deve existir um usuário inicial.

## 3. Usuário inicial para login

Use o e-mail configurado no seu banco.

No último teste, você estava usando:

```text
teste.editado1780357062@agroorbit00.com
```

Senha:

```text
123456
```

Se esse e-mail não existir no H2, use o usuário inicial do `DataInitializer`, normalmente:

```text
lucas@agroorbit.com
```

Senha:

```text
123456
```

## 4. Fazer login no Swagger

No Swagger, procure:

```text
POST /auth/login
```

Clique em:

```text
Try it out
```

Use o JSON:

```json
{
  "email": "teste.editado1780357062@agroorbit00.com",
  "password": "123456"
}
```

Se esse usuário não existir, use:

```json
{
  "email": "lucas@agroorbit.com",
  "password": "123456"
}
```

Clique em:

```text
Execute
```

A resposta deve retornar `200 OK` com um token:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Copie o token inteiro.

## 5. Autorizar o Swagger

No topo da página do Swagger, clique em:

```text
Authorize
```

Cole o token com `Bearer` antes:

```text
Bearer SEU_TOKEN_AQUI
```

Exemplo:

```text
Bearer eyJhbGciOiJIUzI1NiJ9...
```

Clique em:

```text
Authorize
```

Depois clique em:

```text
Close
```

Agora os endpoints protegidos devem funcionar.

## 6. Testar usuários

### 6.1 Listar usuários

Endpoint:

```text
GET /users
```

Resultado esperado:

```text
200 OK
```

Deve retornar uma lista de usuários.

### 6.2 Buscar usuário por ID

Endpoint:

```text
GET /users/{id}
```

Use:

```text
1
```

Resultado esperado:

```text
200 OK
```

### 6.3 Editar usuário

Endpoint:

```text
PUT /users/{id}
```

Use um ID existente.

Exemplo:

```json
{
  "name": "Lucas Viana",
  "email": "teste.editado1780357062@agroorbit00.com",
  "role": "ROLE_PRODUCER"
}
```

Resultado esperado:

```text
200 OK
```

Atenção: se trocar o e-mail do usuário logado, faça login novamente com o novo e-mail para gerar outro token.

## 7. Testar dashboard

Endpoint:

```text
GET /dashboard
```

Resultado esperado:

```text
200 OK
```

Esse endpoint deve retornar um resumo do sistema, como fazendas, talhões, sensores, alertas e dados recentes.

## 8. Testar fazendas

### 8.1 Criar fazenda

Endpoint:

```text
POST /farms
```

JSON:

```json
{
  "userId": 1,
  "name": "Fazenda Swagger",
  "owner": "Lucas Viana",
  "city": "São Paulo",
  "state": "SP",
  "country": "Brasil"
}
```

Resultado esperado:

```text
200 OK ou 201 Created
```

Guarde o `id` retornado. Ele será o `farmId`.

### 8.2 Listar fazendas

Endpoint:

```text
GET /farms
```

Resultado esperado:

```text
200 OK
```

### 8.3 Buscar fazenda por ID

Endpoint:

```text
GET /farms/{id}
```

Use o `farmId` criado.

### 8.4 Editar fazenda

Endpoint:

```text
PUT /farms/{id}
```

JSON:

```json
{
  "userId": 1,
  "name": "Fazenda Swagger Editada",
  "owner": "Lucas Viana",
  "city": "Campinas",
  "state": "SP",
  "country": "Brasil"
}
```

Resultado esperado:

```text
200 OK
```

## 9. Testar talhões

### 9.1 Criar talhão com polígono GeoJSON

Endpoint:

```text
POST /crop-areas
```

JSON:

```json
{
  "farmId": 1,
  "name": "Talhão Swagger",
  "cropType": "Milho",
  "areaSize": 4.5,
  "areaUnit": "HA",
  "latitude": -23.55052,
  "longitude": -46.633308,
  "boundaryGeoJson": "{\"type\":\"Polygon\",\"coordinates\":[[[-46.634,-23.551],[-46.632,-23.551],[-46.632,-23.549],[-46.634,-23.549],[-46.634,-23.551]]]}",
  "status": "NORMAL"
}
```

Se você criou uma fazenda nova, troque:

```json
"farmId": 1
```

pelo ID da fazenda criada.

Resultado esperado:

```text
200 OK ou 201 Created
```

Guarde o `id`. Ele será o `cropAreaId`.

### 9.2 Listar talhões

Endpoint:

```text
GET /crop-areas
```

Resultado esperado:

```text
200 OK
```

### 9.3 Buscar talhão

Endpoint:

```text
GET /crop-areas/{id}
```

Use o `cropAreaId`.

Confira se a resposta tem:

```json
"areaUnit": "HA"
```

e:

```json
"boundaryGeoJson": "..."
```

### 9.4 Editar talhão

Endpoint:

```text
PUT /crop-areas/{id}
```

JSON:

```json
{
  "farmId": 1,
  "name": "Talhão Swagger Editado",
  "cropType": "Soja",
  "areaSize": 12000,
  "areaUnit": "M2",
  "latitude": -23.55052,
  "longitude": -46.633308,
  "boundaryGeoJson": "{\"type\":\"Polygon\",\"coordinates\":[[[-46.635,-23.552],[-46.631,-23.552],[-46.631,-23.548],[-46.635,-23.548],[-46.635,-23.552]]]}",
  "status": "ATTENTION"
}
```

Resultado esperado:

```text
200 OK
```

## 10. Testar sensores

### 10.1 Criar sensor

Endpoint:

```text
POST /sensors
```

JSON:

```json
{
  "cropAreaId": 1,
  "name": "Sensor Swagger",
  "sensorType": "WEATHER_STATION",
  "status": "ACTIVE",
  "installationDate": "2026-06-01"
}
```

Troque `cropAreaId` pelo ID do talhão criado.

Resultado esperado:

```text
200 OK ou 201 Created
```

Guarde o `id`. Ele será o `sensorId`.

### 10.2 Listar sensores

Endpoint:

```text
GET /sensors
```

Resultado esperado:

```text
200 OK
```

### 10.3 Buscar sensor por ID

Endpoint:

```text
GET /sensors/{id}
```

Use o `sensorId`.

## 11. Testar leituras simuladas de sensores

Atenção: o campo `manualAlert` deve ser boolean, não `"N"` ou `"Y"`.

### 11.1 Criar leitura normal

Endpoint:

```text
POST /sensor-readings
```

JSON:

```json
{
  "sensorId": 1,
  "temperature": 28.5,
  "airHumidity": 55,
  "soilHumidity": 60,
  "manualAlert": false
}
```

Troque `sensorId` pelo ID do sensor criado.

Resultado esperado:

```text
200 OK ou 201 Created
```

### 11.2 Criar leitura crítica

Endpoint:

```text
POST /sensor-readings
```

JSON:

```json
{
  "sensorId": 1,
  "temperature": 35.2,
  "airHumidity": 27.5,
  "soilHumidity": 22,
  "manualAlert": false
}
```

Resultado esperado:

```text
200 OK ou 201 Created
```

### 11.3 Listar leituras

Endpoint:

```text
GET /sensor-readings
```

Resultado esperado:

```text
200 OK
```

### 11.4 Buscar última leitura do talhão

Endpoint:

```text
GET /sensor-readings/latest/crop-area/{cropAreaId}
```

Use o ID do talhão.

## 12. Testar dados satelitais / NDVI

### 12.1 Criar dado satelital saudável

Endpoint:

```text
POST /satellite-data
```

JSON:

```json
{
  "cropAreaId": 1,
  "source": "SENTINEL_HUB_STATISTICAL_API",
  "ndviAverage": 0.72,
  "ndviMin": 0.61,
  "ndviMax": 0.88,
  "surfaceTemperature": 30.5,
  "cloudCoverage": 15,
  "captureDate": "2026-06-01"
}
```

Troque `cropAreaId` pelo ID do talhão.

### 12.2 Criar dado satelital crítico

Endpoint:

```text
POST /satellite-data
```

JSON:

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

Resultado esperado:

```text
200 OK ou 201 Created
```

### 12.3 Buscar último dado satelital do talhão

Endpoint:

```text
GET /satellite-data/latest/crop-area/{cropAreaId}
```

Resultado esperado:

```text
200 OK
```

## 13. Testar análise de risco

Depois de criar:

- uma leitura crítica;
- um dado satelital crítico;

execute:

```text
POST /risk-analysis/{cropAreaId}
```

Resultado esperado:

```text
200 OK ou 201 Created
```

A resposta deve indicar risco, alerta ou recomendação.

Depois disso, teste:

```text
GET /crop-areas/{cropAreaId}/alerts
```

e:

```text
GET /crop-areas/{cropAreaId}/recommendations
```

## 14. Testar alertas

### 14.1 Listar alertas

Endpoint:

```text
GET /climate-alerts
```

Resultado esperado:

```text
200 OK
```

### 14.2 Listar alertas abertos

Endpoint:

```text
GET /climate-alerts/open
```

### 14.3 Listar alertas críticos

Endpoint:

```text
GET /climate-alerts/critical
```

### 14.4 Resolver alerta

Pegue um ID de alerta e execute:

```text
PUT /climate-alerts/{id}/resolve
```

Resultado esperado:

```text
200 OK
```

O alerta deve voltar com:

```json
"status": "RESOLVED"
```

## 15. Testar recomendações

### 15.1 Listar recomendações

Endpoint:

```text
GET /recommendations
```

Resultado esperado:

```text
200 OK
```

### 15.2 Buscar recomendações do talhão

Endpoint:

```text
GET /crop-areas/{cropAreaId}/recommendations
```

### 15.3 Buscar recomendações do alerta

Endpoint:

```text
GET /alerts/{alertId}/recommendations
```

## 16. Testes de erro

### 16.1 Login inválido

Endpoint:

```text
POST /auth/login
```

JSON:

```json
{
  "email": "usuario@inexistente.com",
  "password": "senhaerrada"
}
```

Resultado esperado:

```text
401 Unauthorized
```

### 16.2 Buscar recurso inexistente

Endpoint:

```text
GET /crop-areas/999999
```

Resultado esperado:

```text
404 Not Found
```

### 16.3 Criar talhão inválido

Endpoint:

```text
POST /crop-areas
```

JSON:

```json
{
  "farmId": 1,
  "name": "",
  "cropType": "Milho",
  "areaSize": 4.5,
  "areaUnit": "HA",
  "status": "NORMAL"
}
```

Resultado esperado:

```text
400 Bad Request
```

## 17. Conferir dados no H2

No H2 Console, rode:

```sql
SELECT * FROM TB_USER;
SELECT * FROM TB_FARM;
SELECT * FROM TB_CROP_AREA;
SELECT * FROM TB_SENSOR;
SELECT * FROM TB_SENSOR_READING;
SELECT * FROM TB_SATELLITE_DATA;
SELECT * FROM TB_CLIMATE_ALERT;
SELECT * FROM TB_RECOMMENDATION;
```

Para conferir o polígono e a unidade de área:

```sql
SELECT ID_CROP_AREA, NM_NAME, NR_AREA_SIZE, DS_AREA_UNIT, DS_BOUNDARY_GEOJSON
FROM TB_CROP_AREA;
```
