package aiss.api.resources;

import java.util.Collection;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jboss.resteasy.spi.NotFoundException;

import aiss.model.Alimento;
import aiss.model.Dieta;
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
	public Collection<Dieta> getAll(){
		
		Collection<Dieta> dietas=repository.getAllDietas();
		
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
}
