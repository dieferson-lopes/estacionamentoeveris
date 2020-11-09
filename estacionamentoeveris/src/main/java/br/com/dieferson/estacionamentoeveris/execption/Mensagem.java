package br.com.dieferson.estacionamentoeveris.execption;

/**
 * @author dsantolo
 */
public class Mensagem {

	private String campo;
	private String msg;
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Mensagem(String campo, String msg) {
		super();
		this.campo = campo;
		this.msg = msg;
	}
	
}
