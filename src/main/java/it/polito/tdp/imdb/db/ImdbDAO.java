package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map<Integer,Director>idMap){
		String sql = "SELECT * FROM directors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				if(!idMap.containsKey(res.getInt("id"))) {
					Director d=new Director(res.getInt("id"),res.getString("first_name"),res.getString("last_name"));
					idMap.put(d.getId(), d);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Integer>getAnni(){
		String sql="SELECT DISTINCT m.year "
				+ "FROM movies AS m "
				+ "WHERE m.year>=2004 AND m.year<=2006 "
				+ "ORDER BY m.year";
		List<Integer>anni=new ArrayList<Integer>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				anni.add(res.getInt("m.year"));
			}
			conn.close();
			return anni;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Director>getVertici(Map<Integer,Director>idMap,int anno){
		String sql="SELECT DISTINCT d.id,d.first_name,d.last_name "
				+ "FROM movies AS m, movies_directors AS md,directors AS d "
				+ "WHERE m.id=md.movie_id AND "
				+ "md.director_id=d.id AND m.year=? "
				+ "ORDER BY d.id";
		List<Director>registi=new ArrayList<Director>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("d.id"))) {
		    		registi.add(idMap.get(res.getInt("d.id")));
		    	}
			}
			conn.close();
		    return registi;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<Adiacenza>getAdiacenze(Map<Integer,Director>idMap,int anno){
		String sql="SELECT md1.director_id,md2.director_id,COUNT(DISTINCT r1.actor_id) AS peso "
				+ "FROM movies_directors AS md1, movies_directors AS md2, "
				+ "roles AS r1,roles AS r2, movies AS m1,movies AS m2 "
				+ "WHERE md1.director_id>md2.director_id AND "
				+ "m1.year=? AND m2.year=? AND m1.id=md1.movie_id AND m2.id=md2.movie_id AND "
				+ "(md1.movie_id<>md2.movie_id OR md1.movie_id=md2.movie_id) "
				+ "AND r1.movie_id=md1.movie_id AND r2.movie_id=md2.movie_id "
				+ "AND r1.actor_id=r2.actor_id "
				+ "GROUP BY md1.director_id,md2.director_id";
		List<Adiacenza>adiacenze=new ArrayList<Adiacenza>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				adiacenze.add(new Adiacenza(idMap.get(res.getInt("md1.director_id")),idMap.get(res.getInt("md2.director_id")),res.getDouble("peso")));
			}
			conn.close();
		    return adiacenze;
		}catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
