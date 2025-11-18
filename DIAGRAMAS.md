# Entendendo os Diagramas C4

Este documento explica os dois diagramas gerados por este projeto, seguindo o modelo C4 (Context, Containers, Components, Code).

## 📊 Diagrama 1: System Context (Contexto do Sistema)

### O que é?

O diagrama de contexto fornece uma **visão de alto nível** do sistema, mostrando como ele se encaixa no mundo ao seu redor.

### Para quem?

- **Stakeholders não-técnicos** (gerentes, POs, clientes)
- Qualquer pessoa que precise entender o "big picture"

### O que mostra?

#### 🎯 Sistema Principal

- **WebShop**: Sistema de e-commerce para venda de produtos online

#### 👥 Pessoas (Actors)

- **Customer** (Externo): Cliente que compra produtos
- **Admin** (Interno): Administrador que gerencia o sistema

#### 🔗 Sistemas Externos

- **Payment Gateway**: Sistema de pagamento (Stripe, PayPal, etc.)
- **Email System**: Serviço de envio de emails transacionais

#### 💡 Relacionamentos

```
Customer ──uses──> WebShop ──uses──> Payment Gateway
                     │
Admin    ──uses──────┘        └──> Email System
```

### Perguntas que este diagrama responde:

1. ✅ Quem são os usuários do sistema?
2. ✅ Com quais sistemas externos o WebShop se integra?
3. ✅ Qual é o escopo do sistema?
4. ✅ Quem interage com o sistema e como?

---

## 🔧 Diagrama 2: Container (Arquitetura Interna)

### O que é?

O diagrama de containers mostra a **arquitetura interna** do sistema, revelando as principais partes tecnológicas e como elas se comunicam.

> **Container** no C4 = Uma unidade executável (aplicação web, API, banco de dados, etc.)

### Para quem?

- **Desenvolvedores**
- **Arquitetos de software**
- **DevOps/SREs**

### O que mostra?

#### 📦 Containers do WebShop

1. **Web Application** (React/TypeScript)

   - Interface do usuário
   - Acessada via browser pelo Customer e Admin
   - Faz requisições REST para a API

2. **API Backend** (Spring Boot)

   - Lógica de negócio
   - Processa requisições da Web Application
   - Integra com sistemas externos
   - Persiste dados no banco

3. **Database** (PostgreSQL)
   - Armazena produtos, pedidos e usuários
   - Acessado apenas pela API

#### 🔄 Fluxo de Comunicação

```
Customer/Admin
      │
      ▼ (HTTPS)
Web Application
      │
      ▼ (REST/HTTPS)
API Backend ─────────┐
      │              │
      │              ├─(REST/HTTPS)──> Payment Gateway
      │              │
      ▼ (JDBC)       └─(SMTP)────────> Email System
   Database
```

### Perguntas que este diagrama responde:

1. ✅ Quais tecnologias são usadas?
2. ✅ Como os componentes se comunicam?
3. ✅ Qual é a arquitetura técnica do sistema?
4. ✅ Onde os dados são armazenados?
5. ✅ Como o sistema se integra com serviços externos?

---

## 🎓 Conceitos Importantes para Ensinar

### 1. Separação de Responsabilidades

- **Frontend (Web Application)**: Interface + Experiência do usuário
- **Backend (API)**: Lógica de negócio + Integrações
- **Banco de Dados**: Persistência

### 2. Comunicação entre Camadas

- **Web ↔ API**: REST/HTTPS (stateless, JSON)
- **API ↔ Database**: JDBC (conexão direta)
- **API ↔ Externos**: REST/HTTPS e SMTP (protocolos padrão)

### 3. Segurança por Camadas

- Database **não é exposto** diretamente
- Apenas API acessa o banco
- Web Application não tem acesso direto aos dados

### 4. Sistemas Externos como Dependências

- Payment Gateway: Dependência crítica para pagamentos
- Email System: Dependência não-crítica (pode falhar sem quebrar o sistema)

---

## 📝 Exercícios para Alunos

### Nível 1: Compreensão

1. Liste todos os atores (pessoas) do sistema
2. Identifique quais sistemas são internos e quais são externos
3. Conte quantos containers existem no WebShop

### Nível 2: Análise

1. Por que o Database não está conectado diretamente à Web Application?
2. Quais containers precisam estar online para um cliente fazer uma compra?
3. O que acontece se o Email System ficar indisponível?

### Nível 3: Extensão

1. Adicione um novo container "Mobile App" ao sistema
2. Adicione um sistema externo "Inventory System" para controle de estoque
3. Crie um novo tipo de usuário "Warehouse Manager"

---

## 🔗 Modelo C4 Completo

Este projeto implementa os **2 primeiros níveis** do modelo C4:

1. ✅ **Context**: Visão do sistema no mundo
2. ✅ **Container**: Arquitetura interna de alto nível
3. ⬜ **Component**: Componentes dentro de cada container (não implementado)
4. ⬜ **Code**: Classes e interfaces (não implementado)

### Por que começar com Context e Container?

- São os mais **úteis** para a maioria dos projetos
- Fáceis de **manter atualizados**
- Fornecem **90% do valor** com 20% do esforço
- Adequados para **discussões com stakeholders**

---

## 📚 Referências

- [Modelo C4 - Simon Brown](https://c4model.com)
- [Structurizr](https://structurizr.com)
- [The Art of Visualising Software Architecture](https://leanpub.com/visualising-software-architecture)
