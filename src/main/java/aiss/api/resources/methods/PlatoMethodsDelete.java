package aiss.api.resources.methods;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alimento;
import aiss.model.Dieta;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;

public class PlatoMethodsDelete {

	public static void checkPlatoNoPerteneceADieta(Plato plato, DietaRepository repository) {
		for (Dieta dieta : repository.getAllDietas()) {
			if (dieta.getPlatos().stream().anyMatch(p -> p.equals(plato))) {
				throw new BadRequestException("No se puede eliminar el plato, ya que pertenece a alguna dieta");
			}
		}
	}
}
