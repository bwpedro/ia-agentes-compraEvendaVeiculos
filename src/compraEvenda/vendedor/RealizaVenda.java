/*
 * Código desenvolvido por Othon Alberto.
 * github: othonalberto
 * email: othon@alunos.utfpr.edu.br
 */
package compraEvenda.vendedor;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author othonalberto
 */
public class RealizaVenda extends CyclicBehaviour {
    
    @Override
    public void action() {
        // modelo de resposta que o vendedor espera receber 
        MessageTemplate modeloResposta = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        
        // recebe a mensagem do comprador
        ACLMessage msg = myAgent.receive(modeloResposta);
                        
        if (msg != null) {
            String conteudo = msg.getContent();
            double preco = Double.parseDouble(conteudo);
            
            ACLMessage resposta = msg.createReply();
            resposta.setPerformative(ACLMessage.INFORM);
                            
            System.out.println("Agente vendedor " + myAgent.getAID().getName() + " pronto para executar a venda para o agente " + msg.getSender().getName() + " pelo preco de " + preco);
            myAgent.send(resposta);   
                            
        } else {
            // checa se o que chegou é uma mensagem do tipo FAILURE
            modeloResposta = MessageTemplate.MatchPerformative(ACLMessage.FAILURE);
            msg = myAgent.receive(modeloResposta);
            
            // se sim, mata o agente pois a venda não poderá ser realizada
            if (msg != null) {
                myAgent.doDelete();
            }
            else block();
        }
                            
    }              
}
