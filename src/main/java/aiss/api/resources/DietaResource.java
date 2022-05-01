package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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

import aiss.model.Alimento;
import aiss.model.Dieta;
import aiss.model.Plato;
import aiss.model.TipoAlimento;
import aiss.model.TipoDieta;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;

@Path("/dietas")
public class DietaResource {

	private static DietaResource _instance=null;
	DietaRepository repository;
	
	private DietaResource() {
		repository=MapDietaRepository.getInstance();

	}
	
	public static DietaResource getInstance()
	{
		if(_instance==null)
				_instance=new DietaResource();
		return _instance;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Dieta> getAll(@QueryParam("tipo") String tipo){
		
		Collection<Dieta> dietas=repository.getAllDietas();
		
		if(tipo != null) {
			if(Arrays.asList(TipoAlimento.values())
					.stream().map(v -> v.toString().toUpperCase())
					.anyMatch(v -> v.equals(tipo.toUpperCase()))) {
				throw new BadRequestException("Tipo de alimento no válido");
			}
			
			dietas = dietas.stream().filter(d -> d.getTipo().toString().equals(tipo))
					.collect(Collectors.toList());
		}
		
		return dietas;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Dieta get(@PathParam("id") String dietaId)
	{
		Dieta dieta = repository.getDieta(dietaId);
		if(dieta == null) throw new NotFoundException("la dieta con ID: " + dietaId + " no existe");
		return dieta;
	}
	
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addDieta(@Context UriInfo uriInfo, Dieta dieta) {
		if (dieta.getNombre() == null || "".equals(dieta.getNombre()))
			throw new BadRequestException("El nombre de la dieta no debe ser nulo");
		
		if (dieta.getPlatos()==null)
			throw new BadRequestException("Una dieta debe contener platos");
		
		Collection<TipoDieta> tiposDieta=Arrays.asList(TipoDieta.values());
		if(tiposDieta.contains(dieta.getTipo()))
			throw new BadRequestException("Tipo de dieta no válido");
		
		repository.addDieta(dieta);

		// para la respuesta.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(dieta.getId());
		ResponseBuilder resp = Response.created(uri);
		resp.entity(dieta);
		return resp.build();
	}
	
	@PUT 
	@Consumes("aplication/json")
	public Response updateDiet(Dieta nuevaDieta) {
		if(nuevaDieta == null) {
			throw new BadRequestException("No se ha enviado ninguna modificación");
		}
		if(nuevaDieta.getId() == null || nuevaDieta.getId().isEmpty()) {
			throw new BadRequestException("No podemos identificar el plato, no existe un ID");
		}
		Dieta actualDieta = repository.getDieta(nuevaDieta.getId());
		if (actualDieta == null){
			throw new BadRequestException("El Id " + nuevaDieta.getId() + " no corresponde a ninguna dieta" );
		}
		
		if(nuevaDieta.getNombre() != null) 
			throw new BadRequestException("Solo puede modificarse el listado de platos o la descripción de la dieta");
		if(nuevaDieta.getTipo() != null) 
			throw new BadRequestException("Solo puede modificarse el listado de platos o la descripción de la dieta");
		/*Comprobación de que la Id de los platos introducidos existen: */
		if(nuevaDieta.getPlatos() != null) {
			List<Plato> platos = new LinkedList<>();
			for (Plato plato : nuevaDieta.getPlatos()) {
				Plato platoTemp = repository.getPlato(plato.getId());
				if (platoTemp == null){
					throw new BadRequestException("Ha introducido un plato que no existe: " + plato.getId() );
				}
				platos.add(platoTemp);
			}
			actualDieta.setPlatos(platos);
			}
		if(nuevaDieta.getDescripcion() !=null)
			actualDieta.setDescripcion(nuevaDieta.getDescripcion());
		repository.updateDieta(actualDieta);
		return Response.noContent().build();
	}
	
	
	
	
	
	@DELETE
	@Path("/{id}")
	public Response removeDieta(@PathParam("id") String id) {
		Dieta dieta=repository.getDieta(id);
		if (dieta == null)
			throw new NotFoundException("La dieta con ID: " + id + " no existe");
		else
			repository.deleteDieta(id);
		
		return Response.noContent().build();
	}
	
	@POST	
	@Path("/{dietaId}/{platoId}")
	@Consumes("text/plain")
	@Produces("application/json")
	public Response addSong(@Context UriInfo uriInfo,@PathParam("dietaId") String dietaId, @PathParam("platoId") String platoId)
	{				
		
		Dieta dieta = repository.getDieta(dietaId);
		Plato plato = repository.getPlato(platoId);
		
		if (dieta==null)
			throw new NotFoundException("La dieta con ID: " + dietaId + " no existe");
		
		if (plato == null)
			throw new NotFoundException("El plato con ID: " + platoId + " no existe");
		
		if (dieta.getPlato(platoId)!=null)
			throw new BadRequestException("El plato con ID: " + platoId + " ya está presente en la dieta con ID: " + dietaId);
			
		repository.addPlato(dietaId, platoId);		

		// para la respuesta.
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(dietaId);
		ResponseBuilder resp = Response.created(uri);
		resp.entity(dieta);			
		return resp.build();
	}
	
	
	@DELETE
	@Path("/{dietaId}/{platoId}")
	public Response removeSong(@PathParam("dietaId") String dietaId, @PathParam("platoId") String platoId) {
		Dieta dieta = repository.getDieta(dietaId);
		Plato plato = repository.getPlato(platoId);
		
		if (dieta==null)
			throw new NotFoundException("La dieta con ID: " + dietaId + " no existe");
		
		if (plato == null)
			throw new NotFoundException("El plato con ID: " + platoId + " no existe");
		
		
		repository.deletePlato(dietaId, platoId);		
		
		return Response.noContent().build();
	}
	
}
