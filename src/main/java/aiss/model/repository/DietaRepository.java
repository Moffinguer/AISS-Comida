package aiss.model.repository;

import java.util.Collection;

import aiss.model.Alimento;
import aiss.model.Dieta;
import aiss.model.Plato;

public interface DietaRepository {

		// Alimentos
		public void addAlimento(Alimento a);
		public Collection<Alimento> getAllAlimentos();
		public Alimento getAlimento(String alimentoId);
		public void updateAlimento(Alimento a);
		public void deleteAlimento(String alimentoId);
		
		// Platos
		public void addPlato(Plato p);
		public Collection<Plato> getAllPlatos();
		public Plato getPlato(String platoId);
		public void updatePlato(Plato p);
		public void deletePlato(String platoId);
		
		//Dietas
		public void addDieta(Dieta d);
		public Collection<Dieta> getAllDietas();
		public Dieta getDieta(String dietaId);
		public void updateDieta(Dieta d);
		public void deleteDieta(String dietaId);
}
