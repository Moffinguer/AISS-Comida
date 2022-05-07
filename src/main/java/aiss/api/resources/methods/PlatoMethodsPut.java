package aiss.api.resources.methods;

import java.util.LinkedList;
import java.util.List;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alimento;
import aiss.model.Ingrediente;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;

public class PlatoMethodsPut {

	public static Plato getPlatoModified(Plato nuevoPlato, DietaRepository repository) {
		Plato actualPlato = null;
		try {
			actualPlato = repository.getPlato(nuevoPlato.getId());
			if (actualPlato == null) {
				throw new BadRequestException(
						"No podemos identificar el plato, no existe un plato con ID " + nuevoPlato.getId());
			}
		} catch (Exception e) {
			if (nuevoPlato == null) {
				throw new BadRequestException("No se ha enviado ninguna modificación");
			}
			if (nuevoPlato.getId() == null || nuevoPlato.getId().isEmpty()) {
				throw new BadRequestException("No podemos identificar el plato, no existe un ID");
			}
		}

		return actualPlato;
	}

	public static void setIngredientesOfPlato(Plato nuevoPlato, Plato actualPlato, DietaRepository repository) {
		try {
			List<Ingrediente> ingredientes = new LinkedList<>();
			AddIngredients(nuevoPlato, ingredientes, repository);
			actualPlato.setAlimentos(ingredientes);
			repository.updatePlato(actualPlato);
		} catch (NullPointerException npe) {
			throw new BadRequestException("No ha introducido ningún ingrediente");
		}
	}

	public static void AddIngredients(Plato nuevoPlato, List<Ingrediente> ingredientes, DietaRepository repository)
			throws NullPointerException {
		for (Ingrediente ingrediente : nuevoPlato.getAlimentos()) {
			String ingredienteId = ingrediente.getAlimento().getId();
			if (ingredienteId == null) {
				throw new BadRequestException("No ha introducido IDs para los ingredientes");
			}
			Alimento alimento = repository.getAlimento(ingrediente.getAlimento().getId());
			Double cantidad = ingrediente.getCantidad();
			if (alimento == null) {
				throw new BadRequestException("No existe un alimento con ID: " + ingredienteId);
			}
			if (cantidad == null) {
				throw new BadRequestException("No ha indicado cuantas calorias tiene el ingrediente " + ingredienteId);
			}
			ingredientes.add(new Ingrediente(alimento, cantidad));
		}
	}
}
