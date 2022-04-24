package aiss.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import aiss.model.Alimento.Alergeno;
import aiss.model.Alimento.Temporada;

public class Plato {
		
		//ATRIBUTOS
		private String id;
		private String nombre;
		private List<Alimento> alimentos;
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
		
		public Plato (String id, String nombre, List<Alimento> alimentos, String provinciaOrigen,
				String CAOrigen, Temporada temporada) {
			this.id = id;
			this.nombre = nombre;
			this.alimentos = alimentos;
			this.listaAlergenos = this.alimentos.stream().map(a->a.getAlergeno()).collect(Collectors.toSet());
			this.calorias = this.alimentos.stream().mapToDouble(a->a.getCalorias()).sum();
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

		public List<Alimento> getAlimentos() {
			return alimentos;
		}

		public void setAlimentos(List<Alimento> alimentos) {
			this.alimentos = alimentos;
		}

		public Set<Alergeno> getListaAlergenos() {
			return listaAlergenos;
		}

		public void setListaAlergenos() {
			this.listaAlergenos = this.alimentos.stream().map(a->a.getAlergeno()).collect(Collectors.toSet());
		}

		public Double getCalorias() {
			return calorias;
		}

		public void setCalorias() {
			this.calorias = this.alimentos.stream().mapToDouble(a->a.getCalorias()).sum();
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
		public void addAlimento (Alimento alimento) {
			if (this.alimentos == null) {
				this.alimentos = new ArrayList<Alimento>();
			}
			alimentos.add(alimento);
		}
		
		public void deleteAlimento (String id) {
			for (int i = 0; i < this.alimentos.size(); i++) {
				if (this.alimentos.get(i).getId() == id.trim()) {
					this.alimentos.remove(i);
					break;
				}
			}
		}
		
		public void deleteAlimento (Alimento alimento) {
			this.alimentos.remove(alimento);
		}
		
		public Alimento getAlimento(String id) {
			Alimento alimento = null;
			for (int i = 0; i < this.alimentos.size(); i++) {
				if (this.alimentos.get(i).getId() == id.trim()) {
					alimento = this.alimentos.get(i);
					break;
				}
			}
			return alimento;
		}
		
}
