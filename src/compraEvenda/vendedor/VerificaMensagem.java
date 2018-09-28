/*
 * Código desenvolvido por Othon Alberto.
 * github: othonalberto
 * email: othon@alunos.utfpr.edu.br
 */
package compraEvenda.vendedor;

import compraEvenda.Veiculo;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 *
 * @author othonalberto
 */
public class VerificaMensagem extends CyclicBehaviour {
    private final String[] veiculosAvenda;
    private String[] tokens;
        
    Veiculo veiculoAtual,
            veiculoDesejado,
            melhorVeiculo;
        
    int match,
        maiorMatch;
        
    public VerificaMensagem(String[] veiculosAvenda) {
        this.veiculosAvenda = veiculosAvenda;
    } 
        
    @Override
    public void action() {
        
        // modelo de resposta que se espera receber
        MessageTemplate contrato = MessageTemplate.MatchPerformative(ACLMessage.CFP);  
                
        ACLMessage msg = myAgent.receive(contrato);
        match = 0;
        
        if (msg != null) {
            
            // recebe as caracteristicas que o comprador deseja
            String params = msg.getContent();
                        
                        
            // busca nos seus veículos se algum deles cumpre requisitos
            for (int i = 0; i < veiculosAvenda.length; i++) {
                tokens = veiculosAvenda[i].split(";");
                           
                veiculoAtual = new Veiculo(Integer.parseInt(tokens[0]),
                                           Integer.parseInt(tokens[1]),
                                           tokens[2],
                                           tokens[3],
                                           Boolean.parseBoolean(tokens[4]),
                                           Double.parseDouble(tokens[5]));
                                                   
                        
                tokens = params.split(";");
                           
                veiculoDesejado = new Veiculo(Integer.parseInt(tokens[0]),
                                              Integer.parseInt(tokens[1]),
                                              tokens[2],
                                              tokens[3],
                                              Boolean.parseBoolean(tokens[4]),
                                              Double.parseDouble(tokens[5]));
                            
                            
                // é criado um medidor de quantas características do veículo são iguais ao veículo pedido
                if (veiculoAtual.getAno() == veiculoDesejado.getAno()) match++;
                
                if (veiculoAtual.getPotencia() == veiculoDesejado.getPotencia()) match++;
                
                if (veiculoAtual.getCor().equals(veiculoDesejado.getCor())) match++;
                
                if (veiculoAtual.getNome().equals(veiculoDesejado.getNome())) match++;
                
                if (veiculoAtual.isCambio() == veiculoDesejado.isCambio()) match++;
                
                // margem de 10mil
                if (veiculoAtual.getPreco() <= veiculoDesejado.getPreco() + 10000) match++;
                            
                if (match > maiorMatch) {
                    maiorMatch = match;
                    melhorVeiculo = veiculoAtual;
                }
                            
                match = 0;
                     
            }
                        
            ACLMessage resposta = msg.createReply();
              
            if (maiorMatch != 0) {
                
                // tem um carro com as características à venda
                resposta.setPerformative(ACLMessage.PROPOSE);
                
                // passa como resposta todas as características do veículo que melhor cumpre os requisitos e seu medidor
                resposta.setContent(String.valueOf(melhorVeiculo.getAno()) + ";" + String.valueOf(melhorVeiculo.getPotencia()) + ";" +
                                    melhorVeiculo.getCor() + ";" + melhorVeiculo.getNome() + ";" +
                                    String.valueOf(melhorVeiculo.isCambio()) + ";" + String.valueOf(melhorVeiculo.getPreco()) + ";" + 
                                    String.valueOf(maiorMatch));
                
                System.out.println("Agente vendedor " + myAgent.getAID().getName() + " enviou mensagem de proposta..");
            } else {
                // Avisa que n  ão tem carro com as características
                resposta.setPerformative(ACLMessage.REFUSE);
                resposta.setContent("Carro não disponível");
                System.out.println("Agente vendedor " + myAgent.getAID().getName() + "enviou mensagem de não disponibilidade");
            }
                    
            myAgent.send(resposta);
                
        } else block();
    }
}
