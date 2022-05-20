package aiss.api.resources;
import java.util.Random;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.jboss.resteasy.client.ClientRequest;
import java.util.logging.*;
import org.restlet.resource.ClientResource;

import aiss.model.Alimento;
import aiss.model.IngredienteExterno;
public class IngredienteResource {
	private static final String FOOD_API_URI = "https://food-api-349515.appspot.com/api/ingredients";
	private static final Logger Log = Logger.getLogger(IngredienteExterno.class.getName());
	
	public static Collection<IngredienteExterno> getIngredientes(){
		Log.log(Level.FINE, "URI:" + FOOD_API_URI);
		ClientResource cr = new ClientResource(FOOD_API_URI);
		IngredienteExterno[] search = cr.get(IngredienteExterno[].class);
		return Arrays.asList(search);
	}
	
	public Collection<Alimento> getInstance(){
		Collection<IngredienteExterno> ingredientesExternos = IngredienteResource.getIngredientes();
		Collection<Alimento> alimentosExternos = new LinkedList<Alimento>();
		Random rnd = new Random();
		for(IngredienteExterno ie: ingredientesExternos) {	
			Alimento tempAlimento = new Alimento();
			tempAlimento.setNombre(ie.getNombre());
			tempAlimento.setCalorias(Double.parseDouble(ie.getCalorias100gr()));
			tempAlimento.set
			alimentosExternos.add(tempAlimento);
		}
		return null;
	}
	
}
