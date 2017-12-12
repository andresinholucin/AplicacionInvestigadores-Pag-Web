package ec.edu.upse.proyinv.generales;

import lombok.Getter;

public class Conexion {
	@Getter private String puerto="8080";
	@Getter private String direccion="192.168.44.169";
	@Getter private String aplicacion="/AplicacionInvestigadores-WS/api/";
	
	
	
	public Conexion(){}
	
	public String urlcompeta(String ws,String metodo){
		String url="http://"+direccion+":"+puerto+aplicacion+ws+metodo;
		return url;
	}
}
