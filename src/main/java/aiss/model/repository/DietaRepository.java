package aiss.model.repository;

import java.util.Collection;

import aiss.model.Alimento;
import aiss.model.Dieta;
import aiss.model.Plato;

public interface DietaRepository {

		public void addAlimento(Alimento a);
		public Collection<Alimento> getAllAlimentos();
		public Alimento getAlimento(String alimentoId);
		public void updateAlimento(Alimento a);
		public void deleteAlimento(String alimentoId);
		
		public void addPlato(Plato p);
		public Collection<Plato> getAllPlatos();
		public Plato getPlato(String platoId);
		public void updatePlato(Plato p);
		public void deletePlato(String platoId);
		public void addAlimento(String platoId, String alimentoId, String cantidad);
		public void deleteAlimento(String platoId, String alimentoId);
		
		public void addDieta(Dieta d);
		public Collection<Dieta> getAllDietas();
		public Dieta getDieta(String dietaId);
		public void updateDieta(Dieta d);
		public void deleteDieta(String dietaId);
		public void addPlato(String dietaId, String platoId);
		public void deletePlato(String dietaId, String platoId);
}
