package com.example.structurizrexample.model;

import cc.catalysts.structurizr.ViewProvider;
import com.structurizr.model.Container;
import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.SoftwareSystem;
import com.structurizr.view.ContainerView;
import com.structurizr.view.SystemContextView;
import com.structurizr.view.ViewSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebShop implements ViewProvider {

    private final SoftwareSystem webShop;
    private final SoftwareSystem paymentSystem;
    private final SoftwareSystem emailSystem;
    private final Container webApplication;
    private final Container database;
    private final Container apiBackend;
    private final Personas personas;

    @Autowired
    public WebShop(Model model, Personas personas) {
        this.personas = personas;
        
        // Sistema principal - WebShop
        webShop = model.addSoftwareSystem(Location.Internal, "WebShop", 
            "Sistema de e-commerce para venda de produtos online");

        // Sistemas externos
        paymentSystem = model.addSoftwareSystem(Location.External, "Payment Gateway", 
            "Sistema de pagamento externo (ex: Stripe, PayPal)");
        
        emailSystem = model.addSoftwareSystem(Location.External, "Email System", 
            "Sistema de envio de emails transacionais");

        // Containers do WebShop
        webApplication = webShop.addContainer("Web Application", 
            "Interface web para clientes e administradores", "React/TypeScript");
        webApplication.addTags("WebApp");
        
        apiBackend = webShop.addContainer("API Backend", 
            "Fornece funcionalidades via REST API", "Spring Boot");
        
        database = webShop.addContainer("Database", 
            "Armazena produtos, pedidos e usuários", "PostgreSQL");
        database.addTags("Database");

        // Relacionamentos entre pessoas e containers
        personas.getCustomer().uses(webApplication, "Navega, busca e compra produtos", "HTTPS");
        personas.getAdmin().uses(webApplication, "Gerencia produtos e visualiza pedidos", "HTTPS");

        // Relacionamentos entre containers
        webApplication.uses(apiBackend, "Faz requisições para", "REST/HTTPS");
        apiBackend.uses(database, "Lê e escreve dados", "JDBC");
        
        // Relacionamentos com sistemas externos
        apiBackend.uses(paymentSystem, "Processa pagamentos", "REST/HTTPS");
        apiBackend.uses(emailSystem, "Envia confirmações e notificações", "SMTP");
        
        // Relacionamentos inversos (notificações)
        paymentSystem.uses(apiBackend, "Envia callback de status do pagamento", "Webhook/HTTPS");
    }

    @Override
    public void createViews(ViewSet viewSet) {
        // 1. DIAGRAMA DE CONTEXTO - Visão macro do sistema
        SystemContextView contextView = viewSet.createSystemContextView(
            webShop, 
            "SystemContext", 
            "Diagrama de Contexto do Sistema WebShop"
        );
        
        // Adiciona todos os elementos (pessoas e sistemas)
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();
        contextView.setPaperSize(com.structurizr.view.PaperSize.A4_Landscape);
        
        // 2. DIAGRAMA DE CONTAINER - Visão interna do WebShop
        ContainerView containerView = viewSet.createContainerView(
            webShop, 
            "Containers", 
            "Diagrama de Containers do Sistema WebShop"
        );
        
        // Adiciona todos os containers do WebShop
        containerView.addAllContainers();
        
        // Adiciona as pessoas que interagem com o sistema
        containerView.add(personas.getCustomer());
        containerView.add(personas.getAdmin());
        
        // Adiciona os sistemas externos que se comunicam com os containers
        containerView.add(paymentSystem);
        containerView.add(emailSystem);
        containerView.setPaperSize(com.structurizr.view.PaperSize.A4_Landscape);
        
        // Adiciona relacionamentos externos que não aparecem automaticamente
        containerView.addAllInfluencers();
        
        // 3. ESTILOS - Torna os diagramas mais legíveis
        com.structurizr.view.Styles styles = viewSet.getConfiguration().getStyles();
        
        // Estilo para pessoas
        styles.addElementStyle(com.structurizr.model.Tags.PERSON)
            .background("#08427b")
            .color("#ffffff")
            .shape(com.structurizr.view.Shape.Person);
        
        // Estilo para software systems
        styles.addElementStyle(com.structurizr.model.Tags.SOFTWARE_SYSTEM)
            .background("#1168bd")
            .color("#ffffff");
        
        // Estilo para containers
        styles.addElementStyle(com.structurizr.model.Tags.CONTAINER)
            .background("#438dd5")
            .color("#ffffff");
        
        // Estilo específico para Web Application (frontend)
        styles.addElementStyle("WebApp")
            .background("#85bbf0")
            .color("#000000")
            .shape(com.structurizr.view.Shape.WebBrowser);
        
        // Estilo específico para Database
        styles.addElementStyle("Database")
            .background("#438dd5")
            .color("#ffffff")
            .shape(com.structurizr.view.Shape.Cylinder);
        
        // Estilo para relacionamentos
        styles.addRelationshipStyle(com.structurizr.model.Tags.RELATIONSHIP)
            .thickness(2)
            .fontSize(24);
    }
}
