package aiss.model;

public class Ingrediente {
	private Alimento alimento;
	private Double cantidad;
	public Ingrediente() {}
	public Ingrediente(Alimento alimento, Double cantidad) {
		super();
		this.alimento = alimento;
		this.cantidad = cantidad;
	}
	public Alimento getAlimento() {
		return alimento;
	}
	public void setAlimento(Alimento alimento) {
		this.alimento = alimento;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}
	
}
