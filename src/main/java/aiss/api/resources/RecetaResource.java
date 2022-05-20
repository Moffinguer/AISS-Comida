package aiss.api.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import aiss.model.RecetaExterna;

public class RecetaResource {
	private static final String FOOD_API_URI = "https://food-api-349515.appspot.com/api/recipes";
	
	@PUT 
	@Consumes("application/json")
	public boolean updateReceta(RecetaExterna receta) {
		ClientResource cr = null;
		boolean success = true;
		
		try {
			cr = new ClientResource(FOOD_API_URI);
			cr.setEntityBuffering(true);		// Needed for using RESTlet from JUnit tests
			cr.put(receta);			
		} catch (ResourceException re) {
			System.err.println("Error al actualizar la receta: " + cr.getResponse().getStatus());
			success = false;
		}
		
		return success;
	}
	public Response deleteReceta() {
		
	}

}
