package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import aiss.model.Alergeno;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;


@Path("/platos")
public class PlatoResource {
	
	private static PlatoResource _instance=null;
	DietaRepository repository;
	
	private PlatoResource() {
		repository=MapDietaRepository.getInstance();

	}
	
	public static PlatoResource getInstance()
	{
		if(_instance==null)
				_instance=new PlatoResource();
		return _instance;
	}
	
	@SuppressWarnings("finally")
	@GET
	@Produces("application/json")
	public Collection<Plato> getAll(@QueryParam("nombre") String orderName,
			@QueryParam("calorias") String orderCalories){
		orderName = orderName.toUpperCase();
		orderCalories = orderCalories.toUpperCase();
		List<Plato> res = (List<Plato>) repository.getAllPlatos();
		List<Comparator> options = new LinkedList<>();
		/*
		 * Para facilitar una busqueda por dos campos simultaneos, se guardará por cada una de las QUERY
		 * un comparador, teniendo el campo `nombre` preferencia. 
		 */
		if(orderName != null) {
			if(orderName.equals("ASC")) {
				options.add(Comparator.comparing(Plato::getNombre));
			}else if(orderName.equals("DESC")){
				options.add(Comparator.comparing(Plato::getNombre).reversed());
			}else{
				throw new BadRequestException("Query \'nombre\', solo admite los valores \'ASC\' o \'DESC\'");
			}	
		}
		if(orderCalories != null) {
			if(orderCalories.equals("ASC")) {
				options.add(Comparator.comparing(Plato::getCalorias));
			}else if(orderCalories.equals("DESC")){
				options.add(Comparator.comparing(Plato::getCalorias).reversed());
			}else{
				throw new BadRequestException("Query \'calorias\', solo admite los valores \'ASC\' o \'DESC\'");
			}
		}
		/*
		 * Para poder filtrarlo por cada uno, como hay 3 posibilidades (que se filtre por los 2 campos, por 1, o por ninguno)
		 */
		try {
			res = (List<Plato>) res.stream().sorted(options.get(0).thenComparing(options.get(1))).collect(Collectors.toList());
		}catch(IndexOutOfBoundsException iobe){
			try {
				res = (List<Plato>) res.stream().sorted(options.get(0)).collect(Collectors.toList());
			}catch(IndexOutOfBoundsException iobe2) {}
		}finally {
			return res;	
		}
	}
	@GET
	@Consumes("application/json")
	public Collection<Alergeno> getListOfAler(){
		return Arrays.asList(Alergeno.values());
	}
	@PUT
	@Consumes("application/json")
	public Response updateDish(Plato nuevoPlato) {
		if(nuevoPlato == null) {
			throw new BadRequestException("No se ha enviado ninguna modificación");
		}
		if(nuevoPlato.getId() == null || nuevoPlato.getId().isEmpty()) {
			throw new BadRequestException("No podemos identificar el plato, no existe un ID");
		}
		Plato actualPlato = repository.getPlato(nuevoPlato.getId());
		/*
		 * Compruebo que ningún campo esté siendo modificado, salvo el de los alimentos
		 * En caso de que hubiera alguno no definido, en la petición, lanzaría un error
		 */
		try {
			if(!nuevoPlato.getCalorias().equals(actualPlato.getCalorias())) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(!nuevoPlato.getListaAlergenos().equals(actualPlato.getListaAlergenos())) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(!nuevoPlato.getNombre().equals(actualPlato.getNombre())) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(!nuevoPlato.getCAOrigen().equals(actualPlato.getCAOrigen())) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(!nuevoPlato.getTemporada().equals(actualPlato.getTemporada())) 
				throw new BadRequestException("No podemos identificar el plato, no existe un ID");
		}catch(NullPointerException npe) {
			throw new BadRequestException("Solo puede modificarse el listado de alimentos");
		}
		if(nuevoPlato.getAlimentos() != null) 
			actualPlato.setAlimentos(nuevoPlato.getAlimentos());
		return Response.noContent().build();
	}
}
