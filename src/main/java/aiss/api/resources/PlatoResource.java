package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
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
import aiss.model.Alimento;
import aiss.model.Ingrediente;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;


@Path("/platos")
public class PlatoResource {
	
	private static PlatoResource _instance=null;
	DietaRepository repository;
	private final String upperOption = "X";
	private final String lowerOption = "-";
	public PlatoResource() {
		repository=MapDietaRepository.getInstance();

	}
	
	public static PlatoResource getInstance()
	{
		if(_instance==null)
				_instance=new PlatoResource();
		return _instance;
	}
	@GET
	@Produces("application/json")
	public Collection<Plato> getAll(@QueryParam("sortBy") String sort, @QueryParam("s") String caracteres){
		List<Comparator> options = new LinkedList<>();
		if(sort != null && !sort.isEmpty()) {
			for(String param: Arrays.asList(sort.split(","))) {
				String parameter = param.substring(1);
				String ordering = param.substring(0,1);
				checkRestrictions(parameter, ordering);
				takeOptions(options, parameter, ordering);
			}
		}
		Collection<Plato> res = repository.getAllPlatos();
		getPlatoPorCaracter(res, caracteres);
		if(!options.isEmpty()) {
			if(options.size() == 1) {
				sorting(res, options.get(0));
			}else {
				sorting(res,options.get(0).thenComparing(options.get(1)));
			}
		}
		return res;
		
	}
	private void getPlatoPorCaracter(Collection<Plato> platos, String caracteres){
		/*Devuelve los platos que empiezan(X), terminan(-) o contienen un caracter especificado*/
		
		if(caracteres != null && !caracteres.isEmpty()) {
			Predicate<Plato> condition;
			if (caracteres.startsWith(upperOption)) {
				condition = x->x.getNombre().startsWith(caracteres.substring(1, caracteres.length()));
			}
			else if (caracteres.startsWith(lowerOption)) {
				condition = x->x.getNombre().endsWith(caracteres.substring(1, caracteres.length()));
			}
			else {
				condition = x->x.getNombre().contains(caracteres.substring(1, caracteres.length()));
			}
			platos = platos.stream().filter(x -> condition.test(x)).collect(Collectors.toList());
		}
	}
	private void sorting(Collection<Plato> platos, Comparator options) {
		platos = (Collection<Plato>) platos.stream().sorted(options).collect(Collectors.toList());
	}
	private void takeOptions(List<Comparator> options, String parameter, String ordering) {
		/*
		 * Para poder filtrarlo por cada uno, como hay 3 posibilidades (que se filtre por los 2 campos, por 1, o por ninguno)
		 */
		if(parameter.equalsIgnoreCase("nombre")) {
			if(ordering.equals(upperOption)) {
				options.add(Comparator.comparing(Plato::getNombre).reversed());
			}else {
				options.add(Comparator.comparing(Plato::getNombre));
			}
		}else {
			if(ordering.equals(upperOption)) {
				options.add(Comparator.comparing(Plato::getCalorias).reversed());
			}else {
				options.add(Comparator.comparing(Plato::getCalorias));
			}
		}
	}
	private void checkRestrictions(String parameter, String ordering) {
		if(!parameter.equalsIgnoreCase("nombre") && !parameter.equalsIgnoreCase("calorias")) {
			throw new BadRequestException("Query \'" + parameter + "\', solo admite los valores \'nombre\' o \'calorias\'");
		}
		if(!ordering.equals(upperOption) && !ordering.equals(lowerOption)) {
			throw new BadRequestException("Solo se admiten los simbolos \'X\' y \'-\' pero se ha usado \'" + ordering + "\'");
		}
	}
	
	@PUT
	@Path("/{id}")
	@Consumes("application/json")
	public Response updateDish(@PathParam("id") String id, Plato nuevoPlato) {
		if(nuevoPlato == null) {
			throw new BadRequestException("No se ha enviado ninguna modificación");
		}
		if(id == null || id.isEmpty()) {
			throw new BadRequestException("No podemos identificar el plato, no existe un ID");
		}
		Plato actualPlato = repository.getPlato(id);
		if(actualPlato == null)
			throw new BadRequestException("No podemos identificar el plato, no existe un ID como " + nuevoPlato.getId());
		checkFieldsNotFilled(nuevoPlato);
		if(nuevoPlato.getAlimentos() != null) {
			List<Ingrediente> ingredientes = new LinkedList<>();
			try {
				searchAddIngredients(nuevoPlato, ingredientes);
			}catch(NullPointerException npe) {
				throw new BadRequestException("No ha introducido ningún ingrediente");
			}
			actualPlato.setAlimentos(ingredientes);
		}
		repository.updatePlato(actualPlato);
		return Response.noContent().build();
	}

	private void searchAddIngredients(Plato nuevoPlato, List<Ingrediente> ingredientes) {
		for(Ingrediente ingrediente : nuevoPlato.getAlimentos()) {
			String ingredienteId = ingrediente.getAlimento().getId();
			if(ingredienteId == null) {
				throw new BadRequestException("No ha introducido IDs para los ingredientes");
			}
			Alimento alimento = repository.getAlimento(ingrediente.getAlimento().getId());
			Double cantidad = ingrediente.getCantidad();
			if(alimento == null) {
				throw new BadRequestException("No existe un alimento con ID: " + ingredienteId);
			}
			if(cantidad == null) {
				throw new BadRequestException("No ha indicado cuantas calorias tiene el ingrediente " + ingredienteId);
			}
			ingredientes.add(new Ingrediente(alimento, cantidad));
		}
	}

	private void checkFieldsNotFilled(Plato nuevoPlato) {
		/*
		 * Compruebo que ningún campo esté siendo modificado, salvo el de los alimentos
		 * En caso de que hubiera alguno no definido, en la petición, lanzaría un error
		 */
			if(nuevoPlato.getCalorias() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getListaAlergenos() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getNombre() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getCAOrigen() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getTemporada() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getId() != null)
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
	}
}
