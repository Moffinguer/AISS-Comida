package aiss.api.resources.methods;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Dieta;
import aiss.model.Plato;
import aiss.model.TipoDieta;
import aiss.model.repository.DietaRepository;

public class DietaMethodsPost {

	public static void checkDietaIsValid(Dieta dieta, DietaRepository repository) {
		if (dieta.getNombre() == null || "".equals(dieta.getNombre()))
			throw new BadRequestException("El nombre de la dieta no debe ser nulo");
		
		if (dieta.getPlatos()==null)
			throw new BadRequestException("Una dieta debe contener platos");
		
		if(dieta.getTipo()!=null) {
			Collection<TipoDieta> tiposDieta=Arrays.asList(TipoDieta.values());
			if(!tiposDieta.contains(dieta.getTipo()))
				throw new BadRequestException("Tipo de dieta no válido");
		}		
		
		List<Plato> platos = checkPlatosAreValid(dieta, repository);
		dieta.setPlatos(platos);
	}

	private static List<Plato> checkPlatosAreValid(Dieta dieta, DietaRepository repository) {
		List<Plato> platos = dieta.getPlatos();
		for(int i = 0; i < platos.size(); i++) {
			Plato platoBody = dieta.getPlatos().get(i);
			Plato platoRepo = repository.getPlato(platoBody.getId());
			if(platoRepo == null) {
				throw new BadRequestException("El plato con ID: " + platoBody.getId() + " no existe");
			} else {
				platos.set(i, platoRepo);
			}
		}
		return platos;
	}
	
	
}
