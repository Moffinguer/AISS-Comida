package aiss.model.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import aiss.api.resources.PerfilResource;
import aiss.api.resources.RecetaResource;
import aiss.model.PerfilExterno;
import aiss.model.RecetaExterna;

public class TestRecetaExterna {
	
	public RecetaExterna recetaAux= new RecetaExterna(),
		                 recetaPost= new RecetaExterna();
	static RecetaResource resourceReceta=new RecetaResource();
	static PerfilResource resourcePerfil = new PerfilResource();
	public static String API_KEY;
	@BeforeClass
	public static void getKey() {
		String messageKey = resourcePerfil.addProfile(new PerfilExterno(
				"AissProyect",
				"618587795",
				"aissProyect@us.es")
				);
		String api_key = resourcePerfil.getKey();
		assertNotNull("No hemos creado una clave, mal formato a la hora de hacer la petición", messageKey);
		assertNotNull("No se ha podido recuperar la clave para operar con nuestras recetas", api_key);
		System.out.println(api_key);
		API_KEY = api_key;
	}
	
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
	
//	@Test
//	public void testAddPlaylist() {
//			recetaAux.setId("r3");
//			recetaAux.setCalorias("203");
//			recetaAux.setDescripcion("pizza con tomate");
//			recetaAux.setCategoria("italiana");
//			recetaAux.setPersonas("3");
//			recetaAux.setPrecio("2");
//			recetaAux.setTiempo("90");
//			recetaAux.setNombre("pizza");
//			
//			recetaPost = resourceReceta.addReceta(recetaAux, API_KEY);
//			
//			assertNotNull("Error al sumar la receta", recetaPost);
//			assertEquals("Error en el id de la receta", "r3", recetaPost.getId());
//			assertEquals("Error en el nombre de la receta", "pizza", recetaPost.getNombre());
//			assertEquals("Error en las calorías de la receta", "203", recetaPost.getCalorias());
//			assertEquals("Error en la descripcion de la receta", "pizza con tomate", recetaPost.getDescripcion());
//			assertEquals("Error en la categoría de la receta", "italiana", recetaPost.getCategoria());
//			assertEquals("Error en las personas de la receta", "3", recetaPost.getPersonas());
//			assertEquals("Error en el precio de la receta", "2", recetaPost.getPrecio());
//			assertEquals("Error en el tiempo de la receta", "90", recetaPost.getTiempo());
//			
//		}
	
	@Test
	public void testUpdateReceta() {
		RecetaExterna receta = RecetaResource.getRecetas().stream().collect(Collectors.toList()).get(0);
		receta.setNombre("new name");
		receta.setTiempo("82");
//		receta.setPrecio("100");
//		receta.setCalorias("110");
		receta.setPersonas("1");
		receta.setCategoria("nice food");
		receta.setDescripcion("this is a new description for the recipe");
		
		boolean success = RecetaResource.updateReceta(receta, API_KEY);
		assertTrue("Error when updating the recipe", success);
		
		RecetaExterna updatedReceta = null;
		for (RecetaExterna r : RecetaResource.getRecetas().stream().collect(Collectors.toList())) {
			if(r.getId().equals(receta.getId())) {
				assertEquals("The field name does not match", receta.getNombre(), r.getNombre());
				assertEquals("The field time does not match", receta.getTiempo(), r.getTiempo());
//				assertEquals("The field price does not match", receta.getPrecio(), r.getPrecio());
//				assertEquals("The field calories does not match", receta.getCalorias(), r.getCalorias());
				assertEquals("The field people does not match", receta.getPersonas(), r.getPersonas());
				assertEquals("The field category does not match", receta.getCategoria(), r.getCategoria());
				assertEquals("The field description does not match", receta.getDescripcion(), r.getDescripcion());
				updatedReceta = r;
				break;
			}
		}
		
		//Show Result
		assertNotNull("The updated Recipe is null",updatedReceta);
		System.out.println("\nPrinting the updated Recipe:");
		System.out.println(updatedReceta.getNombre());
		System.out.println(updatedReceta.getTiempo());
//		System.out.println(updatedReceta.getPrecio());
//		System.out.println(updatedReceta.getCalorias());
		System.out.println(updatedReceta.getPersonas());
		System.out.println(updatedReceta.getCategoria());
		System.out.println(updatedReceta.getDescripcion());
		
	}
		
}