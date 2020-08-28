package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Integer> getMesi(){
		String sql = "SELECT DISTINCT MONTH(DATE) AS mese " + 
				"FROM matches " + 
				"ORDER BY mese ASC " ;
		List<Integer> result = new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				result.add(res.getInt("mese"));
			}
			conn.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public List<Match> getMatches(Integer mese, Map<Integer, Match> idMap){
		String sql = "SELECT DISTINCT m.MatchID AS mid " + 
				"FROM matches m " + 
				"WHERE MONTH(DATE) = ? " ;
		List<Match> result = new ArrayList<Match>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				if(idMap.containsKey(res.getInt("mid"))) {
					result.add(idMap.get(res.getInt("mid")));
				}
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public List<Adiacenza> getAdiacenze(Integer min, Map<Integer, Match> idMap){
		String sql = "SELECT a1.MatchID AS m1, a2.MatchID AS m2, COUNT(DISTINCT a1.PlayerID) AS peso " + 
				"FROM actions a1, actions a2 " + 
				"WHERE a1.TimePlayed >= ? " + 
				"AND a2.TimePlayed >= ? " + 
				"AND a1.PlayerID = a2.PlayerID " + 
				"AND a1.MatchID < a2.MatchID " + 
				"GROUP BY m1, m2 " ;
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, min);
			st.setInt(2, min);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				if(idMap.containsKey(res.getInt("m1")) && idMap.containsKey(res.getInt("m2"))){
					adiacenze.add(new Adiacenza(idMap.get(res.getInt("m1")), idMap.get(res.getInt("m2")), res.getInt("peso")));
				}
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return adiacenze;
	}
	
	
	
	public List<Adiacenza> getCollegamenti(Integer mese, Integer min, Map<Integer, Match> idMap){
		String sql = "SELECT m1.MatchID AS id1, m2.MatchID AS id2, COUNT(*) AS conto " + 
					"FROM actions a1, actions a2, matches m1, matches m2 " + 
					"WHERE MONTH(m1.Date)=? AND MONTH(m2.Date)=? AND m1.MatchID>m2.MatchID AND a1.MatchID=m1.MatchID AND a2.MatchID=m2.MatchID " + 
					"AND a1.TimePlayed>=? AND a2.TimePlayed>=? AND a1.PlayerID=a2.PlayerID " + 
					"GROUP BY m1.MatchID, m2.MatchID " + 
					"ORDER BY conto DESC " ;
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setInt(2, mese);
			st.setInt(3, min);
			st.setInt(4, min);
			ResultSet res = st.executeQuery();
			while(res.next()) {
				result.add(new Adiacenza(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")), res.getInt("conto")));
			}
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
