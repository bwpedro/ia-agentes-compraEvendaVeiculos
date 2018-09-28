/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compraEvenda.vendedor;

import jade.core.*;
import compraEvenda.Veiculo;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedrowarmlingbotelho
 */

public class Vendedor extends Agent {
    private String[] veiculosAvenda;
    public Veiculo veiculoAtual,
                   veiculoDesejado,
                   melhorVeiculo;
    
    private String tokens[];
    
    private int match = 0,
                maiorMatch = 0;
    
    @Override
    protected void setup() {
       
        System.out.println("Iniciando agente Vendedor: " + getAID().getName());
        
        //Object params = getArguments(); // recebe os parametros pela main, os carros que terá a venda
        veiculosAvenda = (String[]) getArguments();
        
        // registra que tem os carros a venda das paginas amarelas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription servico = new ServiceDescription();
        
        servico.setType("vendedor");
        servico.setName("VENDEDOR");
        dfd.addServices(servico);
        
        try {
            DFService.register(this, dfd);
            System.out.println("Agente vendedor " + getAID().getName() + " registrado.");
        } catch (FIPAException ex) {
            Logger.getLogger(Vendedor.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        // fica vendo se tem mensagens para ele
        addBehaviour(new VerificaMensagem(veiculosAvenda));
                
        // RESPOSTA DE VENDA
        addBehaviour(new RealizaVenda());
    }
    
    @Override
    protected void takeDown() {
        System.out.println("Agente sendo desligado: " + getAID().getName());
    }
}
