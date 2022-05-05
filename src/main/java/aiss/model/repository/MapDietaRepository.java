package aiss.model.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Dieta;
import aiss.model.Ingrediente;
import aiss.model.Plato;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;
import aiss.model.TipoDieta;

public class MapDietaRepository implements DietaRepository{
	
	Map<String, Alimento> alimentoMap = new HashMap<String, Alimento>();
	Map<String, Plato> platoMap = new HashMap<String, Plato>();
	Map<String, Dieta> dietaMap = new HashMap<String, Dieta>();
	private static MapDietaRepository instance=null;
	private int index=0;
	
	public static MapDietaRepository getInstance() {
		
		if (instance==null) {
			instance = new MapDietaRepository();
			instance.init();
		}
		
		return instance;
	}
	
	public void init() {
		Alimento alimento, alimento2, alimento3, alimento4;
		Dieta dieta;
		Plato plato1, plato2;
		alimento = new Alimento(
				"Merluza",
				150.75,
				Categoria.PESCADO,
				TipoAlimento.PROTEINAS,
				Alergeno.PESCADO,
				Temporada.VERANO
				);
		alimento2 = new Alimento(
				"Chorizo",
				2000.03,
				Categoria.CARNE,
				TipoAlimento.PROTEINAS,
				Alergeno.SOJA,
				Temporada.OTOÑO
				);
		alimento3 = new Alimento(
				"Manzana",
				100.13,
				Categoria.FRUTAS,
				TipoAlimento.SALESMINERALES,
				Alergeno.LECHE,
				Temporada.JULIO
				);
		List<Ingrediente> ingrediente2 = new LinkedList<Ingrediente>();
		ingrediente2.add(new Ingrediente(alimento2, 500.));
		plato2 = new Plato(
				"Chorizo Picante",
				ingrediente2,
				"Zamora",
				"Leon",
				Temporada.DICIEMBRE);
		alimento4 = new Alimento(
				"Huevo",
				150.69,
				Categoria.HUEVOS,
				TipoAlimento.PROTEINAS,
				Alergeno.HUEVOS,
				Temporada.PRIMAVERA
				);
		List<Ingrediente> ingrediente1 = new LinkedList<Ingrediente>();
		ingrediente1.add(new Ingrediente(alimento, 500.));
		ingrediente1.add(new Ingrediente(alimento4, 50.));
		plato1 = new Plato(
				"Merluza con Huevos",
				ingrediente1,
				"Cadiz",
				"Andalucia",
				Temporada.ABRIL);
		List<Plato> platos1 = new LinkedList<>();
		platos1.add(plato1);
		dieta = new Dieta(
				"La dieta de los huevos",
				"La de los lunes"
				, platos1,
				TipoDieta.PESCETARIANA);
		instance.addAlimento(alimento);
		instance.addAlimento(alimento2);
		instance.addAlimento(alimento3);
		instance.addAlimento(alimento4);
		instance.addPlato(plato1);
		instance.addPlato(plato2);
		instance.addDieta(dieta);
		alimento4 = new Alimento(
				"Leche",
				150.69,
				Categoria.lACTEOS,
				TipoAlimento.LIPIDOS,
				Alergeno.LECHE,
				Temporada.PRIMAVERA
				);
		instance.addAlimento(alimento4);
	}
	

	@Override
	public void addAlimento(Alimento a) {
		a.setId((index)+"al");
		alimentoMap.put((index++)+"al", a);
	}

	@Override
	public Collection<Alimento> getAllAlimentos() {
		// TODO Auto-generated method stub
		return alimentoMap.values();
	}

	@Override
	public Alimento getAlimento(String alimentoId) {
		// TODO Auto-generated method stub
		return alimentoMap.get(alimentoId);
	}

	@Override
	public void updateAlimento(Alimento a) {
		// TODO Auto-generated method stub
		alimentoMap.put(a.getId(), a);
	}

	@Override
	public void deleteAlimento(String alimentoId) {
		alimentoMap.remove(alimentoId);
		
	}

	@Override
	public void addPlato(Plato p) {
		p.setId((index)+"pl");
		platoMap.put((index++)+"pl", p);
		
	}

	@Override
	public Collection<Plato> getAllPlatos() {
		// TODO Auto-generated method stub
		return this.platoMap.values();
	}

	@Override
	public Plato getPlato(String platoId) {
		// TODO Auto-generated method stub
		return this.platoMap.get(platoId);
	}

	@Override
	public void updatePlato(Plato p) {
		this.platoMap.put(p.getId(), p);
		
	}

	@Override
	public void deletePlato(String platoId) {
		// TODO Auto-generated method stub
		this.platoMap.remove(platoId);
		
	}

	@Override
	public void addDieta(Dieta d) {
		d.setId(index+"di");
		dietaMap.put((index++)+"di", d);
		
	}

	@Override
	public Collection<Dieta> getAllDietas() {
		// TODO Auto-generated method stub
		return dietaMap.values();
	}

	@Override
	public Dieta getDieta(String dietaId) {
		// TODO Auto-generated method stub
		return dietaMap.get(dietaId);
	}

	@Override
	public void updateDieta(Dieta d) {
		dietaMap.put(d.getId(), d);
		
	}

	@Override
	public void deleteDieta(String dietaId) {
		dietaMap.remove(dietaId);
		
	}
	
	//para añadir alimentos a platos y platos a dietas
	@Override
	public void addAlimento(String platoId, String alimentoId, String cantidad) {
		// TODO Auto-generated method stub
		Plato plato= getPlato(platoId);
		plato.addAlimento(alimentoMap.get(alimentoId), Double.parseDouble(cantidad));
	}

	@Override
	public void deleteAlimento(String platoId, String alimentoId) {
		// TODO Auto-generated method stub
		Plato plato= getPlato(platoId);
		plato.deleteAlimento(alimentoId);
	}

	@Override
	public void addPlato(String dietaId, String platoId) {
		// TODO Auto-generated method stub
		Dieta dieta= getDieta(dietaId);
		dieta.addPlato(platoMap.get(platoId));
	}

	@Override
	public void deletePlato(String dietaId, String platoId) {
		// TODO Auto-generated method stub
		Dieta dieta= getDieta(dietaId);
		dieta.deletePlato(platoId);
	}

}
