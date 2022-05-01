package aiss.model.repository;

import java.util.Collection;
import java.util.Map;

import aiss.model.Alimento;
import aiss.model.Dieta;
import aiss.model.Plato;

public class MapDietaRepository implements DietaRepository{
	
	Map<String, Alimento> alimentoMap;
	Map<String, Plato> platoMap;
	Map<String, Dieta> dietaMap;
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
		
	}
	

	@Override
	public void addAlimento(Alimento a) {
		alimentoMap.put(a.getId(), a);
		
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
		// TODO Auto-generated method stub
		
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
		
	}

	@Override
	public void addDieta(Dieta d) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}
	
	//para añadir alimentos a platos y platos a dietas
	@Override
	public void addAlimento(String platoId, String alimentoId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAlimento(String platoId, String alimentoId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addPlato(String dietaId, String platoId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlato(String dietaId, String platoId) {
		// TODO Auto-generated method stub
		
	}

}
