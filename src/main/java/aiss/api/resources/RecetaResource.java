package aiss.api.resources;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;


import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import aiss.model.RecetaExterna;

public class RecetaResource {
	private static final String FOOD_API_URI = "https://food-api-349515.appspot.com/api/recipes";
	private static final Logger Log = Logger.getLogger(RecetaExterna.class.getName());
	
	public static Collection<RecetaExterna> getRecetas(){
		Log.log(Level.FINE, "URI:" + FOOD_API_URI);
		ClientResource cr = new ClientResource(FOOD_API_URI);
		RecetaExterna[] search = cr.get(RecetaExterna[].class);
		return Arrays.asList(search);
	}
	

	public static boolean updateReceta(RecetaExterna receta, String API_KEY) {
		ClientResource cr = null;
		boolean success = true;
		
		try {
			cr = new ClientResource(FOOD_API_URI + "?token=" + API_KEY);
			cr.setEntityBuffering(true);		// Needed for using RESTlet from JUnit tests
			cr.put(receta);			
		} catch (ResourceException re) {
			System.err.println("Error al actualizar la receta: " + cr.getResponse().getStatus());
			success = false;
		}
		
		return success;
	}


	public RecetaExterna addReceta(RecetaExterna receta, String API_KEY) {
		ClientResource cr = null;
		RecetaExterna resultReceta = null;
		try {
			cr = new ClientResource(FOOD_API_URI + "?token=" + API_KEY);
			cr.setEntityBuffering(true);		// Needed for using RESTlet from JUnit tests
			resultReceta = cr.post(receta,RecetaExterna.class);
			
		} catch (ResourceException re) {
			System.err.println("Error cuando se suma la receta: " + cr.getResponse().getStatus());
		}
		
		return resultReceta;
	}
	
	public boolean deleteReceta(String recetaId, String API_KEY) {
		ClientResource cr = null;
		boolean success = true;
		try {
			cr = new ClientResource(FOOD_API_URI + "/" + recetaId + "?token=" + API_KEY);
			cr.setEntityBuffering(true);		// Needed for using RESTlet from JUnit tests
			cr.delete();
			
		} catch (ResourceException re) {
			System.err.println("Error cuando se borra la receta: " + cr.getResponse().getStatus());
			success = false;
		}
		
		return success;
	}

}
