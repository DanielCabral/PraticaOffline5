package TerceiraTentativa;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import Questao1.Token.ObjetoSocket;

public class TokenServer {
	static int cont=0;
	static LinkedList<Socket> listaDeNos = new LinkedList<Socket>();

	public static void main(String [] args) throws IOException {
		//ServerSocket ouvindo a porta 12345, aceitando novos clientes
		ServerSocket token;
		token = new ServerSocket(12345);
		System.out.println("Servidor ouvindo a porta 12345\n\n");
		while(cont<4) {
			// o método accept() bloqueia aO execução até que o servidor receba 3 pedido de conexão			
		    Socket cliente = token.accept();
		    System.out.println("Cliente conectado\n\n");
		    listaDeNos.add(cliente);	
		    cont++;
		    Servidor s= new Servidor(cliente, cont);
		    Thread t=new Thread(s);
		    t.start();
		}
		iniciarTopologiaAnel();
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t2.start();
	}
	
	public static void iniciarTopologiaAnel() throws IOException {
		for(int i=0;i<4;i++) {
			System.out.println("Processo "+(i+1));
			System.out.println("Com servidor na porta: 500"+(i+1));
			if(i == 3 ) {
				System.out.println("conectar na porta 5001");
				InetAddress addr = listaDeNos.get(0).getInetAddress();
				ObjetoSocket dadosDeEnderecoEPorta = new ObjetoSocket(addr.getHostAddress() , (5001));
				
				//Fluxo de envio para o NO n-1
				ObjectOutputStream oss=new ObjectOutputStream(listaDeNos.get(i).getOutputStream());
			 	//Enviar para NO n-1, os dados com endereco e porta do servidor que o no seguinte criou
				oss.writeObject(dadosDeEnderecoEPorta);
			}else {
				System.out.println("conectar na porta 500"+(i+2));
				InetAddress addr = listaDeNos.get(i+1).getInetAddress();
				ObjetoSocket dadosDeEnderecoEPorta = new ObjetoSocket(addr.getHostAddress() , (5000+i+2));
				
				//Fluxo de envio para o NO n-1
				ObjectOutputStream oss=new ObjectOutputStream(listaDeNos.get(i).getOutputStream());
			 	//Enviar para NO n-1, os dados com endereco e porta do servidor que o no seguinte criou
				oss.writeObject(dadosDeEnderecoEPorta);
			}
		}
	}
	
	public static int getTamanho() {
		cont= listaDeNos.size();
		return cont;
	}
	
	public int proximaPorta() {
		return listaDeNos.size();
	}
}
