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
import aiss.model.Alimento;
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
	public Collection<Plato> getAll(@QueryParam("sortBy") String sort){
		List<Comparator> options = new LinkedList<>();
		for(String param: Arrays.asList(sort.split(","))) {
			String parameter = param.substring(1);
			if(!parameter.equalsIgnoreCase("nombre") && !parameter.equalsIgnoreCase("calorias")) {
				throw new BadRequestException("Query \'" + parameter + "\', solo admite los valores \'nombre\' o \'calorias\'");
			}
			String ordering = "" + param.charAt(0);
			if(!ordering.equals("+") && !ordering.equals("-")) {
				throw new BadRequestException("Solo se admiten los simbolos \'+\' y \'-\'");
			}
			if(parameter.equalsIgnoreCase("nombre")) {
				if(ordering.equals("+")) {
					options.add(Comparator.comparing(Plato::getNombre));
				}else {
					options.add(Comparator.comparing(Plato::getNombre).reversed());
				}
			}else {
				if(ordering.equals("+")) {
					options.add(Comparator.comparing(Plato::getCalorias));
				}else {
					options.add(Comparator.comparing(Plato::getCalorias).reversed());
				}
			}
		}
		/*
		 * Para poder filtrarlo por cada uno, como hay 3 posibilidades (que se filtre por los 2 campos, por 1, o por ninguno)
		 */
		Collection<Plato> res = repository.getAllPlatos();
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
	
	@GET
	@Produces("application/json")
	public Collection <Plato> getPlatoPorCaracter(@QueryParam("s") String caracteres)
	{
		Collection<Plato> platos = repository.getAllPlatos();
	/*Devuelve los platos que empiezan(+), terminan(-) o contienen un caracter especificado*/
		if (caracteres.charAt(0)== '+' ) {
			platos = platos.stream().filter(x->x.getNombre().startsWith(caracteres.substring(1, caracteres.length()))).collect(Collectors.toList());
		}
		else if (caracteres.charAt(0)== '-' ) {
			platos = platos.stream().filter(x->x.getNombre().endsWith(caracteres.substring(1, caracteres.length()))).collect(Collectors.toList());
		}
		else {
			platos = platos.stream().filter(x->x.getNombre().contains(caracteres.substring(1, caracteres.length()))).collect(Collectors.toList());
		}
		return platos;
		
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
			if(nuevoPlato.getCalorias() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getListaAlergenos() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getNombre() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getCAOrigen() != null) 
				throw new BadRequestException("Solo puede modificarse el listado de alimentos");
			if(nuevoPlato.getTemporada() != null) 
				throw new BadRequestException("No podemos identificar el plato, no existe un ID");
		if(nuevoPlato.getAlimentos() != null) 
			actualPlato.setAlimentos(nuevoPlato.getAlimentos());
		return Response.noContent().build();
	}
}
