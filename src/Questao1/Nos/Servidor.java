package Questao1.Nos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class Servidor implements Runnable{
	public Socket Nocliente = null, noEnvio = null;
	Thread escutaVizinho;

	Boolean criarTok = false, atribuiuProximoaoPrimeiro = true;
	int index;

	public Servidor(Socket client, int index) throws IOException{
		this.Nocliente = client;
		this.index = index;
		System.out.println("No servidor: "+TokenServer.listaDeNos.size()+" index "+index);

		escutaVizinho = new Thread(new Runnable() {			
			@Override
			public void run() {				
				try {
					Object rcv;
					while(true){
						if(TokenServer.listaDeNos.size() > 1) {
							System.out.println("ok "+index);
						
							if(index == TokenServer.listaDeNos.size()-1 && noEnvio == null) {
								Nocliente = TokenServer.listaDeNos.get(index-1);
								noEnvio = TokenServer.listaDeNos.get(0);
								System.out.println("1- "+noEnvio);
							}
							ObjectInputStream is = new ObjectInputStream(Nocliente.getInputStream());
							rcv = is.readObject();

							//Repassar objeto
							System.out.println(rcv);
							ObjectOutputStream oss=new ObjectOutputStream(TokenServer.listaDeNos.get(1).getOutputStream()); 
							oss.writeObject(rcv);

							
						}else {
							//System.out.println("shit");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		escutaVizinho.start();

	}

	public void atualizarVizinhoZero() throws IOException {
		noEnvio = TokenServer.listaDeNos.get(index+1);
		criarToken();
		System.out.println("2- "+noEnvio);
	}
	
	public void atualizarVizinho() {
		noEnvio = TokenServer.listaDeNos.get(index+1);
		System.out.println("2- "+noEnvio);
	}
	
	public void criarToken() throws IOException {
			ObjectOutputStream oss2=new ObjectOutputStream(noEnvio.getOutputStream());
			oss2.writeObject(criarTok);
	}

	public Socket getCliente() {
		return Nocliente;
	}


	public void run(){

	}

}