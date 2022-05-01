package aiss.model.resources;

import static org.junit.Assert.*;

import java.util.Collection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.ResourceException;

import aiss.api.resources.AlimentoResource;
import aiss.api.resources.DietaResource;
import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;
import aiss.model.repository.MapDietaRepository;
public class AlimentoResourceTest {

	static Alimento alimento, alimento2, alimento3, alimento4;
	static AlimentoResource ar = new AlimentoResource();
	static MapDietaRepository plr = new MapDietaRepository();
	private static int id = 50;
	@BeforeClass
	public static void setUp() throws Exception {
		
		alimento = new Alimento((id++)+"al",
				"Merluza",
				150.75,
				Categoria.PESCADO,
				TipoAlimento.PROTEINAS,
				Alergeno.PESCADO,
				Temporada.VERANO
				);
		alimento2 = new Alimento((id++)+"al",
				"Chorizo",
				2000.03,
				Categoria.CARNE,
				TipoAlimento.PROTEINAS,
				null,
				Temporada.OTOÑO
				);
		alimento3 = new Alimento((id++)+"al",
				"Manzana",
				100.13,
				Categoria.FRUTAS,
				TipoAlimento.SALESMINERALES,
				null,
				null
				);
		alimento4 = new Alimento((id++)+"al",
				"Huevo",
				150.69,
				Categoria.HUEVOS,
				null,
				Alergeno.HUEVOS,
				null
				);
		plr.addAlimento(alimento);
		plr.addAlimento(alimento2);
		plr.addAlimento(alimento3);
		plr.addAlimento(alimento4);
		
	}

	@AfterClass
	public static void tearDown() throws Exception {
		plr.deleteAlimento(alimento.getId());
		plr.deleteAlimento(alimento2.getId());
		plr.deleteAlimento(alimento3.getId());
		plr.deleteAlimento(alimento4.getId());
	}
	@Test
	public void testGetListOfAler() {
		assertTrue("No han salido los alergenos esperados", ar.getListOfAler().size() > 0);
		System.out.println(ar.getListOfAler());
	}
	
}
