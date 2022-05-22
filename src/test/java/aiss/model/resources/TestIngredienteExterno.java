package aiss.model.resources;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import aiss.api.resources.IngredienteResource;
import aiss.model.Alimento;
import aiss.model.IngredienteExterno;
import aiss.model.repository.MapDietaRepository;

public class TestIngredienteExterno {
	@Test
	public void testGetAll() {
		Collection<IngredienteExterno> ingredientesExternos = IngredienteResource.getIngredientes();
		assertNotNull("The collection of external Ingredients is null", ingredientesExternos);
		
		// Show result
		System.out.println("Listing all external Ingredients:");
		for(IngredienteExterno ie : ingredientesExternos) {
			System.out.println(ie.getNombre());
		}
	}
}
