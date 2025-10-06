package com.spotify.musiclibrary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table (name = "songs")
public class Song implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 150)
    private String title;

    @Column(length = 100)
    private String artist;

    @Column(length = 100)
    private String genre;

    @Column(name = "duration_sec")
    private Integer duration; // duration in seconds

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "album_id")
    private Album album;

    // Constructors
    public Song() {}

    public Song(String title, String artist, String genre, Integer duration) {
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getArtist() { return artist; }

    public void setArtist(String artist) { this.artist = artist; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public Integer getDuration() { return duration; }

    public void setDuration(Integer duration) { this.duration = duration; }

    public Album getAlbum() { return album; }

    public void setAlbum(Album album) { this.album = album; }
}
