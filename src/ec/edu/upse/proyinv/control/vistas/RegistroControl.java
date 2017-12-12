package ec.edu.upse.proyinv.control.vistas;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Window;

import ec.edu.upse.proyinv.generales.Conexion;
import ec.edu.upse.proyinv.modelo.Canton;
import ec.edu.upse.proyinv.modelo.Carrera;
import ec.edu.upse.proyinv.modelo.CarreraPersona;
import ec.edu.upse.proyinv.modelo.Facultad;
import ec.edu.upse.proyinv.modelo.Pai;
import ec.edu.upse.proyinv.modelo.Parroquia;
import ec.edu.upse.proyinv.modelo.Persona;
import ec.edu.upse.proyinv.modelo.Privilegio;
import ec.edu.upse.proyinv.modelo.Provincia;
import ec.edu.upse.proyinv.modelo.Universidad;
import ec.edu.upse.proyinv.modelo.Usuario;
import ec.edu.upse.proyinv.modelo.UsuarioPrivilegio;
import lombok.Getter;
import lombok.Setter;

public class RegistroControl {
	
	
	@Wire
	private Window winRegistro; 
	
	@Getter @Setter private long idPais;
	
	@Getter @Setter private Pai pais;
	@Getter @Setter private Universidad universidad;
	@Getter @Setter private Persona persona;
	
	@Getter @Setter private Universidad universidadSeleccionada;
	@Getter @Setter private Facultad facultadSeleccionada;
	@Getter @Setter private Carrera carreraSeleccionada;
	
	@Getter @Setter private Pai paisSeleccionado;
	@Getter @Setter private Provincia provinciaSeleccionado;
	@Getter @Setter private Canton cantonSeleccionado;
	@Getter @Setter private Parroquia parroquiaSeleccionado;
	@Getter @Setter private Privilegio privilegioSeleccionado;
	
	@Getter @Setter public String txtnombre;
	@Getter @Setter public String txtapellido;
	@Getter @Setter public String txtcedula;
	@Getter @Setter public String txtemail;
	@Getter @Setter public String sexo;
	@Getter @Setter public String txtdireccion;
	
	
	@Getter @Setter public String txtusuario;
	@Getter @Setter public String password;
	@Getter @Setter public String retypedPassword;
	Conexion con=new Conexion();
	
	@Wire  Datebox txtfechanacimientoid;
	@Wire Radio radm;
	@Wire Checkbox chkterminos;
	
