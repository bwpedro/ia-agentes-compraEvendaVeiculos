/*
 * Código desenvolvido por Othon Alberto.
 * github: othonalberto
 * email: othon@alunos.utfpr.edu.br
 */
package compraEvenda.comprador;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author othonalberto
 */
public class ConheceVendedores extends OneShotBehaviour {
    List<AID> vendedores;
    
    public ConheceVendedores(List<AID> vendedores) {
        this.vendedores = vendedores;
    }
    
    @Override
    public void action() {
        
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("vendedor");
        template.addServices(sd);
        
        try {
            // procura todos os vendedores do sistema
            DFAgentDescription[] busca = DFService.search(myAgent, template);

            // adiciona cada vendedor encontrado na lista de vendedores
            for (int i = 0; i < busca.length; i++) {
                vendedores.add(busca[i].getName());
            }
        
            if (vendedores.size() == 2)
                System.out.println("Agente comprador " + myAgent.getAID().getName() + " já conhece os vendedores. Total: " + vendedores.size());
            else block();
            
        } catch (FIPAException ex) {
            Logger.getLogger(Comprador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
