package aiss.api.resources;

import java.util.Collection;
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
import javax.ws.rs.core.UriInfo;
import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import aiss.api.resources.methods.DietaMethodsGet;
import aiss.api.resources.methods.DietaMethodsPost;
import aiss.api.resources.methods.DietaMethodsPut;
import aiss.api.resources.methods.ResponsePost;
import aiss.model.Dieta;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;

@Path("/dietas")
public class DietaResource {

	private static DietaResource _instance=null;
	DietaRepository repository;
	
	public DietaResource() {
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
			dietas = DietaMethodsGet.checkTipoIsValid(tipo, dietas);
		}
		
		return dietas;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Dieta getDieta(@PathParam("id") String dietaId, @QueryParam("fields") String fields)
	{
		Dieta dieta = repository.getDieta(dietaId);
		if(dieta == null) throw new NotFoundException("la dieta con ID: " + dietaId + " no existe");
		Dieta res = DietaMethodsGet.selectDietaFields(dietaId, fields, dieta);
		return res;
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addDieta(@Context UriInfo uriInfo, Dieta dieta) {
		
		DietaMethodsPost.checkDietaIsValid(dieta, repository);
		
		repository.addDieta(dieta);
		
		return ResponsePost.getResponseDieta(uriInfo, dieta, "getDieta", repository, this);
	}
	
	@PUT 
	@Consumes("application/json")
	public Response updateDieta(Dieta nuevaDieta) {
		
		Dieta actualDieta= DietaMethodsPut.getDietaModified(nuevaDieta, repository);
		DietaMethodsPut.setPlatosToDieta(nuevaDieta, repository, actualDieta);
		
		repository.updateDieta(actualDieta);
		
		return Response.noContent().build();
	}
	
	@POST	
	@Path("/{dietaId}/{platoId}")
	@Produces("application/json")
	public Response addPlato(@Context UriInfo uriInfo,@PathParam("dietaId") String dietaId, @PathParam("platoId") String platoId)
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

		return ResponsePost.getResponseDieta(uriInfo, dieta, "getDieta", repository, this);
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
	
	@DELETE
	@Path("/{dietaId}/{platoId}")
	public Response removePlato(@PathParam("dietaId") String dietaId, @PathParam("platoId") String platoId) {
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