package aiss.api;
	import java.util.HashSet;
	import java.util.Set;

	import javax.ws.rs.core.Application;

import aiss.api.resources.AlimentoResource;
import aiss.api.resources.DietaResource;
import aiss.api.resources.PlatoResource;
import aiss.model.IngredienteExterno;
	
	public class FoodApplication  extends Application {
		private Set<Object> singletons = new HashSet<Object>();
		private Set<Class<?>> classes = new HashSet<Class<?>>();

		// Loads all resources that are implemented in the application
		// so that they can be found by RESTEasy.
		public FoodApplication() {
			
			singletons.add(AlimentoResource.getInstance());
			singletons.add(DietaResource.getInstance());
			singletons.add(PlatoResource.getInstance());
			singletons.add(IngredienteResource.getInstance());
		}

		@Override
		public Set<Class<?>> getClasses() {
			return classes;
		}

		@Override
		public Set<Object> getSingletons() {
			return singletons;
		}
	}
