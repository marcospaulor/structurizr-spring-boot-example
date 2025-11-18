package com.example.structurizrexample.model;

import com.structurizr.model.Location;
import com.structurizr.model.Model;
import com.structurizr.model.Person;
import com.structurizr.model.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Personas {

    private final Person admin;
    private final Person customer;

    @Autowired
    public Personas(Model model) {
        // Administrador do sistema (interno)
        admin = model.addPerson(Location.Internal, "Admin", 
            "Administrador do sistema responsável por gerenciar produtos, visualizar pedidos e gerar relatórios");
        admin.addTags(Tags.PERSON);
        
        // Cliente do e-commerce (externo)
        customer = model.addPerson(Location.External, "Customer", 
            "Cliente que acessa a loja virtual para navegar, buscar produtos e realizar compras online");
        customer.addTags(Tags.PERSON);
    }

    public Person getAdmin() {
        return admin;
    }

    public Person getCustomer() {
        return customer;
    }
}
