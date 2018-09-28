package compraEvenda;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.*;
import java.util.Scanner;


/**
 *
 * @author pedrowarmlingbotelho
 */
public class Main {

    public static void main(String[] args) {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl();
        
        ContainerController contController = runtime.createMainContainer(profile);
        
        AgentController agentController;
        AgentController rma;
        
        String ano, potencia, cor, nome, cambio, preco;
        
        try {
            
            String vei1 = "2010;120;Amarelo;Fiat;false;19000",
                   vei2 = "2017;220;Preto;Chevrolet;true;63000",
                   vei3 = "2015;150;Branco;Ford;true;49000", 
                   vei4 = "2001;80;Cinza;Volkswagem;false;8000",
                   vei5 = "2018;300;Branco;Ford;true;80000",
                   vei6 = "2013;180;Preto;Fiat;true;38000",
                   vei7 = "2014;140;Azul;Chevrolet;true;32000",
                   vei8 = "2008;110;Vermelho;Ford;false;18000",
                   vei9 = "2011;150;Cinza;Volkswagem;true;30000",
                   vei10 = "2009;130;Amarelo;Ford;false;23000",
                   vei11 = "2015;180;Preto;Chevrolet;true;51000",
                   vei12 = "2017;210;Branco;Ford;true;64000",
                   vei13 = "2018;250;Vermelho;Volkswagem;true;72000",
                   vei14 = "2014;150;Cinza;Fiat;true;38000",
                   vei15 = "2016;170;Preto;Chevrolet;true;54000",
                   vei16 = "2010;100;Vermelho;Fiat;false;29000";
            
            String v1[] = {vei1, vei4, vei6, vei8, vei10, vei12, vei14, vei16};
            String v2[] = {vei2, vei3, vei5, vei7, vei9, vei11, vei13, vei15};
            
            System.out.println("Bem vindo, preencha os atributos abaixo");
            Scanner teclado = new Scanner(System.in);
            System.out.print("Ano: ");
            ano = teclado.nextLine();
            System.out.print("Potencia: ");
            potencia = teclado.nextLine();
            System.out.print("Cor: ");
            cor = teclado.nextLine();
            System.out.print("Nome: ");
            nome = teclado.nextLine();
            
            System.out.print("Cambio(1-Automático/0-Manual): ");
            cambio = teclado.nextLine();
            if ("1".equals(cambio))
                cambio = "true";
            else
                cambio = "false";
            
            System.out.print("Preço: ");
            preco = teclado.nextLine();

            String v3[] = {ano+";"+potencia+";"+cor+";"+nome+";"+cambio+";"+preco};
            
            rma = contController.createNewAgent("rma", "jade.tools.rma.rma", null);
            
            agentController = contController.createNewAgent("Vendedor_1", "compraEvenda.vendedor.Vendedor", v1);
            agentController.start();
            
            agentController = contController.createNewAgent("Vendedor_2", "compraEvenda.vendedor.Vendedor", v2);
            agentController.start();
            
            agentController = contController.createNewAgent("Comprador", "compraEvenda.comprador.Comprador", v3);
            agentController.start();
            
            rma.start();
        } catch (StaleProxyException ex) {
            System.out.println("Erro na Main: " + ex);
        }
    }
    
}
