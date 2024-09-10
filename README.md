# Recursos de Autorização com Spring Security 3.4

## Recursos utilizados no projeto
- [Java 22](https://jdk.java.net/22/)
- [Spring Boot - 3.3.3](https://spring.io/projects/spring-boot)
- [IntelliJ IDEA 2023.3.6 (Community Edition)](https://www.jetbrains.com/idea/)


## Autenticação X Autorização
- Autenticação
    - Quem está tentando acessar um determinado recurso?
- Autorização
    - Dado que sei quem esta tentando acessar o recurso, ele tem permissão para isso ?
- Spring Security traz métodos de segurança que permite você separar sua logica de autorização da lógica de domínio.

## Criando a estrutura inicial
- O projeto irá se basear em uma aplicativo para banco.
- Criando a estrutura inicial do projeto com o [Spring Initializr](https://start.spring.io/)
- ![01-spring-initializr](./images/01-spring-initializr.png)
- A ideia desta aplicação é para que ela tenha uma estrura de domínio simples mas que foque em segurança.

## Criando a entidade de domínio Banck Account
- A classe será constituida com os seguintes atributos:
    - **id** - identificador da conta.
    - **owner** - identificador do proprietário da conta bancária.
    - **accountNumber** - número da conta bancária.
    - **balance** - saldo da conta.
    ```java
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class BankAccount {

        private Integer id;
        private String owner;
        private String accountNumber;
        private double balance;
    }
    ```

## Criando a classe de serviço BankAccountService
- A classe de serviço inicialmente irá receber dois métodos, `findById()` e `getById()`. Ambos irão executar a mesma ação mas a idéia aqui é pode utilizar o `getById()`, comparado com o `findById()`, para explicar melhor algumas das funcionalidades do Spring Security funciona de acordo com algumas circunstâncias.
- O objetivo aqui não é focar no tipo de dado que os métodos retornam, por isso suas informações de resposta serão um objeto com valor fixo.
    ```java
    public class BankAccountService {

        public BankAccountData findById(Integer id) {
            return BankAccountData.builder()
                    .id(id)
                    .accountNumber("1234")
                    .owner("angelo")
                    .balance(1000D)
                    .build();
        }

        public BankAccountData getById(Integer id) {
            return findById(id);
        }
    }
    ```

## Criando e validando nosso serviço com segurança
- Nesta etapa vou criar uma classe de teste unitário para poder ver como de fato a segurança esta sendo valiadade em alguns cenários.
- Aqui vou demonstrar em etapas e de modo bem manual o que acontece por de trás dos "*panos*". Vamos poder ver as regras sendo aplicadas para as validações de segurança e por fim como o Spring resume toda essa funcionalidade de uma maneira mais prática.

1. Método `login()`
    - O método configura um contexto de autenticação para simular o login de um usuário durante os testes.
    - O método recebe um parâmetro `user`, que representa o nome do usuário que está sendo autenticado.
    - Cria um token de autenticação que será usado para simular um usuário autenticado durante os testes.
        ```java
        Authentication auth = new TestingAuthenticationToken(user, "password", "ADMIN");
        ```
    - Na sequência vamos configurar o contexto de segurança e para isto vamos utilizar o seguinte trecho que vai definir o objeto de autenticação criado (`auth`) no contexto de segurança do Spring.
    - O `SecurityContextHolder` é uma classe que armazena o `SecurityContext`, permitindo que a autenticação esteja disponível em qualquer parte da aplicação que precise acessar informações sobre o usuário autenticado.
        ```java
        SecurityContextHolder.getContext().setAuthentication(auth);
        ```

2. Método `cleanUp()`
    - Método que será executado, limpando o `SecurityContextHolder` pré configurado, após a cada teste
        ```java
        @AfterEach
        void cleanUp() {
            SecurityContextHolder.clearContext();
        }
        ```
3. Cenários de testes
    - O primeiro teste deve encontrar uma conta bancária com sucesso
        ```java
        @Test
        @DisplayName("Should find the bank account with success")
        void findByIdGranted() {
            login("angelo");
            service.findById(1);
        }
        ```
    - O segundo teste deve encontrar uma conta bancária com sucesso
        ```java
        @Test
        @DisplayName("Should get the bank account with success")
        void getByIdGranted() {
            login("angelo");
            service.getById(1);
        }
        ```

    - O terceiro teste deve falhar ao tentar recuperar uma conta bancária da qual o usuário não tem acesso
        ```java
        @Test
        @DisplayName("Should fail to find the bank account")
        void findByIdWhenDenied() {
            login("jake");
            assertThatExceptionOfType(AuthorizationDeniedException.class)
                    .isThrownBy(() -> service.findById(1))
                    .withMessage("Access Denied");
        }
        ```

    - O quarto teste deve falhar ao tentar recuperar uma conta bancária da qual o usuário não tem acesso
        ```java
        @Test
        @DisplayName("Should fail to get the bank account")
        void getByIdWhenDenied() {
            login("jake");
            assertThatExceptionOfType(AuthorizationDeniedException.class)
                    .isThrownBy(() -> service.getById(1))
                    .withMessage("Access Denied");
        }
        ```

## Executando os testes
- *No projeto para este artigo esta sendo utilizado a versão 22 do Java e a versão 2023.3.6 (Community Edition) do IntelliJ, então em caso de falha ou se o seguinte erro acontecer ao executar os testes, segue a solução*
    - Falha:
        ```shell
        java: invalid source release 21 with --enable-preview
        (preview language features are only supported for release 22)
        ```
    - Solução:
        - **File > Project Structure > Modules > Language Level: X - Experimental features**
          ![02-fail-test-java-22](./images/02-fail-test-java-22.png)
    - Fonte:
        - [Java 17: java: invalid source release 7 with --enable-preview (preview language features are only supported for release 17) - By Vy Do](https://stackoverflow.com/questions/70083274/java-17-java-invalid-source-release-7-with-enable-preview-preview-language)

- Inicialmente dos 4 testes que temos, 2 não devem passar pois ainda não temos implementado os requisitos de segurança para que o usuário logado não veja a conta da qual ele não deve ter acesso
  ![03-tests-fails](./images/03-tests-fails.png)

## Ajustando nossa classe de serviço
- Voltando a classe `BankAccountService` vamos fazer um pequano ajuste no método `findById()`.
- Como `SecurityContextHolder` mantém os dados da autenticação atual, vamos recuperar estes dados e fazer nossa primeira validação por nome do usuário tentando acessar a conta bancária comparando com os dados do proprietário da conta bancária.
    ```java
    public BankAccountData findById(Integer id) {
        var bankAccount = BankAccountData.builder()
                .id(id)
                .accountNumber("1234")
                .owner("angelo")
                .balance(1000D)
                .build();

        // Getting the authenticated user from the session
        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        // Checking if the authenticated user has the same name as the bank account owner
        if (!principal.getName().equals(bankAccount.getOwner())) {
            throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
        }

        return bankAccount;
    }
    ```
## De volta aos testes
- Com a alteração realizada no passo anterior agora devemos ter todos os testes passando.
  ![05-success-tests](./images/04-success-tests.png)
- Mas como podemos melhorar o seguintes pontos?
    - 1. Para cada teste nós estamos limpando o usuário da sessão
    - 2. Estamos misturando nossa lógica de domínio com o serviço que válida o usuário da sessão.

## Hello World Spring Security!
- Para inicio vamos criar um objeto de proxy manualmente chamado `BanckAccountServiceProxy`. Com isso vou conseguir explicar melhor o que o Spring Security esta fazendo por trás.
- Esta nova classe vai extender `BankAccountService` e com isso vamos sobrescrever o método `findById()`.
- Na classe `BankAccountService` removemos a regra de validar o `owner` e passamos ela para `BanckAccountServiceProxy`.
- Com isso removemos a regra de validação de segurança do nosso domínio e passamos a utilziar um `proxy` para adminstrar isso.
    ```java
    public class BankAccountServiceProxy extends BankAccountService {

        @Override
        public BankAccountData findById(Integer id) {
            var bankAccount = super.findById(id);

            Principal principal = SecurityContextHolder.getContext().getAuthentication();

            if (!principal.getName().equals(bankAccount.getOwner())) {
                throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
            }

            return bankAccount;
        }
    }
    ```

- Vamos extrair os métodos `findById` e `getById` para uma interface chamada `BankAccountInterface`
    ```java
    public interface BankAccountInterface {
        BankAccountData findById(Integer id);
        BankAccountData getById(Integer id);
    }
    ```

- A classe final fica assim
    ```java
    @Service
    @AllArgsConstructor
    public class BankAccountServiceProxy implements BankAccountInterface {

        private final BankAccountService service;

        @Override
        public BankAccountData findById(Integer id) {
            var bankAccount = service.findById(id);

            Principal principal = SecurityContextHolder.getContext().getAuthentication();

            if (!principal.getName().equals(bankAccount.getOwner())) {
                throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
            }

            return bankAccount;
        }

        @Override
        public BankAccountData getById(Integer id) {
            return service.findById(id);
        }
    }
    ```

- E não se esqueça de alterar o seguinte trecho na classe de teste `BankAccountServiceTest` fazendo `service` receber agora uma instância da *interface* `BankAccountInterface`
    ```java
    public class BankAccountServiceTest {

        private final BankAccountInterface service = new BankAccountServiceProxy(new BankAccountService());
        //some code here...
    ```

- Agora apenas um testes vai falhar `@DisplayName("Should fail to get the bank account")` e isso se deve pois o caminho lógico sendo respeitado para ter o valor devolvido é:
    - 1. Temos uma instância da *interface* `BankAccountInterface`
    - 2. A *interface* irá chamar o método `service.getById(1)`
    - 3. A instância de `service` é de `new BankAccountServiceProxy` então o método `service.getById(1)` a ser executado será da classe `BankAccountServiceProxy`
    - 4. O método é
      ```java
      @Override
      public BankAccountData getById(Integer id) {
          return service.findById(id);
      }
      ```
    - 5. Este `findById()` vem do atributo importado na classe `private final BankAccountService service;`
    - 6. Por fim este método não chega a passar pela segurança e com isso retorna o valor fixo
      ```java
          @Override
          public BankAccountData findById(Integer id) {
              return BankAccountData.builder()
                      .id(id)
                      .accountNumber("1234")
                      .owner("angelo")
                      .balance(1000D)
                      .build();
          }
      ```
- Para corrgir este teste precisamos apenas implementar na classe `BankAccountServiceProxy` a validação de segurança em `getById()` igual temos em `findById()`
    ```java
    @Override
    public BankAccountData getById(Integer id) {
        var bankAccount = service.findById(id);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();

        if (!principal.getName().equals(bankAccount.getOwner())) {
            throw new AuthorizationDeniedException("Access Denied", new AuthorizationDecision(false));
        }

        return bankAccount;
    }
    ```

## Utilizando recursos integrados do Spring Security
- Primeiramente vamos fazer algumas modificações nos nossos testes atendendo os pontos que haviam sido comentados:
    - 1. "*...Para cada teste nós estamos limpando o usuário da sessão*"
    - 2. "*...Estamos misturando nossa lógica de domínio com o serviço que válida o usuário da sessão.*"

- Na classe `BankAccountServiceTest` vamos remover
    - 1. O método `cleanUp()`
    - 2. O método `login()`
    - 3. Dentro dos testes remover a chamada do método `login()`

- Após a refatoração vamos adicionar `@SpringBootTest` no escopo da classe e também uma nova *annotation* chamada `@WithMockUser()`.
- Ela será utilizada em cada teste sendo passado o nome do usuário da sessão de teste
- *Ex.:* `@WithMockUser("angelo")`.
- A classe deve ficar assim com todos os testes passando.
    ```java
    @SpringBootTest
    public class BankAccountServiceTest {

        private final BankAccountInterface service = new BankAccountServiceProxy(new BankAccountService());
        
        @Test
        @DisplayName("Should find the bank account with success")
        @WithMockUser("angelo")
        void findByIdGranted() {
            service.findById(1);
        }

        @Test
        @DisplayName("Should get the bank account with success")
        @WithMockUser("angelo")
        void getByIdGranted() {
            service.getById(1);
        }

        @Test
        @DisplayName("Should fail to find the bank account")
        @WithMockUser("jake")
        void findByIdWhenDenied() {
            assertThatExceptionOfType(AuthorizationDeniedException.class)
                    .isThrownBy(() -> service.findById(1))
                    .withMessage("Access Denied");
        }

        @Test
        @DisplayName("Should fail to get the bank account")
        @WithMockUser("jake")
        void getByIdWhenDenied() {
            assertThatExceptionOfType(AuthorizationDeniedException.class)
                    .isThrownBy(() -> service.getById(1))
                    .withMessage("Access Denied");
        }
    }
    ```

- Mas como você pode notar estamos duplicando `@WithMockUser("angelo")` e `@WithMockUser("jake")` e isso não é bom pois temos tipos de pessoas diferentes e aqui estamos distinguindo isso apenas pelo nome.
- Vamos melhorar isso criando uma *annotation* específica para cada tipo de usuário.
- Vamos criar a *annotation* `WithMockUserAngelo` e `WithMockUserJake`
- `WithMockUserAngelo`
    ```java
    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser("angelo")
    public @interface WithMockUserAngelo {
    }
    ```

- `WithMockUserJake`
    ```java
    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser("jake")
    public @interface WithMockUserJake {
    }
    ```

- E com isso podemos incluir essas novas *annotations* em nossos testes, tudo deve continuar funcionando e os testes passando.
    ```java
        @Test
        @DisplayName("Should find the bank account with success")
        @WithMockUserAngelo
        void findByIdGranted() {
        //... some code here
    ``` 
- 