package Questao1.Nos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class TokenServer {
	static int cont=0;
	static LinkedList<Socket> listaDeNos = new LinkedList<Socket>();
	LinkedList<Servidor> servidores = new LinkedList<Servidor>();

	public TokenServer() throws IOException {
		//ServerSocket ouvindo a porta 12345, aceitando novos clientes
				ServerSocket token;
				token = new ServerSocket(12345);
				System.out.println("Servidor ouvindo a porta 12345\n\n");
				while(true) {
					// o método accept() bloqueia aO execução até que o servidor receba 3 pedido de conexão			
					Socket cliente = token.accept();
					System.out.println("Cliente conectado\n\n");
					listaDeNos.add(cliente);	
					cont++;
					Servidor s= new Servidor(cliente, cont-1);
					servidores.add(s);

					if(cont == 2) {	
						servidores.get(cont-2).atualizarVizinhoZero();
					}
					else if(cont>2) {
						servidores.get(cont-2).atualizarVizinho();
					}
					
					if(cont == 2) {
						//s.criarToken();
					}
					Thread t=new Thread(s);
					t.start();
				}
	}
	
	public static void main(String [] args) throws IOException {
		new TokenServer();
	}

	public static int getTamanho() {
		cont= listaDeNos.size();
		return cont;
	}

	public int proximaPorta() {
		return listaDeNos.size();
	}
}
