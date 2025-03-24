# Taxes API

## Configuração e Execução

1. Clone o repositório: `git clone https://github.com/alexmaiazup/catalisa-desafio-modulo-7.git`

2. Instale as dependências: `mvn clean install`

3. Configure as variáveis de ambiente em um arquivo `.env`:
	* `DB_HOST=` (endereço do servidor de banco de dados)
	* `DB_PORT=` (porta do servidor de banco de dados)
	* `DB_NAME=` (nome do banco de dados)
	* `DB_USERNAME=` (usuário do banco de dados)
	* `DB_PASSWORD=` (senha do banco de dados)
	* `SERVER_PORT=` (porta do servidor da API)

4. Execute a API: `mvn spring-boot:run`

**Observação**: Certifique-se de que o banco de dados Postgres esteja instalado e configurado corretamente no seu sistema. Além disso, verifique se as variáveis de ambiente estão corretas e se o banco de dados está acessível.

## Rodando os Testes

1. Execute os testes unitários: `mvn test`

## Gerando Tokens JWT

**A API utiliza o Swagger para documentação. Para acessar o Swagger e visualizar todas as rotas e Request Body necessários, basta rodar a aplicação e acessar a rota `/swagger-ui.html`.**

1. Registre um novo usuário via o endpoint `/user/register`.
   - roles: `ROLE_ADMIN`, `ROLE_DEFAULT`

2. Faça login via o endpoint `/user/login` para obter o token JWT.

3. Coloque o token JWT no header das requisições como `Bearer Token`.

### Endpoints

* **GET /tipos**: Retorna uma lista de todos os tipos de impostos

* **GET /tipos/{id}**: Retorna um tipo de imposto específico pelo ID

* **POST /tipos**: Cria um novo tipo de imposto

* **PUT /tipos/{id}**: Atualiza um tipo de imposto específico pelo ID

* **DELETE /tipos/{id}**: Deleta um tipo de imposto específico pelo ID

* **POST /user/login**: Realiza o login e retorna o token JWT

* **POST /user/register**: Cria um novo usuário

* **POST /calculo**: Realiza o cálculo de impostos

## Notas

* A API utiliza autenticação via JWT. Certifique-se de incluir o token JWT no header das requisições.
* A API segue os padrões de resposta HTTP. Verifique os códigos de status e os corpos das respostas para obter mais informações.
* O banco de dados utilizado é o Postgres. Certifique-se de que ele esteja instalado e configurado corretamente no seu sistema.
