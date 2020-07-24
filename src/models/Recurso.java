package models;

import java.io.Serializable;

public class Recurso implements Serializable{
	String conteudo;
	public Recurso() {
		
	}
	public Recurso(String conteudo) {
		super();
		this.conteudo = conteudo;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
