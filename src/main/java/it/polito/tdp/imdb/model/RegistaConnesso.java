package it.polito.tdp.imdb.model;

public class RegistaConnesso implements Comparable<RegistaConnesso>{

	private Director d;
	private double peso;
	
	public RegistaConnesso(Director d, double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	
	public Director getD() {
		return d;
	}

	public void setD(Director d) {
		this.d = d;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return this.d.getId()+" - "+d.getFirstName()+" "+d.getLastName()+" - # attori condivisi: "+(int)this.getPeso();
	}
	@Override
	public int compareTo(RegistaConnesso o) {
		// TODO Auto-generated method stub
		return (int)(o.getPeso()-this.peso);
	}
	
}
