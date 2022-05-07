package aiss.api.resources.methods;

import java.util.LinkedList;
import java.util.List;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Dieta;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;

public class DietaMethodsPut {

	public static Dieta getDietaModified(Dieta nuevaDieta, DietaRepository repository) {
		if(nuevaDieta == null) throw new BadRequestException("No se ha enviado ninguna modificación");
		if(nuevaDieta.getId() == null)throw new BadRequestException("No existe ningún id en la consulta" );
		
		Dieta actualDieta = repository.getDieta(nuevaDieta.getId());
		if (actualDieta == null){
			throw new BadRequestException("El Id " + nuevaDieta.getId() + " no corresponde a ninguna dieta" );
		}
		if(nuevaDieta.getDescripcion() != null)
			actualDieta.setDescripcion(nuevaDieta.getDescripcion());
		
		return actualDieta;
	}

	public static void setPlatosToDieta(Dieta nuevaDieta, DietaRepository repository, Dieta actualDieta) {
		if(nuevaDieta.getPlatos() != null) {
			List<Plato> platos = new LinkedList<>();
			for (Plato plato : nuevaDieta.getPlatos()) {
				Plato platoTemp = repository.getPlato(plato.getId());
				if (platoTemp == null){
					throw new BadRequestException("Ha introducido un plato que no existe: " + plato.getId() );
				}
				platos.add(platoTemp);
			}
			if(platos.isEmpty()) {
				throw new BadRequestException("No ha introducido ningún plato");
			}
			actualDieta.setPlatos(platos);
			}
	}
}
