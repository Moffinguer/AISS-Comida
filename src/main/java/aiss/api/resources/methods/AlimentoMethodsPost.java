package aiss.api.resources.methods;

import java.util.Arrays;
import java.util.Collection;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;

public class AlimentoMethodsPost {

	public static void checkAlimentoConsumed(Alimento alimento) {
		if(alimento.getNombre() == null || alimento.getNombre().equals("")) throw new BadRequestException("El nombre del alimento no puede ser nulo");
		if(alimento.getCalorias() == null) throw new BadRequestException("Las calorías del alimento no puede ser nulo");
		if(alimento.getTipo() == null ) throw new BadRequestException("El alimento debe ser al menos de un tipo");
		if(alimento.getAlergeno() == null) throw new BadRequestException("El alimento debe tener al menos un alergeno");
		if(alimento.getCategoria() == null) throw new BadRequestException("El alimento deber pertenecer al menos a una categoría");
	}
}
