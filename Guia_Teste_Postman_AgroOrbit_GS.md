# Guia detalhado de testes via Postman — AgroOrbit API Java

Este guia foi feito para testar a API AgroOrbit usando a collection .


## 1. Rodar a API

Dentro do projeto Java:

```bash
mvn spring-boot:run
```

Confirme que a API subiu na porta 8080:

```text
Tomcat started on port 8080
```

## 2. Importar a collection

No Postman:

1. Clique em `Import`.
2. Selecione o arquivo `.json` da collection.
3. Aguarde aparecer a collection:

```text
AgroOrbit API - Testes completos GS Java
```

## 3. Conferir variáveis

Clique na collection e abra a aba:

```text
Variables
```

Confira:

```text
baseUrl = http://localhost:8080
mainUserEmail = teste.editado1780357062@agroorbit00.com
mainUserPassword = 123456
token = vazio inicialmente
```

Se quiser usar outro usuário, altere `mainUserEmail` e salve.

## 4. Atenção ao usuário de login

O e-mail configurado precisa existir no H2.

E-mail usado:

```text
teste.editado1780357062@agroorbit00.com
```

Senha:

```text
123456
```

Se esse usuário não existir, use o usuário do `DataInitializer`, normalmente:

```text
lucas@agroorbit.com
```

Senha:

```text
123456
```

Ou crie um usuário no endpoint:

```text
POST /auth/register
```

## 5. Login e token

Execute primeiro:

```text
01 - Autenticação > Login usuário inicial - salva token
```

Resultado esperado:

```text
200 OK
```

A resposta deve conter um token.

O script do Postman salva automaticamente o token em:

```text
token
```

Depois disso, as próximas requisições usam:

```text
Authorization: Bearer {{token}}
```

## 6. Se der 403

Se algum endpoint retornar:

```text
403 Forbidden
```

faça esta verificação:

1. Volte no request de login.
2. Execute novamente.
3. Abra o request que deu erro.
4. Vá em `Headers`.
5. Confira se aparece:

```text
Authorization: Bearer eyJ...
```

Se aparecer:

```text
Authorization: Bearer {{token}}
```

ou:

```text
Authorization: Bearer
```

o token não foi salvo ou o environment está sobrescrevendo a variável.

Solução rápida:

- execute o login novamente;
- confira se a variável `token` está preenchida;
- ou cole o token manualmente para confirmar.

## 7. Ordem recomendada de execução

Execute as pastas nesta ordem:

```text
01 - Autenticação
02 - Usuários
03 - Dashboard
04 - Fazendas
05 - Talhões / Áreas de plantio
06 - Sensores
07 - Leituras simuladas de sensores
08 - Dados satelitais / NDVI
09 - Análise de risco
10 - Alertas climáticos
11 - Recomendações
```

A pasta:

```text
12 - Limpeza opcional
```

deve ser executada só no final, se quiser apagar dados de teste.

## 8. Testar autenticação

### 8.1 Login válido

Request:

```text
01 - Autenticação > Login usuário inicial - salva token
```

Resultado esperado:

```text
200 OK
```

### 8.2 Login inválido

Request:

```text
01 - Autenticação > Login inválido - deve retornar 401
```

Resultado esperado:

```text
401 Unauthorized
```

Esse teste não deve ser 500. Se der 500, o tratamento de exceção do login está incorreto.

## 9. Testar usuários

Execute:

```text
02 - Usuários > Listar usuários
```

Resultado esperado:

```text
200 OK
```

Depois:

```text
02 - Usuários > Buscar usuário inicial por ID 1
```

Resultado esperado:

```text
200 OK
```

Para editar usuário:

```text
02 - Usuários > Editar usuário de teste
```

Atenção: se você editar o e-mail do usuário usado no login, será necessário logar novamente com o novo e-mail.

## 10. Testar dashboard

Execute:

```text
03 - Dashboard > Resumo do dashboard
```

Resultado esperado:

```text
200 OK
```

Esse teste confirma que o token está funcionando e que a API consegue buscar dados consolidados.

## 11. Testar fazendas

Execute em ordem:

```text
04 - Fazendas > Criar fazenda
04 - Fazendas > Listar fazendas
04 - Fazendas > Buscar fazenda por ID
04 - Fazendas > Editar fazenda
04 - Fazendas > Listar talhões da fazenda
```

O request `Criar fazenda` salva automaticamente o ID criado na variável:

```text
farmId
```

Os próximos requests usam essa variável.

## 12. Testar talhões

Execute em ordem:

