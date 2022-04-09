/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import hr.algebra.model.Person;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author GamerGruft
 */
public interface Repository {
    
    int createMovie(Movie movie) throws Exception;
    void createMovies(List<Movie> movies) throws Exception;
    List<Movie> selectMovies() throws Exception;
    Movie selectMovie(int id) throws Exception;
    void updateMovie(int id, Movie movie) throws Exception;
    void deleteMovie(int id) throws Exception;
    void deleteMovies() throws Exception;
    
    int createPerson(Person person) throws Exception;
    Optional<Person> selectPerson(int id) throws Exception;
    List<Person> selectPersons() throws Exception;
    void updatePerson(int id, Person person) throws Exception;
    void deletePerson(int id) throws Exception;
    
    void createActor(int idPerson, int idMovie) throws Exception;
    public List<Person> selectActors(int idMovie) throws Exception ;
    void deleteActor(int idPerson, int idMovie) throws Exception;
    
    void createDirector(int idPerson, int idMovie) throws Exception;
    public List<Person> selectDirectors(int idMovie) throws Exception ;
    void deleteDirector(int idPerson, int idMovie) throws Exception;
}
