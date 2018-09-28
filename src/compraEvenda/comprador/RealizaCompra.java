/*
 * Código desenvolvido por Othon Alberto.
 * github: othonalberto
 * email: othon@alunos.utfpr.edu.br
 */
package compraEvenda.comprador;

import compraEvenda.Veiculo;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.List;

/**
 *
 * @author othonalberto
 */
public class RealizaCompra extends CyclicBehaviour {
    private int passo, qtdRespostas, match, melhorMatch;
    private AID vendedorMatch, vendedorPreco, melhorVendedor;
    private double preco, melhorPreco, precoMatch, precoMax;    
    private MessageTemplate modeloResposta;
    private ACLMessage resposta;  
    private final List<AID> vendedores;
    private final String veiculoAcomprar;
    private Veiculo veiculoMatch, veiculoPreco, veiculoAtual;
    
    
    public RealizaCompra(List<AID> vendedores, String veiculoAcomprar) {
        this.vendedores = vendedores;
        this.veiculoAcomprar = veiculoAcomprar;
        this.melhorVendedor = null;
        this.modeloResposta = null;
        this.resposta = null;
    }
 
    @Override
    public void action() {
        
        switch(passo) {
                    
            case 0: // envia qual carro quer comprar
                
                ACLMessage contrato = new ACLMessage(ACLMessage.CFP);
                        
                // adiciona os vendedores que receberão a mensagem
                for (int i = 0; i < vendedores.size(); i++) {
                    contrato.addReceiver(vendedores.get(i));
                }
                        
                // seta atributos da mensagem
                contrato.setContent(veiculoAcomprar);
                contrato.setConversationId("compra-veiculo");
                contrato.setReplyWith("contrato"+System.currentTimeMillis());
                
                // envia mensagem
                myAgent.send(contrato);
                        
                // modelo de resposta que se espera receber
                modeloResposta = MessageTemplate.and(MessageTemplate.MatchConversationId("compra-veiculo"),
                                                             MessageTemplate.MatchInReplyTo(contrato.getReplyWith()));
                        
                passo++;
                System.out.println("Agente comprador " + myAgent.getAID().getName() + " enviou o carro que deseja comprar.");
                
                break;
                                  
            case 1: // recebe respostas dos vendedores
                        
                resposta = myAgent.receive(modeloResposta);
        
                if (resposta != null ) {
                            
                    System.out.println("Chegou resposta ao agente comprador " + myAgent.getAID().getName() + " enviada pelo agente " + resposta.getSender().getName());
                    
                    // verifica se a resposta enviada por um dos vendedores é uma proposta
                    if (resposta.getPerformative() == ACLMessage.PROPOSE) {
                        
                        // separa os atributos enviados pelo vendedores
                        String tokens[] = resposta.getContent().split(";");
                        
                        // cria um veículo com os atributos separados
                        veiculoAtual = new Veiculo(Integer.parseInt(tokens[0]),
                                           Integer.parseInt(tokens[1]),
                                           tokens[2],
                                           tokens[3],
                                           Boolean.parseBoolean(tokens[4]),
                                           Double.parseDouble(tokens[5]));
                        
                        match = Integer.parseInt(tokens[6]);
                        
                        preco = veiculoAtual.getPreco();
                        
                        /*
                        Nos ifs abaixo são salvos o veículo com maior match entre os dois e o com menor preço.
                        Além disso, também são guardados o vendedor de cada um e o preco.
                        */
                        
                        if (vendedorMatch == null || match > melhorMatch) {
                            melhorMatch = match;
                            vendedorMatch = resposta.getSender();
                            veiculoMatch = veiculoAtual;
                            precoMatch = preco;
                        }
                        
                        if (vendedorPreco == null || preco < melhorPreco) {
                            melhorPreco = preco;
                            vendedorPreco = resposta.getSender();
                            veiculoPreco = veiculoAtual;
     
                        }
                    }
                            
                    qtdRespostas++;
                            
                    // se já tiver recebidos todas as respostas
                    if (qtdRespostas == vendedores.size()) {
                       
                        /*
                         Verifica se o preço vinculado ao melhor match é no máximo 10 mil a mais. Se sim, comprará este carro
                         Se não, verifica se o veículo de menor preço cumpre a esse requisito. Então, compra o carro.
                         Caso contrário, não há nenhum carro ideal para a compra. Desse modo, mata todos os agentes e encerra o programa
                        */
                        if (precoMatch != 0 && precoMatch < 10000 + (Double.parseDouble(veiculoAcomprar.split(";")[5]))) {
                            melhorVendedor = vendedorMatch;
                            veiculoAtual = veiculoMatch;
                            passo = 2;
                        } else if (melhorPreco != 0 && melhorPreco < 10000 + (Double.parseDouble(veiculoAcomprar.split(";")[5]))) {
                            melhorVendedor = vendedorPreco;
                            veiculoAtual = veiculoPreco;
                            passo = 2;
                        } else {
                            System.out.println("Não há nenhum carro ideal para a compra!");

                            for (int i = 0; i < vendedores.size(); i++) {
                                // para cada vendedor, realiza processo de matar o agente
                                ACLMessage pedido = new ACLMessage(ACLMessage.FAILURE);
                                pedido.addReceiver(vendedores.get(i));
                                pedido.setContent("desligar");
                                myAgent.send(pedido);
                            }
                            
                            myAgent.doDelete();
                            
                            System.exit(0);
                        }
                       
                    }
                                    
                } else {
                    block();
                }
                            
                        
                break;
                          
            case 2: // envia mensagem para comprar o carro
                
                ACLMessage pedido = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                pedido.addReceiver(melhorVendedor);
                
                if (melhorVendedor == vendedorMatch)
                    pedido.setContent(String.valueOf(precoMatch));
                else
                    pedido.setContent(String.valueOf(melhorPreco));
                
                pedido.setReplyWith("compra" + System.currentTimeMillis());
                
                myAgent.send(pedido);
                        
                modeloResposta = MessageTemplate.and(MessageTemplate.MatchConversationId("compra-veiculo"),
                                                             MessageTemplate.MatchInReplyTo(pedido.getReplyWith()));
                        
                passo = 3;
                        
                System.out.println("Agente comprador enviou proposta ao Agente vendedor " + melhorVendedor.getName());
                        
                break;
                        
            case 3: // Recebe resposta do pedido de compra
                
                resposta = myAgent.receive();
                        
                if (resposta != null) {
                            
                    // caso a resposta seja do tipo INFORM, significa que a compra foi realizada
                    if (resposta.getPerformative() == ACLMessage.INFORM) {
                        // compra ok
                        System.out.println("Carro comprado com sucesso! Detalhes:");
                        
                        System.out.println("Nome: " + veiculoAtual.getNome());
                        System.out.println("Ano: " + veiculoAtual.getAno());
                        System.out.println("Cor: " + veiculoAtual.getCor());
                        System.out.println("Potência: " + veiculoAtual.getPotencia());
                        
                        if (veiculoAtual.isCambio())  
                            System.out.println("Câmbio: Automático");
                        else
                            System.out.println("Câmbio: Manual");
                        
                        System.out.println("Preço: " + veiculoAtual.getPreco());
                        
                        // Após a compra, mata os agentes
                        for (int i = 0; i < vendedores.size(); i++) {
                            pedido = new ACLMessage(ACLMessage.FAILURE);
                            pedido.addReceiver(vendedores.get(i));
                            pedido.setContent("desligar");
                            myAgent.send(pedido);
                        }
                            
                        myAgent.doDelete();
                            
                        System.exit(0);
                                
                    } else if (resposta.getPerformative() == ACLMessage.REFUSE) {
                        System.out.println("Erro!");
                    }
                            
                    passo = 4;
                            
                } else {
                    block();
                }
                        
                break;
                       
        }  
    }
}
