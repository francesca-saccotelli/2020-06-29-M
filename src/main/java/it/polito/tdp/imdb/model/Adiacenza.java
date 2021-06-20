package it.polito.tdp.imdb.model;

public class Adiacenza {

	private Director d1;
	private Director d2;
	private Double peso;
	public Adiacenza(Director d1, Director d2, Double peso) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.peso = peso;
	}
	public Director getD1() {
		return d1;
	}
	public void setD1(Director d1) {
		this.d1 = d1;
	}
	public Director getD2() {
		return d2;
	}
	public void setD2(Director d2) {
		this.d2 = d2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
}
