/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compraEvenda.comprador;

import jade.core.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedrowarmlingbotelho
 */
public class Comprador extends Agent {
    private String[] veiculoAcomprar;
    private List<AID> vendedores;
    
    @Override
    protected void setup(){
        
        System.out.println("Iniciando agente Comprador: " + getAID().getName());
        
        veiculoAcomprar = (String[]) getArguments();
        
        // verifica se há parâmetros
        if (veiculoAcomprar == null) {
            System.out.println("Características não especificadas");
            System.exit(0);
        }

        // cria o registro para as páginas amarelas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription servico = new ServiceDescription();
        servico.setType("comprador");
        servico.setName("COMPRADOR");
        dfd.addServices(servico);
        
        try {
            // registar o agente nas paginas amarelas
            DFService.register(this,dfd);
            System.out.println("Agente comprador " + getAID().getName() + " registrado.");
        } catch (FIPAException fe){
            System.out.println(fe);
        } 
        
        vendedores = new ArrayList<>();
        
        // comportamento para conhecer os vendedores
        addBehaviour(new ConheceVendedores(vendedores));
        
        // comportamento que realiza a compra
        addBehaviour(new RealizaCompra(vendedores, veiculoAcomprar[0]));
    
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Agente sendo desligado: " + getAID().getName());
    }

}
