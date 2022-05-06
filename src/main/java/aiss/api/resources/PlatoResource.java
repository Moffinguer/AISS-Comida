package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import aiss.model.Dieta;
import aiss.model.Ingrediente;
import aiss.model.Plato;
import aiss.model.Temporada;
import aiss.model.TipoDieta;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;


@Path("/platos")
public class PlatoResource {
	
	private static PlatoResource _instance=null;
	DietaRepository repository;
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
	public Collection<Plato> getAll(@QueryParam("sortBy") String sort, @QueryParam("s") String caracteres,  
			@QueryParam("temporada") String temporada, @QueryParam("ca") String ca, @QueryParam("tipoDieta") String tipoDieta){
		List<Comparator> options = new LinkedList<>();
		if(sort != null) {
			for(String param: Arrays.asList(sort.split(","))) {
				String parameter = param.substring(1);
				String ordering = param.substring(0,1);
				checkRestrictions(parameter, ordering);
				takeOptions(options, parameter, ordering);
			}
		}
		Collection<Plato> res = new LinkedList<>(repository.getAllPlatos());
		if(caracteres != null) {
			res= getPlatoPorCaracter(res, caracteres);
		}
		if(temporada != null) {
			res=getPlatosPorTemporada(temporada, res);
		}
		if(ca != null) {
			res=getPlatosPorCA(ca, res);
		}
		if(tipoDieta != null) {
			res=getPlatosPorTipoDieta(tipoDieta, res);
		}
		if(!options.isEmpty()) {
			if(options.size() == 1) {
				sorting(res, options.get(0));
			}else {
				sorting(res,options.get(0).thenComparing(options.get(1)));
			}
		}
		return res;
		
	}
	private Collection<Plato> getPlatoPorCaracter(Collection<Plato> platos, String caracteres){
		/*Devuelve los platos que empiezan(X), terminan(-) o contienen un caracter especificado*/
		
		if (caracteres.charAt(0)== 'X' ) {
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
	
	private Collection<Plato> getPlatosPorTemporada(String temporada, Collection<Plato> platos) {
		
		if(Arrays.asList(Temporada.values()).stream().map(v -> v.toString().toUpperCase()).
				anyMatch(v -> v.equals(temporada.toUpperCase()))) {
			
			platos = platos.stream().filter(p -> p.getTemporada().toString().toUpperCase().equals(temporada.toUpperCase())).collect(Collectors.toList());
			
		} else{
			throw new BadRequestException("Temporada no válida");
	    }
		
		return platos;
	}
	
	private Collection<Plato> getPlatosPorCA(String ca, Collection<Plato> platos) {
		
		platos = platos.stream().filter(p -> p.getCAOrigen().toUpperCase().equals(ca.toUpperCase())).collect(Collectors.toList());
	
	return platos;
	
    }
	
	private Collection<Plato> getPlatosPorTipoDieta(String tipoDieta, Collection<Plato> platos) {
		
		if(Arrays.asList(TipoDieta.values()).stream().map(v -> v.toString().toUpperCase()).
				anyMatch(v -> v.equals(tipoDieta.toUpperCase()))) {
			
		    platos = repository.getAllDietas().stream().filter(d -> d.getTipo().toString().toUpperCase().equals(tipoDieta.toUpperCase())).
				flatMap(d -> d.getPlatos().stream()).collect(Collectors.toSet());
			
		} else{
			throw new BadRequestException("Tipo de dieta no válida");
	    }
		
		return platos;
	}

	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Plato getPlato(@PathParam("id") String platoId)
	{
		Plato plato = repository.getPlato(platoId);
		if(plato == null) throw new NotFoundException("el alimento con ID: " + platoId + " no existe");
		return plato;
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
//		checkFieldsNotFilled(nuevoPlato);
		Plato actualPlato = repository.getPlato(nuevoPlato.getId());
		if(actualPlato == null)
			throw new BadRequestException("No podemos identificar el plato, no existe un ID como " + nuevoPlato.getId());
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
	private void sorting(Collection<Plato> platos, Comparator options) {
		Collections.sort((List<Plato>) platos, options);
		}
	private void takeOptions(List<Comparator> options, String parameter, String ordering) {
		/*
		 * Para poder filtrarlo por cada uno, como hay 3 posibilidades (que se filtre por los 2 campos, por 1, o por ninguno)
		 */
		if(parameter.equalsIgnoreCase("nombre")) {
			if(ordering.equals("X")) {
				options.add(Comparator.comparing(Plato::getNombre).reversed());
			}else {
				options.add(Comparator.comparing(Plato::getNombre));
			}
		}else {
			if(ordering.equals("X")) {
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
		if(!ordering.equals("X") && !ordering.equals("-")) {
			throw new BadRequestException("Solo se admiten los simbolos \'X\' y \'-\' pero se ha usado \'" + ordering + "\'");
		}
	}
	
	@POST	
	@Consumes("application/json")
	@Produces("application/json")
	public Response addPlato(@Context UriInfo uriInfo, Plato plato) {	
		if(plato.getNombre() == null || plato.getNombre().equals("")) throw new BadRequestException("El nombre del plato no puede ser nulo");
		if(plato.getAlimentos() == null || plato.getAlimentos().isEmpty()) throw new BadRequestException("La lista de alimentos no puede ser nula o estar vacía");
		if(plato.getCAOrigen() == null || plato.getCAOrigen().equals("")) throw new BadRequestException("La comunidad autónoma de origen del plato no puede ser nula");
		
		List<Ingrediente> alimentos = plato.getAlimentos();
		for(int i=0; i<alimentos.size(); i++) {
			Alimento alimentoPlato = plato.getAlimentos().get(i).getAlimento();
			Alimento alimentoRepo = repository.getAlimento(alimentoPlato.getId());
			if( alimentoRepo == null ||
			   !alimentoRepo.getNombre().equals(alimentoPlato.getNombre()) ||
			   !alimentoRepo.getCalorias().equals(alimentoPlato.getCalorias()) ||
			   !alimentoRepo.getCategoria().equals(alimentoPlato.getCategoria()) ||
			   !alimentoRepo.getTipo().equals(alimentoPlato.getTipo()) ||
			   !alimentoRepo.getAlergeno().equals(alimentoPlato.getAlergeno()) ||
			   !alimentoRepo.getTemporada().equals(alimentoPlato.getTemporada()))
			{
					throw new BadRequestException("El alimento con ID: " + alimentoPlato.getId() + " no existe");
				
			}
		}
		
		Collection<Temporada> temporadas = Arrays.asList(Temporada.values());
		if(plato.getTemporada()!=null && !temporadas.contains(plato.getTemporada())) {
			throw new BadRequestException("Temporada no válida");
		}
		
		repository.addPlato(plato);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getPlato");
		URI uri = ub.build(plato.getId());
		ResponseBuilder respb = Response.created(uri);
		respb.entity(plato);
		return respb.build();
	}	
	
	@POST	
	@Path("/{platoId}/{alimentoId}")
	@Produces("application/json")
	public Response addPlato(@Context UriInfo uriInfo,@PathParam("platoId") String platoId,
			@PathParam("alimentoId") String alimentoId,
			@QueryParam("cantidad") String cantidad)
	{				
		if(cantidad == null) {
			throw new BadRequestException("La cantidad no puede ser nula");
		}
		try {
			Double.parseDouble(cantidad);
		} catch(NumberFormatException e){
			throw new BadRequestException("La cantidad debe ser un número");
	    }
		
		
		Alimento alimento = repository.getAlimento(alimentoId);
		Plato plato = repository.getPlato(platoId);
		
		if (alimento==null)
			throw new NotFoundException("La dieta con ID: " + alimentoId + " no existe");
		
		if (plato == null)
			throw new NotFoundException("El plato con ID: " + platoId + " no existe");
		
		if (plato.getAlimento(alimentoId)!=null)
			throw new BadRequestException("El alimento con ID: " + alimentoId + " ya está presente en el alimento con ID: " + alimentoId);
			
		repository.addAlimento(platoId, alimentoId, cantidad);		

		// para la respuesta.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getPlato");
		URI uri = ub.build(platoId);
		ResponseBuilder resp = Response.created(uri);
		resp.entity(plato);			
		return resp.build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response removePlato(@PathParam("id") String id) {
		Plato plato = repository.getPlato(id);
		if (plato == null) {
			throw new NotFoundException("El plato con ID: " + id + " no existe");
		}else {
			for (Dieta dieta : repository.getAllDietas()) {
				if (dieta.getPlatos().stream().anyMatch(p -> p.equals(plato))) {
					throw new BadRequestException("No se puede eliminar el plato, ya que pertenece a alguna dieta");
				}
			}
		}
		repository.deletePlato(id);
		
		return Response.noContent().build();
	}
	
	@DELETE	
	@Path("/{platoId}/{alimentoId}")
	public Response removeAlimento(@PathParam("platoId") String platoId, @PathParam("alimentoId") String alimentoId) {
		
		Alimento alimento = repository.getAlimento(alimentoId);
		Plato plato = repository.getPlato(platoId);
		
		if (alimento==null)
			throw new NotFoundException("La dieta con ID: " + alimentoId + " no existe");
		
		if (plato == null)
			throw new NotFoundException("El plato con ID: " + platoId + " no existe");
		
		
		repository.deleteAlimento(platoId, alimentoId);		
		
		return Response.noContent().build();
	}
}
