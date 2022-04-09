/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import hr.algebra.dal.Repository;
import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author GamerGruft
 */
public class SqlRepository implements Repository{

    private static final String ID_MOVIE="IDMovie";
    private static final String TITLE="Title";
    private static final String PUBLISHED_DATE="PublishedDate";
    private static final String DESCRIPTION="Description";
    private static final String ORG_TITLE="ORGTitle";
    private static final String DURATION="Duration";
    private static final String GENRE="Genres";
    private static final String PICTURE_PATH="PicturePath";
    private static final String ID_PERSON = "IDPerson";
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    
    private static final String CREATE_MOVIE="{CALL createMovie(?,?,?,?,?,?,?,?)}";
    private static final String UPDATE_MOVIE="{CALL updateMovie(?,?,?,?,?,?,?,?)}";
    private static final String DELETE_MOVIE="{CALL deleteMovie(?)}";
    private static final String SELECT_MOVIE="{CALL selectMovie(?)}";
    private static final String SELECT_MOVIES="{CALL selectMovies}";
    private static final String DELETE_MOVIES = "{ CALL deleteMovies }";
    
    private static final String CREATE_PERSON = "{ CALL createPerson (?,?,?) }";
    private static final String UPDATE_PERSON = "{ CALL updatePerson (?,?,?) }";
    private static final String DELETE_PERSON = "{ CALL deletePerson (?) }";
    private static final String SELECT_PERSON = "{ CALL selectPerson (?) }";
    private static final String SELECT_PERSONS = "{ CALL selectPersons }";
    
