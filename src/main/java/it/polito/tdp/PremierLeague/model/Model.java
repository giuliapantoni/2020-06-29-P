package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.Adiacenza;
import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Match, DefaultWeightedEdge> grafo;
	private Map<Integer, Match> idMap = new HashMap<Integer, Match>();
	private List<Match> vertici ;
	private List<Adiacenza> adiacenze ;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		for(Match m : this.dao.listAllMatches()) {
			this.idMap.put(m.matchID, m);
		}
	}
	
	public List<Integer> getMesi(){
		return this.dao.getMesi();
	}
	
	
	public void creaGrafo(Integer min, Map<Integer, Match> idMap) {
		// creo il grafo
		this.grafo = new SimpleWeightedGraph<Match, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		// aggiungo i vertici
		this.vertici = this.dao.getMatches(min, idMap);
		Graphs.addAllVertices(this.grafo, vertici);
		// aggiungo gli archi
		this.adiacenze = this.dao.getAdiacenze(min,idMap);
		for(Adiacenza a : adiacenze) {
			if(this.grafo.containsVertex(a.getM1()) &&  this.grafo.containsVertex(a.getM2()) && a.getPeso() != null) {
				Graphs.addEdgeWithVertices(this.grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
	}
	
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}

	public Map<Integer, Match> getMap() {
		return this.idMap;
	}
	
	public List<Adiacenza> getCollegamenti(Integer mese, Integer min, Map<Integer, Match> idMap){
		return this.dao.getCollegamenti(mese, min, idMap);
	}
	
	
}