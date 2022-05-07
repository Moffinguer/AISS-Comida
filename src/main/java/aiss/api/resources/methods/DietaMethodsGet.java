package aiss.api.resources.methods;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import aiss.model.Dieta;
import aiss.model.TipoAlimento;

public class DietaMethodsGet {

	public static Collection<Dieta> checkTipoIsValid(String tipo, Collection<Dieta> dietas) {
		if(Arrays.asList(TipoAlimento.values())
				.stream().map(v -> v.toString().toUpperCase())
				.anyMatch(v -> v.equals(tipo.toUpperCase()))) {
			throw new BadRequestException("Tipo de alimento no válido");
		}
		
		dietas = dietas.stream().filter(d -> d.getTipo().toString().equals(tipo.toUpperCase()))
				.collect(Collectors.toList());
		
		return dietas;
	}
	
	public static Dieta selectDietaFields(String id, String fields, Dieta dieta){
		Dieta resDieta = null;
		if(fields != null) {
		try {
			resDieta = new Dieta();
			for(String field :  fields.split(",")) {
				if(!field.equalsIgnoreCase("tipo") && !field.equalsIgnoreCase("platos")){
					throw new NotFoundException("Solo puede obtener los platos y los tipo");
				}
				if(field.equalsIgnoreCase("tipo")) {
					resDieta.setTipo(dieta.getTipo());
				}
				if(field.equalsIgnoreCase("platos")){
					resDieta.setPlatos(dieta.getPlatos());
				}
			}
			return resDieta;
		}catch(NullPointerException npe) {
			throw new NotFoundException("No existe una dieta con ID: " + id);
		}
		}
		return dieta;
	}
}
