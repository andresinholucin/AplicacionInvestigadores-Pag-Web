package ec.edu.upse.proyinv.generales;

import java.util.Map;

import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

public class Validaciones extends AbstractValidator{
	
	public void Validaciones(ValidationContext ctx) {
		 Map<String,Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());
		 validatePasswords(ctx, (String)beanProps.get("password").getValue(), (String)ctx.getValidatorArg("retypedPassword"));      
	}
	

	private void validatePasswords(ValidationContext ctx, String password, String retype) {
		System.out.println("llegaste");
		if(password == null || retype == null || (!password.equals(retype))) {
            this.addInvalidMessage(ctx, "password", "Las Contraseñas no son Iguales!");
        }
	}


	public boolean validapassword(String p1, String p2 ){
		if(p1==p2){
			return true;
		}else{
			return false;
		}
	}
	
	public void validacatchap(){
		
	}


	@Override
	public void validate(ValidationContext ctx) {
		// TODO Auto-generated method stub
		
	}



}
