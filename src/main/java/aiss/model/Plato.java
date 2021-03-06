package aiss.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Plato {
		
		private String id;
		private String nombre;
		private List<Ingrediente> alimentos;
		private Set<Alergeno> listaAlergenos; 
		private Double calorias; 
		private String provinciaOrigen; 
		private String CAOrigen; 
		private Temporada temporada; 
		
		public Plato () {}
		
		public Plato (String nombre) {
			this.nombre = nombre;
		}
		public Plato(List<Ingrediente> alimentos) {
			this.alimentos = alimentos;
		}
		public Plato (String nombre, List<Ingrediente> alimentos, String provinciaOrigen,
				String CAOrigen, Temporada temporada) {
			this.nombre = nombre;
			this.alimentos = alimentos;
			this.listaAlergenos = this.alimentos.stream().map(alimento -> alimento.getAlimento().getAlergeno()).collect(Collectors.toSet());
			this.calorias = this.alimentos.stream().mapToDouble(alimento -> alimento.getAlimento().getCalorias()).sum();
			this.provinciaOrigen = provinciaOrigen;
			this.CAOrigen = CAOrigen;
			this.temporada = temporada;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		public List<Ingrediente> getAlimentos() {
			return alimentos;
		}

		public void setAlimentos(List<Ingrediente> alimentos) {
			this.alimentos = alimentos;
			if(!this.alimentos.stream().anyMatch(x -> x.getAlimento().getCalorias() == null || x.getAlimento().getAlergeno() == null)) {
				setListaAlergenos();
				setCalorias();
			}
		}

		public Set<Alergeno> getListaAlergenos() {
			return listaAlergenos;
		}

		public void setListaAlergenos() {
			this.listaAlergenos = this.alimentos.stream().map(alimento -> alimento.getAlimento().getAlergeno()).collect(Collectors.toSet());
			
		}

		public Double getCalorias() {
			return calorias;
		}

		public void setCalorias() {
			this.calorias = this.alimentos.stream().mapToDouble(alimento -> alimento.getAlimento().getCalorias()).sum();
		}

		public String getProvinciaOrigen() {
			return provinciaOrigen;
		}

		public void setProvinciaOrigen(String provinciaOrigen) {
			this.provinciaOrigen = provinciaOrigen;
		}

		public String getCAOrigen() {
			return CAOrigen;
		}

		public void setCAOrigen(String cAOrigen) {
			CAOrigen = cAOrigen;
		}

		public Temporada getTemporada() {
			return temporada;
		}

		public void setTemporada(Temporada temporada) {
			this.temporada = temporada;
		}	
		
		public void addAlimento (Alimento alimento, Double gramos) {
			if (this.alimentos == null) {
				this.alimentos = new LinkedList<Ingrediente>();
			}
			alimentos.add(new Ingrediente(alimento, gramos));
		}
		public void addAlimentos(List<Ingrediente> ingredientes) {
			if(this.alimentos == null) this.alimentos = new LinkedList<Ingrediente>();
			alimentos.addAll(ingredientes);
		}
		public void deleteAlimento (String id) {
			String sameId = id.trim();
			for(int i = this.alimentos.size() - 1; i > -1 ; i--){
				if(alimentos.get(i).getAlimento().getId().equals(sameId)) {
					this.alimentos.remove(i);
					break;
				}
			}
		}
		
		public void deleteAlimento (Ingrediente alimento) {
			this.alimentos.remove(alimento);
		}
		
		public Alimento getAlimento(String id) {
			Alimento alimento = null;
			String sameId = id.trim();
			for(int i = this.alimentos.size() - 1; i > -1 ; i--){
				if(alimentos.get(i).getAlimento().getId().equals(sameId)) {
					return this.getAlimentos().get(i).getAlimento();
				}
			}
			return alimento;
		}
		
		public Double getCantidadAlimento(String id) {
			Double cantidad = 0.;
			String sameId = id.trim();
			for(int i = this.alimentos.size() - 1; i > -1 ; i--){
				if(alimentos.get(i).getAlimento().getId().equals(sameId)) {
					return this.alimentos.get(i).getCantidad();
				}
			}
			return cantidad;
		}
		
}
