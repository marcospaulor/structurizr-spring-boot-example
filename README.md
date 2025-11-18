# Structurizr Spring Boot Example

Este projeto demonstra o uso do **Structurizr** com **Spring Boot** para criar modelos de arquitetura de software de forma programática e modular.

## O que é Structurizr?

> "Structurizr é uma coleção de ferramentas para ajudá-lo a visualizar, documentar e explorar sua arquitetura de software."

Structurizr permite criar diagramas de arquitetura usando código Java, seguindo o modelo C4 (Context, Containers, Components, Code).

## Diagramas Gerados

Este exemplo gera **dois diagramas** principais:

### 1. 📊 Diagrama de Contexto do Sistema (System Context)

Mostra a visão macro do sistema, incluindo:

- **Sistema WebShop** (interno)
- **Sistemas Externos**: Payment Gateway e Email System
- **Usuários**: Customer (externo) e Admin (interno)
- **Relacionamentos** entre usuários e sistemas

**O que você verá:**

- Como o WebShop se relaciona com sistemas externos
- Quem são os usuários do sistema
- Fluxo geral de comunicação entre sistemas

### 2. 🔧 Diagrama de Containers

Mostra a visão interna do WebShop, detalhando:

- **Web Application** (React/TypeScript) - Interface do usuário
- **API Backend** (Spring Boot) - Lógica de negócio
- **Database** (PostgreSQL) - Persistência de dados

**O que você verá:**

- Arquitetura interna do WebShop
- Como os containers se comunicam entre si
- Integração com sistemas externos (Payment Gateway e Email)
- Interação dos usuários com cada container

## Arquitetura do Sistema

```
┌─────────────────────────────────────────────────────────────┐
│                         WebShop                              │
├─────────────────────────────────────────────────────────────┤
│  ┌──────────────┐      ┌──────────────┐      ┌──────────┐  │
│  │     Web      │─────▶│     API      │─────▶│ Database │  │
│  │ Application  │      │   Backend    │      │          │  │
│  │              │      │              │      │          │  │
│  └──────────────┘      └──────────────┘      └──────────┘  │
│         ▲                     │                              │
│         │                     │                              │
└─────────┼─────────────────────┼──────────────────────────────┘
          │                     │
          │                     ├──────▶ Payment Gateway
    Customer/Admin              │
                                └──────▶ Email System
```

## Como funciona este exemplo

### 1. Dependências (`build.gradle`)

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter'
    implementation 'cc.catalysts.structurizr:structurizr-spring-boot:1.3.0.4'
}
```

A biblioteca `structurizr-spring-boot` da Catalysts integra automaticamente o Structurizr com Spring Boot através de AutoConfiguration.

### 2. Configuração (`application.yml`)

```yaml
structurizr:
  workspace:
    id: 108457
    name: StructurizrExample
    description: Exemplo baseado no tutorial Catalysts
    key: sua-api-key
    secret: seu-api-secret
```

**⚠️ Importante:** Crie uma conta gratuita em [structurizr.com](https://structurizr.com) para obter suas credenciais.

### 3. Modelagem Modular

#### Personas (`Personas.java`)

```java
@Component
public class Personas {
    private final Person admin;
    private final Person customer;

    @Autowired
    public Personas(Model model) {
        admin = model.addPerson(Location.Internal, "Admin", "...");
        customer = model.addPerson(Location.External, "Customer", "...");
    }

    // getters...
}
```

**Vantagens:**

- ✅ **Type-safe**: Não precisa passar strings com nomes de personas
- ✅ **Modular**: Cada conceito em sua própria classe
- ✅ **Reutilizável**: Injete `Personas` em outras classes

#### Sistema de Software com Múltiplos Containers e Views (`WebShop.java`)

```java
@Component
public class WebShop implements ViewProvider {
    private final SoftwareSystem webShop;
    private final Container webApplication;
    private final Container apiBackend;
    private final Container database;

    @Autowired
    public WebShop(Model model, Personas personas) {
        // Sistema principal
        webShop = model.addSoftwareSystem(Location.Internal, "WebShop", "...");

        // Containers internos
        webApplication = webShop.addContainer("Web Application", "...", "React");
        apiBackend = webShop.addContainer("API Backend", "...", "Spring Boot");
        database = webShop.addContainer("Database", "...", "PostgreSQL");

        // Relacionamentos
        personas.getCustomer().uses(webApplication, "...", "HTTPS");
        webApplication.uses(apiBackend, "...", "REST/HTTPS");
        apiBackend.uses(database, "...", "JDBC");
    }

    @Override
    public void createViews(ViewSet viewSet) {
        // 1. DIAGRAMA DE CONTEXTO
        SystemContextView contextView = viewSet.createSystemContextView(
            webShop, "SystemContext", "Diagrama de Contexto do Sistema WebShop");
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // 2. DIAGRAMA DE CONTAINER
        ContainerView containerView = viewSet.createContainerView(
            webShop, "Containers", "Diagrama de Containers do Sistema WebShop");
        containerView.addAllContainers();
        containerView.add(personas.getCustomer());
        containerView.add(personas.getAdmin());
    }
}
```

**Destaques do código:**

- ✅ Define **múltiplos containers** (Web, API, Database)
- ✅ Relaciona containers com **sistemas externos**
- ✅ Cria **dois tipos de views** (Context e Container)
- ✅ Usa `addAllContainers()` para incluir todos os containers automaticamente

````

**Nota importante:** A interface `ViewProvider` garante que as views sejam criadas **após** todo o modelo estar pronto.

## Executando o projeto

```bash
./gradlew bootRun
````

Quando a aplicação iniciar, ela automaticamente:

1. Constrói o modelo de arquitetura (sistemas, containers e relacionamentos)
2. Cria as views (Context e Container)
3. Adiciona relacionamentos implícitos
4. Faz upload para o Structurizr.com

**Saída esperada:**

```
Added 5 implicit relationships.
Getting workspace with ID 108457
Putting workspace with ID 108457
{"success":true,"message":"OK"}
```

Depois acesse seu workspace em: https://structurizr.com/workspace/[SEU_ID]

## Visualizando os Diagramas

No Structurizr.com você terá acesso a:

1. **SystemContext** - Visão de alto nível

   - Veja todos os sistemas e como se relacionam
   - Identifique dependências externas

2. **Containers** - Visão detalhada do WebShop
   - Explore a arquitetura interna
   - Entenda o fluxo de dados entre containers
   - Veja integrações com sistemas externos

## Estrutura do Projeto

```
src/main/java/
├── StructurizrExampleApplication.java  # Classe principal Spring Boot
└── model/
    ├── Personas.java                   # Define pessoas do sistema
    └── WebShop.java                    # Define sistema e views
```

## Importante: Correção do Import

O tutorial original menciona o pacote errado. Use:

```java
import cc.catalysts.structurizr.ViewProvider;  // ✅ Correto
// NÃO: cc.catalysts.structurizr.spring.boot.autoconfigure.ViewProvider
```

## Referências

- 📖 [Tutorial Original da Catalysts](https://github.com/Catalysts/cat-boot-structurizr)
- 🌐 [Structurizr.com](https://structurizr.com)
- 📚 [Documentação Structurizr](https://structurizr.org)
- 🏗️ [Modelo C4](https://c4model.com)

## Versões Compatíveis

- Spring Boot: 2.7.18
- Structurizr Spring Boot: 1.3.0.4
- Structurizr Core: 1.3.0 (transitiva)
- Java: 8+
# structurizr-spring-boot-example
