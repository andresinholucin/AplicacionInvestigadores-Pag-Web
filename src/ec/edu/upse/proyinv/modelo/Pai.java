package ec.edu.upse.proyinv.modelo;


import java.util.List;

import lombok.Getter;
import lombok.Setter;


public class Pai{
	
	@Getter @Setter private Integer idPais;
	
	@Getter @Setter private String pais;

	@Getter @Setter private String estado;

	@Getter @Setter private List<Provincia> provincias;
	
	@Override
	public String toString() {
		return "[idPais=" + idPais + ", pais=" + pais + ", estado=" + estado + "]";
	}
	
	
}