```text
05 - Talhões / Áreas de plantio > Criar talhão com boundaryGeoJson e areaUnit
05 - Talhões / Áreas de plantio > Listar talhões
05 - Talhões / Áreas de plantio > Buscar talhão por ID
05 - Talhões / Áreas de plantio > Editar talhão
```

O request de criação envia:

```json
{
  "areaUnit": "HA",
  "boundaryGeoJson": "{\"type\":\"Polygon\",\"coordinates\":[...]}"
}
```

O ID criado é salvo em:

```text
cropAreaId
```

Confirme na resposta se aparecem:

```text
areaUnit
boundaryGeoJson
```

## 13. Testar sensores

Execute:

```text
06 - Sensores > Criar sensor
06 - Sensores > Listar sensores
06 - Sensores > Buscar sensor por ID
06 - Sensores > Editar sensor
```

O ID criado é salvo em:

```text
sensorId
```

## 14. Testar leituras simuladas

Execute:

```text
07 - Leituras simuladas de sensores > Criar leitura normal
07 - Leituras simuladas de sensores > Criar leitura crítica para risco de seca
```

Atenção: o campo `manualAlert` precisa ser boolean:

```json
"manualAlert": false
```

Não use:

```json
"manualAlert": "N"
```

Isso causa erro de conversão.

Depois execute:

```text
07 - Leituras simuladas de sensores > Listar leituras
07 - Leituras simuladas de sensores > Última leitura do talhão
```

## 15. Testar dados satelitais

Execute:

```text
08 - Dados satelitais / NDVI > Criar dado satelital saudável
08 - Dados satelitais / NDVI > Criar dado satelital crítico para risco
08 - Dados satelitais / NDVI > Listar dados satelitais
08 - Dados satelitais / NDVI > Último dado satelital do talhão
```

O dado crítico deve usar NDVI baixo:

```json
"ndviAverage": 0.38
```

## 16. Testar análise de risco

Depois de criar leitura crítica e dado satelital crítico, execute:

```text
09 - Análise de risco > Executar análise de risco do talhão
```

Resultado esperado:

```text
200 OK ou 201 Created
```

Esse teste deve gerar ou retornar alerta/recomendação.

Depois execute:

```text
10 - Alertas climáticos > Listar alertas
11 - Recomendações > Listar recomendações
```

## 17. Testar alertas

Execute:

```text
10 - Alertas climáticos > Listar alertas
10 - Alertas climáticos > Listar alertas abertos
10 - Alertas climáticos > Listar alertas críticos
10 - Alertas climáticos > Buscar alerta por ID
10 - Alertas climáticos > Resolver alerta
```

O request `Resolver alerta` deve mudar o status para:

```text
RESOLVED
```

## 18. Testar recomendações

Execute:

```text
11 - Recomendações > Listar recomendações
11 - Recomendações > Buscar recomendação por ID
11 - Recomendações > Recomendações do alerta
11 - Recomendações > Recomendações do talhão
```

## 19. Testes de erro

A collection também possui testes de erro, como:

```text
Login inválido
Buscar usuário inexistente
Buscar fazenda inexistente
Criar talhão inválido sem nome
Criar sensor inválido
Criar leitura inválida
Criar NDVI inválido
Executar análise de risco de talhão inexistente
Resolver alerta inexistente
```

Esses testes comprovam validação e tratamento de exceções.

## 20. Conferir no H2 Console

Depois dos testes, acesse:

```text
http://localhost:8080/h2-console
```

Use:

```text
JDBC URL: jdbc:h2:file:./data/agroorbitdb
User: sa
Password: vazio
```

Rode:

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

Para verificar o mapa:

```sql
SELECT ID_CROP_AREA, NM_NAME, DS_AREA_UNIT, DS_BOUNDARY_GEOJSON
FROM TB_CROP_AREA;
```

## 21. Problemas comuns

### 21.1 403 em endpoints protegidos

Causa: token não foi enviado.

Correção:

- rode o login válido;
- confira `Authorization: Bearer eyJ...`.

### 21.2 500 em leitura simulada

Causa: `manualAlert` como string.

Errado:

```json
"manualAlert": "N"
```

Correto:

```json
"manualAlert": false
```

### 21.3 Login 401

Causa: e-mail ou senha não existem no H2.

Correção:

- confira o usuário no H2;
- use o e-mail correto;
- ou apague a pasta `data/` para recriar o banco com o `DataInitializer`.

### 21.4 IDs errados

Se algum request buscar ID inexistente, execute novamente a criação do recurso anterior.

Exemplo:

- se sensor não existe, rode `Criar sensor`;
- se talhão não existe, rode `Criar talhão`;
- se fazenda não existe, rode `Criar fazenda`.


