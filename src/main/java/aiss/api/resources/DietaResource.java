package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
	@GET
	@Consumes("application/json")
	@Path("/{id}")
	public Collection<Plato> getPlatos(@QueryParam("fields") String fields, @PathParam("id") String id){
		Collection<Plato> platos = null;
		try {
			platos = repository.getDieta(id).getPlatos();
			for(String field :  fields.split(",")) {
				if(!field.equalsIgnoreCase("tipo") && !field.equalsIgnoreCase("platos")){
					throw new NotFoundException("Solo puede obtener los platos y los tipo");
				}
				if(field.equalsIgnoreCase("tipo")) {
					
				}
			}
		}catch(NullPointerException npe) {
			throw new NotFoundException("No existe una dieta con ID: " + id);
		}finally {
			return platos;
		}
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
