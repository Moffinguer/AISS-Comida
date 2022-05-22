package aiss.api.resources;
import java.util.Random;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.jboss.resteasy.client.ClientRequest;
import java.util.logging.*;
import org.restlet.resource.ClientResource;

import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.IngredienteExterno;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;
import aiss.model.TipoDieta;
public class IngredienteResource {
	private static final String FOOD_API_URI = "https://food-api-349515.appspot.com/api/ingredients";
	private static final Logger Log = Logger.getLogger(IngredienteExterno.class.getName());
	private static IngredienteResource _instance=null;
	public static Collection<IngredienteExterno> getIngredientes(){
		Log.log(Level.FINE, "URI:" + FOOD_API_URI);
		ClientResource cr = new ClientResource(FOOD_API_URI);
		IngredienteExterno[] search = cr.get(IngredienteExterno[].class);
		return Arrays.asList(search);
	}
	public static Collection<Alimento> getIngredientesExternos(){
		Collection<IngredienteExterno> ingredientesExternos = IngredienteResource.getIngredientes();
		Collection<Alimento> alimentosExternos = new LinkedList<Alimento>();
		Random rnd = new Random();
		for(IngredienteExterno ie: ingredientesExternos) {	
			rnd.ints(0, Alergeno.values().length);
			Alimento tempAlimento = new Alimento();
			tempAlimento.setNombre(ie.getNombre());
			tempAlimento.setCalorias(ie.getCalorias100gr());
			tempAlimento.setAlergeno(Alergeno.values()[rnd.ints(0, Alergeno.values().length).findFirst().getAsInt()]);
			tempAlimento.setCategoria(Categoria.values()[rnd.ints(0, Categoria.values().length).findFirst().getAsInt()]);
			tempAlimento.setTemporada(Temporada.values()[rnd.ints(0, Temporada.values().length).findFirst().getAsInt()]);
			tempAlimento.setTipo(TipoAlimento.values()[rnd.ints(0, TipoAlimento.values().length).findFirst().getAsInt()]);
			alimentosExternos.add(tempAlimento);
		}
		return alimentosExternos;
	}
	
}
