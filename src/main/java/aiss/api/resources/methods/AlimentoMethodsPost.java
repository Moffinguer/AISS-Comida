package aiss.api.resources.methods;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alimento;

public class AlimentoMethodsPost {

	public static void checkAlimentoConsumed(Alimento alimento) {
		if (alimento.getNombre() == null || alimento.getNombre().isEmpty())
			throw new BadRequestException("El nombre del alimento no puede ser nulo");
		if (alimento.getCalorias() == null)
			throw new BadRequestException("Las calorías del alimento no puede ser nulo");
		if (alimento.getTipo() == null)
			throw new BadRequestException("El alimento debe ser al menos de un tipo");
		if (alimento.getAlergeno() == null)
			throw new BadRequestException("El alimento debe tener al menos un alergeno");
		if (alimento.getCategoria() == null)
			throw new BadRequestException("El alimento deber pertenecer al menos a una categoría");
	}
}
