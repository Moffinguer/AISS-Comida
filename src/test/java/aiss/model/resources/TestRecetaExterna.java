package aiss.model.resources;

import static org.junit.Assert.assertNotNull;

import java.util.Collection;

import org.junit.Test;

import aiss.api.resources.RecetaResource;
import aiss.model.RecetaExterna;

public class TestRecetaExterna {
	@Test
	public void testGetAll() {
		Collection<RecetaExterna> recetasExternas = RecetaResource.getRecetas();
		assertNotNull("The collection of external Recipes is null", recetasExternas);
		
		// Show result
		System.out.println("Listing all external Recipes:");
		for(RecetaExterna re : recetasExternas) {
			System.out.println(re.getNombre());
		}
	}
}