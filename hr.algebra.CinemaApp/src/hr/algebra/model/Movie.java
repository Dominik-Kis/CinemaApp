/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author GamerGruft
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"title", "publishedDate", "orgTitle", "directors", "actors", "duration", "genres", "picturePath", "description"})
public class Movie {

    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @XmlAttribute
    private int id;
    private String title;
    @XmlElement(name = "publisheddate")
    @XmlJavaTypeAdapter(PublishedDataAdapter.class)
    private LocalDateTime publishedDate;
    private String description;
    @XmlElement(name = "originaltitle")
    private String orgTitle;
    @XmlElementWrapper
    @XmlElement(name = "director")
    private List<Person> directors;
    @XmlElementWrapper
    @XmlElement(name = "actor")
    private List<Person> actors;
    private int duration;
    private String genres;
    @XmlElement(name = "picturepath")
    private String picturePath;
    
    public Movie() {}

    public Movie(String titel, LocalDateTime pubDate, String description, String orgTitle, int duration, String genre, String picPath) {
        this.title = titel;
        this.publishedDate = pubDate;
        this.description = description;
        this.orgTitle = orgTitle;
        this.duration = duration;
        this.genres = genre;
        this.picturePath = picPath;
    }

    public Movie(int id, String titel, LocalDateTime pubDate, String description, String orgTitle, int duration, String genre, String picPath) {
        this.id = id;
        this.title = titel;
        this.publishedDate = pubDate;
        this.description = description;
        this.orgTitle = orgTitle;
        this.duration = duration;
        this.genres = genre;
        this.picturePath = picPath;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
        
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDateTime publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() { 
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgTitle() {
        return orgTitle;
    }

    public void setOrgTitle(String orgTitle) {
        this.orgTitle = orgTitle;
    }

    public List<Person> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Person> directors) {
        this.directors = directors;
    }

    public List<Person> getActors() {
        return actors;
    }

    public void setActors(List<Person> actors) {
        this.actors = actors;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }
    
}
