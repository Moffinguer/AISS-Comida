package aiss.api.resources.methods;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alimento;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;

public class AlimentoMethodsDelete {

	public static void checkAlimentoNoPerteneceAPlato(Alimento alimento, DietaRepository repository) {
		for (Plato plato : repository.getAllPlatos()) {
			if (plato.getAlimentos().stream().anyMatch(x -> x.getAlimento().equals(alimento))) {
				throw new BadRequestException("No puede eliminar el alimenta, ya que pertenece a algún plato");
			}
		}
	}
}