	@Command
	public Boolean submit(){
		/*validaciones del formulario
		 * 
		 */
		if(txtnombre==null ){
			Messagebox.show("LLenar Nombre!");
			return false;
		}
		if(txtapellido==null){
			Messagebox.show("LLenar Apellido!");
			return false;
		}
		if(txtcedula==null){
			Messagebox.show("LLenar Cedula!");
			return false;
		}
		if(txtcedula==null){
			Messagebox.show("LLenar Cedula!");
			return false;
		}
		if(txtemail==null){
			Messagebox.show("LLenar Email!");
			return false;
		}
		if(universidadSeleccionada==null){
			Messagebox.show("Elija Universidad!");
			return false;
		}
		if(facultadSeleccionada==null){
			Messagebox.show("Elija Facultad!");
			return false;
		}
		if(carreraSeleccionada==null){
			Messagebox.show("Elija Carrera!");
			return false;
		}
		if(paisSeleccionado==null){
			Messagebox.show("Elija Pais!");
			return false;
		}
		if(paisSeleccionado.getPais().equals("Ecuador")){
			System.out.println("aqui");
			if(provinciaSeleccionado==null || cantonSeleccionado==null || parroquiaSeleccionado==null){
				Messagebox.show("Elija Detalles de Residencia!");
				return false;
			}
		}
		
		if (txtusuario==null) {
			Messagebox.show("LLenar Usuario!");
			return false;
		}
		if(password==null ||retypedPassword==null){
			Messagebox.show("Error en las Contraseñas!");
			return false;
			
		}
		if(!password.equals(retypedPassword)){
			System.out.println(password+" "+retypedPassword);
			Messagebox.show("contraseñas no coinciden!");
			return false;
		}
		if(!chkterminos.isChecked()){
			Messagebox.show("Acepte terminos y Condiciones!");
			return false;
		}

				
		if (radm.isChecked()==true){sexo="Masculino";
		}else{sexo="Femenino";}
		

		/*
		 * crear objetos para guardar
		 * */
		
		//objeto Persona para ser guardado
		Persona per= new Persona();
		per.setApellido(txtapellido);
		per.setNombre(txtnombre);
		per.setCedula(txtcedula);
		per.setFechaNacimiento(txtfechanacimientoid.getValue());
		per.setEmail(txtemail);
		per.setSexo(sexo);
		per.setDireccion(txtdireccion);
		per.setParroquia(parroquiaSeleccionado);
		per.setNacionalidad(paisSeleccionado.getPais());
		per.setEstado("A");
		
		
		//guardar tabla por tabla: ojo(no debe ser asi!!!) ya que deberia guardar todo una sola... esto hay q corregir
		
		final String urlpersona=con.urlcompeta("/registro","/registrapersona/");
		RestTemplate restTemplatepersona = new RestTemplate();
		Persona resultpersona = restTemplatepersona.postForObject( urlpersona, per, Persona.class);
		System.out.println(resultpersona);
		
		
		//objeto CarreraPersona para ser guardado
		CarreraPersona carper=new CarreraPersona();
		carper.setCarrera(carreraSeleccionada);
		carper.setPersona(resultpersona);
		carper.setEstado("A");
				
		final String urlcarrerapersona=con.urlcompeta("/registro","/registracarrerapersona/");
		RestTemplate restTemplatecarrerapersona = new RestTemplate();
		CarreraPersona resultcarrerapersona = restTemplatecarrerapersona.postForObject( urlcarrerapersona, carper, CarreraPersona.class);
		System.out.println(resultcarrerapersona);
		
		//cambiar la clave a md5
		String passwordmd5;
		passwordmd5=getMD5(password);
		
		//Objeto Ususario para ser guardado
		Usuario usu= new Usuario();
		usu.setPersona(resultpersona);
		usu.setUsuario(txtusuario);
		usu.setClave(passwordmd5);
		usu.setEstado("A");
		
		final String urlusuario=con.urlcompeta("/registro","/registrausuario/");
		RestTemplate restTemplateusuario = new RestTemplate();
		Usuario resultusuario = restTemplateusuario.postForObject( urlusuario, usu, Usuario.class);
		System.out.println(resultusuario);
		
		
		getPrivilegios();
		//objeto UsuarioPrivilegio para ser guardado
		UsuarioPrivilegio usupri= new UsuarioPrivilegio();
		usupri.setUsuario(resultusuario);
		usupri.setPrivilegio(privilegioSeleccionado);
		usupri.setEstado("A");
		
		final String urlusuarioprivilegio=con.urlcompeta("/registro","/registrausuarioprivilegio/");
		RestTemplate restTemplateusuarioprivilegio = new RestTemplate();
		UsuarioPrivilegio resultusuarioprivilegio = restTemplateusuarioprivilegio.postForObject( urlusuarioprivilegio, usupri, UsuarioPrivilegio.class);
		System.out.println(resultusuarioprivilegio);
		
		
		Messagebox.show("Sus Datos han sido guardados con exito!");
		winRegistro.detach();
		return true;
			
	}
		 

	/*
	 * grabar
	 */
	@Command
	public void grabayenvia(@BindingParam("ventana")  Window ventana){
	}
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {

		// Permite enlazar los componentes que se asocian con la anotacion @Wire
		Selectors.wireComponents(view, this, false);
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");
	}

