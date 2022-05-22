package aiss.api.resources;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import aiss.model.PerfilExterno;
import aiss.model.RecetaExterna;

public class PerfilResource {
	private String API_KEY;
	private static final String FOOD_API_URI = "https://food-api-349515.appspot.com/api/myprofile";
	
	public String addProfile(PerfilExterno perfil) {
		ClientResource cr = null;
		String resultPerfil = null;
		try {
			cr = new ClientResource(FOOD_API_URI);
			cr.setEntityBuffering(true);		// Needed for using RESTlet from JUnit tests
			resultPerfil = cr.post(perfil,String.class);
			String[] dumb = resultPerfil.split(" ");
			API_KEY = dumb[dumb.length - 1];
		} catch (ResourceException re) {
			System.err.println("No se ha podido obtener una clave de producto");
		}
		return resultPerfil;
	}
	
	public String getKey() {
		return API_KEY;
	}
	
}
