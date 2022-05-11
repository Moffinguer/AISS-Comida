package aiss.api.resources.methods;

import java.util.List;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alimento;
import aiss.model.Ingrediente;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;

public class PlatoMethodsPost {

	public static void checkPlatoIsValid(Plato plato, DietaRepository repository) {
		if (plato.getNombre() == null || plato.getNombre().isEmpty())
			throw new BadRequestException("El nombre del plato no puede ser nulo");
		if (plato.getAlimentos() == null || plato.getAlimentos().isEmpty())
			throw new BadRequestException("La lista de alimentos no puede ser nula o estar vacía");
		if (plato.getCAOrigen() == null || plato.getCAOrigen().isEmpty())
			throw new BadRequestException("La comunidad autónoma de origen del plato no puede ser nula");

		checkIngredientesAreValid(plato, repository);
	}

	private static void checkIngredientesAreValid(Plato plato, DietaRepository repository) {
		List<Ingrediente> alimentos = plato.getAlimentos();
		for (int i = 0; i < alimentos.size(); i++) {
			Alimento alimentoPlato = alimentos.get(i).getAlimento();
			Alimento alimentoRepo = repository.getAlimento(alimentoPlato.getId());
			if (checkAlimento(alimentoPlato, alimentoRepo)) {
				throw new BadRequestException("El alimento con ID: " + alimentoPlato.getId() + " no existe");

			}
		}
	}

	private static boolean checkAlimento(Alimento alimentoPlato, Alimento alimentoRepo) {
		return alimentoRepo == null;
	}

	public static void checkCantidadIsValid(String cantidad) {
		if (cantidad == null) {
			throw new BadRequestException("La cantidad no puede ser nula");
		}
		try {
			Double.parseDouble(cantidad);
		} catch (NumberFormatException e) {
			throw new BadRequestException("La cantidad debe ser un número");
		}
	}

}
