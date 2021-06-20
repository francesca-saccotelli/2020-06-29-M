package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	ImdbDAO dao;
	private Graph<Director,DefaultWeightedEdge>grafo;
	Map<Integer,Director>idMap;
	
	public Model() {
		dao= new ImdbDAO();
		idMap=new HashMap<>();
	    this.dao.listAllDirectors(idMap);
	}
	
	public List<Integer>getAnni(){
		return this.dao.getAnni();
	}
	public void creaGrafo(int anno) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(idMap, anno));
		for(Adiacenza a:this.dao.getAdiacenze(idMap,anno)) {
			if(this.grafo.containsVertex(a.getD1())&& this.grafo.containsVertex(a.getD2())) {
				Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
			}
		}
	}
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	public List<Director>getVertici(){
		List<Director>vertici=new ArrayList<>(this.grafo.vertexSet());
		return vertici;
	}
	public List<RegistaConnesso>getConnessi(Director d){
		List<Director>vicini=Graphs.neighborListOf(this.grafo,d);
		List<RegistaConnesso>lista=new ArrayList<>();
		for(Director v: vicini) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(d, v));
			lista.add(new RegistaConnesso(v, peso));
		}
        Collections.sort(lista);
		return lista;
	}

}
