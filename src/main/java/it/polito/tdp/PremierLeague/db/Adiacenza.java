package it.polito.tdp.PremierLeague.db;

import it.polito.tdp.PremierLeague.model.Match;

public class Adiacenza{

	private Match m1;
	private Match m2;
	private Integer peso;
	/**
	 * @param m1
	 * @param m2
	 * @param peso
	 */
	public Adiacenza(Match m1, Match m2, Integer peso) {
		super();
		this.m1 = m1;
		this.m2 = m2;
		this.peso = peso;
	}
	public Match getM1() {
		return m1;
	}
	public void setM1(Match m1) {
		this.m1 = m1;
	}
	public Match getM2() {
		return m2;
	}
	public void setM2(Match m2) {
		this.m2 = m2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [m1=" + m1 + ", m2=" + m2 + ", peso = " + peso + " ]";
	}
	
	
	
	
}
