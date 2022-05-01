package aiss.model.resources;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.resource.ResourceException;

import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Dieta;
import aiss.model.Ingrediente;
import aiss.model.Plato;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;
import aiss.model.TipoDieta;
import aiss.model.repository.MapDietaRepository;

public class DietaResourceTest {
	static Alimento alimento, alimento2, alimento3, alimento4;
	static Dieta dieta;
	static Plato plato1, plato2;
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
		List<Ingrediente> ingrediente2 = new LinkedList<Ingrediente>();
		ingrediente2.add(new Ingrediente(alimento2, 500.));
		plato1 = new Plato((id++)+"pl",
				"Chorizo Picante",
				ingrediente2,
				"Zamora",
				"Leon",
				null);
		alimento4 = new Alimento((id++)+"al",
				"Huevo",
				150.69,
				Categoria.HUEVOS,
				null,
				Alergeno.HUEVOS,
				null
				);
		List<Ingrediente> ingrediente1 = new LinkedList<Ingrediente>();
		ingrediente1.add(new Ingrediente(alimento, 500.));
		ingrediente1.add(new Ingrediente(alimento4, 50.));
		plato1 = new Plato((id++)+"pl",
				"Merluza con Huevos",
				ingrediente1,
				"Cadiz",
				"Andalucia",
				null);
		List<Plato> platos1 = new LinkedList<>();
		platos1.add(plato1);
		dieta = new Dieta((id++)+"di",
				"La dieta de los huevos",
				"La de los lunes"
				, platos1,
				TipoDieta.PESCETARIANA);
		plr.addAlimento(alimento);
		plr.addAlimento(alimento2);
		plr.addAlimento(alimento3);
		plr.addAlimento(alimento4);
		plr.addPlato(plato1);
		plr.addPlato(plato2);
		plr.addDieta(dieta);
	}
	public static void tearDown() throws Exception {
		plr.deletePlato(plato1.getId());
		plr.deletePlato(plato2.getId());
		plr.deleteAlimento(alimento.getId());
		plr.deleteAlimento(alimento2.getId());
		plr.deleteAlimento(alimento3.getId());
		plr.deleteAlimento(alimento4.getId());
		plr.deleteDieta(dieta.getId());
	}
	@Test
	public void testUpdateDieta() {
		
	}
}