    private static final String SELECT_ACTORS = "{ CALL selectActors (?) }";
    private static final String CREATE_ACTOR = "{ CALL createActor (?,?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?,?) }";
    private static final String SELECT_DIRECTORS = "{ CALL selectDirectors (?) }";
    private static final String CREATE_DIRECTOR = "{ CALL createDirector (?,?) }";
    private static final String DELETE_DIRECTOR = "{ CALL deleteDirector (?,?) }";
    
    
    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try(Connection con=dataSource.getConnection();
            CallableStatement stmt=con.prepareCall(CREATE_MOVIE)){
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setString(3, movie.getDescription());
            stmt.setString(4, movie.getOrgTitle());
            stmt.setInt(5, movie.getDuration());
            stmt.setString(6, movie.getGenres());
            stmt.setString(7, movie.getPicturePath());
            stmt.registerOutParameter(8, Types.INTEGER);
            
            stmt.executeUpdate();
            
            return stmt.getInt(8);
        }
    }

    @Override
    public void createMovies(List<Movie> movies) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            for (Movie movie : movies) {
                stmt.setString(1, movie.getTitle());
                stmt.setString(2, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
                stmt.setString(3, movie.getDescription());
                stmt.setString(4, movie.getOrgTitle());
                stmt.setInt(5, movie.getDuration());
                stmt.setString(6, movie.getGenres());
                stmt.setString(7, movie.getPicturePath());
                stmt.registerOutParameter(8, Types.INTEGER);

                stmt.executeUpdate();
                
                movie.setId(stmt.getInt(8));
            }
            
            for (Movie movie : movies) {
                for (int i = 0; i < movie.getActors().size(); i++) {
                    createActor(createPerson(movie.getActors().get(i)),movie.getId());
                }
                for (int i = 0; i < movie.getDirectors().size(); i++) {
                    createDirector(createPerson(movie.getDirectors().get(i)),movie.getId());
                }
            }
            
        }
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_MOVIES);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                    rs.getInt(ID_MOVIE),
                    rs.getString(TITLE),
                    LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER),
                    rs.getString(DESCRIPTION),
                    rs.getString(ORG_TITLE),
                    rs.getInt(DURATION),
                    rs.getString(GENRE),
                    rs.getString(PICTURE_PATH));
                
                    movie.setActors(selectActors(movie.getId()));
                    movie.setDirectors(selectDirectors(movie.getId()));

                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public Movie selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(SELECT_MOVIE)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Movie movie = new Movie(
                rs.getInt(ID_MOVIE),
                rs.getString(TITLE),
                LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER),
                rs.getString(DESCRIPTION),
                rs.getString(ORG_TITLE),
                rs.getInt(DURATION),
                rs.getString(GENRE),
                rs.getString(PICTURE_PATH));

                movie.setActors(selectActors(movie.getId()));
                movie.setDirectors(selectDirectors(movie.getId()));
                return movie;
            }

                
        return null;
        }
    }

    @Override
    public void updateMovie(int id, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

            stmt.setInt(1, id);
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getPublishedDate().format(Movie.DATE_FORMATTER));
            stmt.setString(4, movie.getDescription());
            stmt.setString(5, movie.getOrgTitle());
            stmt.setInt(6, movie.getDuration());
            stmt.setString(7, movie.getGenres());
            stmt.setString(8, movie.getPicturePath());

            stmt.executeUpdate();
        }    
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(DELETE_MOVIE)){
            stmt.setInt(1, id);
            stmt.executeUpdate();

        }

    }

    @Override
    public void deleteMovies() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(DELETE_MOVIES)){
            stmt.executeUpdate();

        }

    }

    @Override
    public int createPerson(Person person) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(CREATE_PERSON)){

            stmt.setString(1, person.getFirstName());
            stmt.setString(2, person.getLastName());
            stmt.registerOutParameter(3, Types.INTEGER);


            stmt.executeUpdate();
                
            return stmt.getInt(3);
        }
    }
    
    public void createPersons(List<Person> persons) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_PERSONS);
                ResultSet rs = stmt.executeQuery()) {

            for (Person person : persons) {
                stmt.setString(1, person.getFirstName());
                stmt.setString(2, person.getLastName());
                
                stmt.executeUpdate();
            }
        }
    }

    @Override
    public Optional<Person> selectPerson(int id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Person> selectPersons() throws Exception {
        List<Person> persons = new ArrayList<Person>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_PERSONS);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Person person = new Person(
                    rs.getInt(ID_PERSON),
                    rs.getString(FIRST_NAME),
                    rs.getString(LAST_NAME)
                );
                persons.add(person);
            }
        }
        return persons;
    }

    @Override
    public void updatePerson(int id, Person person) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(UPDATE_PERSON)) {

            stmt.setInt(1, id);
            stmt.setString(2, person.getFirstName());
            stmt.setString(3, person.getLastName());

            stmt.executeUpdate();
        }  
    }

    @Override
    public void deletePerson(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(DELETE_PERSON)){

            stmt.setInt(1, id);

            stmt.executeUpdate();      
        }
    }

    @Override
    public void createActor(int idPerson, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(CREATE_ACTOR)){

            stmt.setInt(1, idPerson);
            stmt.setInt(2, idMovie);

            stmt.executeUpdate();      
        }
    }

    @Override
    public List<Person> selectActors(int id) throws Exception {
        List<Person> actors = new ArrayList<Person>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_ACTORS)){
                stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Person actor = new Person(
                        rs.getInt(ID_PERSON),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME)
                    );
                    actors.add(actor);
                }
            }
        }
        return actors;
    }

    @Override
    public void deleteActor(int idPerson, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(DELETE_ACTOR)){

            stmt.setInt(1, idPerson);
            stmt.setInt(2, idMovie);

            stmt.executeUpdate();      
        }
    }

    @Override
    public void createDirector(int idPerson, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(CREATE_DIRECTOR)){

            stmt.setInt(1, idPerson);
            stmt.setInt(2, idMovie);

            stmt.executeUpdate();
                
        }
    }

    @Override
    public List<Person> selectDirectors(int id) throws Exception {
        List<Person> directors = new ArrayList<Person>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
                CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS)){
                stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Person director = new Person(
                        rs.getInt(ID_PERSON),
                        rs.getString(FIRST_NAME),
                        rs.getString(LAST_NAME)
                    );
                    directors.add(director);
                }
            }
        }   
        return directors;
    }

    @Override
    public void deleteDirector(int idPerson, int idMovie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection();
            CallableStatement stmt = con.prepareCall(DELETE_DIRECTOR)){

            stmt.setInt(1, idPerson);
            stmt.setInt(2, idMovie);

            stmt.executeUpdate();      
        }
    }
    
}
