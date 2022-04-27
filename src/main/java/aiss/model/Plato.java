package aiss.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
public class Plato {
		
		//ATRIBUTOS
		private String id;
		private String nombre;
		private Map<Alimento, Double> alimentos;
		private Set<Alergeno> listaAlergenos; //derivada
		private Double calorias; //derivada
		private String provinciaOrigen; //opcional
		private String CAOrigen; 
		private Temporada temporada; //opcional
		
		//CONSTRUCTORES
		public Plato () {}
		
		public Plato (String nombre) {
			this.nombre = nombre;
		}
		
		public Plato (String id, String nombre) {
			this.id = id;
			this.nombre = nombre;
		}
		
		public Plato (String id, String nombre, Map<Alimento, Double> alimentos, String provinciaOrigen,
				String CAOrigen, Temporada temporada) {
			this.id = id;
			this.nombre = nombre;
			this.alimentos = alimentos;
			this.listaAlergenos = this.alimentos.entrySet().stream().map(a->a.getKey().getAlergeno()).collect(Collectors.toSet());
			this.calorias = this.alimentos.entrySet().stream().mapToDouble(a->a.getKey().getCalorias()*a.getValue()).sum();
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

		public Map<Alimento, Double> getAlimentos() {
			return alimentos;
		}

		public void setAlimentos(Map<Alimento, Double> alimentos) {
			this.alimentos = alimentos;
		}

		public Set<Alergeno> getListaAlergenos() {
			return listaAlergenos;
		}

		public void setListaAlergenos() {
			this.listaAlergenos = this.alimentos.entrySet().stream().map(a->a.getKey().getAlergeno()).collect(Collectors.toSet());
		}

		public Double getCalorias() {
			return calorias;
		}

		public void setCalorias() {
			this.calorias = this.alimentos.entrySet().stream().mapToDouble(a->a.getKey().getCalorias()*a.getValue()).sum();
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
		
		//OTROS METODOS
		public void addAlimento (Alimento alimento, Double gramos) {
			if (this.alimentos == null) {
				this.alimentos = new HashMap<Alimento, Double>();
			}
			alimentos.put(alimento, gramos);
		}
		
		public void deleteAlimento (String id) {
			for (Entry<Alimento, Double> entry : this.alimentos.entrySet()) {
				if (entry.getKey().getId() == id.trim()) {
					this.alimentos.remove(entry.getKey());
					break;
				}
			}
		}
		
		public void deleteAlimento (Alimento alimento) {
			this.alimentos.remove(alimento);
		}
		
		public Alimento getAlimento(String id) {
			Alimento alimento = null;
			for (Entry<Alimento, Double> entry : this.alimentos.entrySet()) {
				if (entry.getKey().getId() == id.trim()) {
					alimento = entry.getKey();
					break;
				}
			}
			return alimento;
		}
		
		public Double getCantidadAlimento(String id) {
			Double cantidad = 0.;
			for (Entry<Alimento, Double> entry : this.alimentos.entrySet()) {
				if (entry.getKey().getId() == id.trim()) {
					cantidad = entry.getValue();
					break;
				}
			}
			return cantidad;
		}
		
}
