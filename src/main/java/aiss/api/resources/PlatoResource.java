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

import aiss.api.resources.methods.PlatoMethodsDelete;
import aiss.api.resources.methods.PlatoMethodsGet;
import aiss.api.resources.methods.PlatoMethodsPost;
import aiss.api.resources.methods.PlatoMethodsPut;
import aiss.api.resources.methods.ResponsePost;
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
	public Collection<Plato> getAll(@QueryParam("sortBy") String sort,
			@QueryParam("s") String caracteres,  
			@QueryParam("temporada") String temporada, 
			@QueryParam("ca") String ca, 
			@QueryParam("tipoDieta") String tipoDieta){
		
		
		Collection<Plato> platos = repository.getAllPlatos();
		if(caracteres != null) {
			platos= PlatoMethodsGet.getPlatoPorCaracter(platos, caracteres);
		}
		if(temporada != null) {
			platos= PlatoMethodsGet.getPlatosPorTemporada(temporada, platos);
		}
		if(ca != null) {
			platos= PlatoMethodsGet.getPlatosPorCA(ca, platos);
		}
		if(tipoDieta != null) {
			platos= PlatoMethodsGet.getPlatosPorTipoDieta(tipoDieta, platos, repository);
		}

		List<Comparator> ordenamiento= PlatoMethodsGet.getOrderPlato(sort);
		PlatoMethodsGet.setOrder(ordenamiento, platos);
		
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
	public Response updatePlato(Plato nuevoPlato) {
		
		Plato actualPlato = PlatoMethodsPut.getPlatoModified(nuevoPlato, repository);
		PlatoMethodsPut.setIngredientesOfPlato(nuevoPlato, actualPlato, repository);
		
		return Response.noContent().build();
	}
	
	@POST	
	@Consumes("application/json")
	@Produces("application/json")
	public Response addPlato(@Context UriInfo uriInfo, Plato plato) {	
		
		PlatoMethodsPost.checkPlatoIsValid(plato, repository);
		repository.addPlato(plato);
		
		return ResponsePost.getResponsePlato(uriInfo, plato, "getPlato", repository, this);
	}	
	
	@POST	
	@Path("/{platoId}/{alimentoId}")
	@Produces("application/json")
	public Response addPlato(@Context UriInfo uriInfo,
			@PathParam("platoId") String platoId,
			@PathParam("alimentoId") String alimentoId,
			@QueryParam("cantidad") String cantidad)
	{				
		
		PlatoMethodsPost.checkCantidadIsValid(cantidad);
		
		Alimento alimento = repository.getAlimento(alimentoId);
		Plato plato = repository.getPlato(platoId);
		
		if (alimento==null)
			throw new NotFoundException("La dieta con ID: " + alimentoId + " no existe");
		
		if (plato == null)
			throw new NotFoundException("El plato con ID: " + platoId + " no existe");
		
		if (plato.getAlimento(alimentoId)!=null)
			throw new BadRequestException("El alimento con ID: " + alimentoId + " ya está presente en el alimento con ID: " + alimentoId);
			
		repository.addAlimento(platoId, alimentoId, cantidad);		

		return ResponsePost.getResponsePlato(uriInfo, plato, "getPlato", repository, this);
	}
	
	@DELETE
	@Path("/{id}")
	public Response removePlato(@PathParam("id") String id) {
		Plato plato = repository.getPlato(id);
		if (plato == null) {
			throw new NotFoundException("El plato con ID: " + id + " no existe");
		}
		
		PlatoMethodsDelete.checkPlatoNoPerteneceADieta(plato, repository);
		
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