	public RegistroControl() {
	    // Recupera el objeto pasado como parametro. 
		persona = (Persona) Executions.getCurrent().getArg().get("Persona");
				
	}
	
	
	
	
	/**
	 * Retorna una lista de Universidades.
	 * @return
	 */
	public List<Universidad> getUniversidades() {
		
		String url=con.urlcompeta("/registro","/universidades");
		try {
			ArrayList<Universidad> universidadArrayList = new ArrayList<Universidad>();
			RestTemplate restTemplate=new RestTemplate();
			ResponseEntity<Universidad[]> response= 
					restTemplate.getForEntity(url, Universidad[].class);
			Arrays.asList(response.getBody())
	        .forEach(universidad -> universidadArrayList.add(universidad));		
			System.out.println(universidadArrayList);
			//System.out.println(universidadSeleccionada.getIdUniversidad());
			return universidadArrayList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
		
	}

	/*
	 * retorna la lista de facultades por universidad
	 */
	@Command
	@NotifyChange({"facultades"})
	public void buscafacultades(){}
	
	public List<Facultad> getFacultades(){
		if (universidadSeleccionada==null){
			//Messagebox.show("debe seleccionar una universidad");
			return null;
		}else{
			ArrayList<Facultad> facultadArrayList = new ArrayList<Facultad>();
			for (int i = 0; i < universidadSeleccionada.getFacultads().size(); i++) {
				facultadArrayList.add(universidadSeleccionada.getFacultads().get(i));
			}
			return facultadArrayList;
		}
	}
	
	/*
	 * retorna la lista de carreras por facultad
	 */
	@Command
	@NotifyChange({"carreras"})
	public void buscacarreras(){}
	
	public List<Carrera> getCarreras(){
		if (universidadSeleccionada==null && facultadSeleccionada==null){
			//Messagebox.show("debe seleccionar una universidad");
			return null;
		}else{
			ArrayList<Carrera> carreraArrayList = new ArrayList<Carrera>();
			for (int i = 0; i < facultadSeleccionada.getCarreras().size(); i++) {
				carreraArrayList.add(facultadSeleccionada.getCarreras().get(i));
			}
			return carreraArrayList;
		}
	}
	
	/**
	 * Retorna una lista de paises.
	 * @return
	 */
	public List<Pai> getPaises() {
		
		String url=con.urlcompeta("/registro","/paises");
		try {
			ArrayList<Pai> paisArrayList = new ArrayList<Pai>();
			RestTemplate restTemplate=new RestTemplate();
			ResponseEntity<Pai[]> response= 
					restTemplate.getForEntity(url, Pai[].class);
			Arrays.asList(response.getBody())
	        .forEach(pais -> paisArrayList.add(pais));		
			System.out.println(paisArrayList);
			return paisArrayList;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/*
	 * Retorna la lista de provincias por pais
	 */
	
	@Command
	@NotifyChange({"provincias"})
	public void buscaprovincias(){}
	
	public List<Provincia> getProvincias(){
		if (paisSeleccionado==null){
			//Messagebox.show("debe seleccionar una universidad");
			return null;
		}else{
			ArrayList<Provincia> provinciaArrayList = new ArrayList<Provincia>();
			for (int i = 0; i < paisSeleccionado.getProvincias().size(); i++) {
				provinciaArrayList.add(paisSeleccionado.getProvincias().get(i));
			}
			return provinciaArrayList;
		}
	}
	
	/*
	 * retorna la lista de cantones por provincias
	 */
	
	@Command
	@NotifyChange({"cantones"})
	public void buscacantones(){}
	
	public List<Canton> getCantones(){
		if (provinciaSeleccionado==null){
			//Messagebox.show("debe seleccionar una universidad");
			return null;
		}else{
			ArrayList<Canton> cantonArrayList = new ArrayList<Canton>();
			for (int i = 0; i < provinciaSeleccionado.getCantons().size(); i++) {
				cantonArrayList.add(provinciaSeleccionado.getCantons().get(i));
			}
			return cantonArrayList;
		}
	}
	/*
	 * retorna la lista de parroquias por cantones
	 */
	@Command
	@NotifyChange({"parroquias"})
	public void buscaparroquia(){}
	public List<Parroquia> getParroquias(){
		if (cantonSeleccionado==null){
			//Messagebox.show("debe seleccionar una universidad");
			return null;
		}else{
			ArrayList<Parroquia> parroquiaArrayList = new ArrayList<Parroquia>();
			for (int i = 0; i < cantonSeleccionado.getParroquias().size(); i++) {
				parroquiaArrayList.add(cantonSeleccionado.getParroquias().get(i));
			}
			//persona.setParroquia(parroquiaSeleccionado);
			return parroquiaArrayList;
		}
	}
	
	public void getPrivilegios(){
		String url=con.urlcompeta("/registro","/privilegios");
		try {
			ArrayList<Privilegio> privilegioArrayList = new ArrayList<Privilegio>();
			RestTemplate restTemplate=new RestTemplate();
			ResponseEntity<Privilegio[]> response= 
					restTemplate.getForEntity(url, Privilegio[].class);
			Arrays.asList(response.getBody())
	        .forEach(privilegio -> privilegioArrayList.add(privilegio));		
			privilegioSeleccionado=privilegioArrayList.get(1);
			//System.out.println(privilegioSeleccionado);
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
	}


	
	/*
	 * codigo para consumir un solo objeto json
	 */
	public void consultarUnPaises(){
		try {
			RestTemplate restTemplate=new RestTemplate();
			System.out.println(idPais);
			pais=restTemplate.getForObject("http://localhost:8080/AplicacionInvestigadores-WS/api/pais/buscaporid/"+idPais, Pai.class);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		//System.out.println(pais);
	}
	
	/*
	 * codigo para consumir un arreglo de json
	 */
	public void consultarPaises(){
		try {
			RestTemplate restTemplate=new RestTemplate();
			
			ResponseEntity<Pai[]> response= 
					restTemplate.getForEntity("http://localhost:8080/AplicacionInvestigadores-WS/api/pais/", Pai[].class);
			
			Arrays.asList(response.getBody())
            .forEach(pais -> System.out.println(pais));
			
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String getMD5(String input) {
		 try {
			 MessageDigest md=MessageDigest.getInstance("MD5");
				md.update(input.getBytes(),0,input.length());
				String inputmd5=new BigInteger(1,md.digest()).toString(16);
				return inputmd5;
		 }
		 catch (NoSuchAlgorithmException e) {
		 throw new RuntimeException(e);
		 }
		 }

	
}
