package com.spotify.musiclibrary.controller;


import com.spotify.musiclibrary.entity.Album;
import com.spotify.musiclibrary.service.AlbumService;
import com.spotify.musiclibrary.servicefeign.UserClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping ("/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final UserClient userClient;

    public AlbumController(AlbumService albumService,
                           UserClient userClient) {
        this.albumService = albumService;
        this.userClient = userClient;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return ResponseEntity.ok(albumService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return albumService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        return ResponseEntity.ok(albumService.save(album));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @RequestBody Album album) {
        return albumService.findById(id)
                .map(existing -> {
                    existing.setTitle(album.getTitle());
                    existing.setArtist(album.getArtist());
                    existing.setGenre(album.getGenre());
                    existing.setReleaseYear(album.getReleaseYear());
                    return ResponseEntity.ok(albumService.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/check-role/{username}")
    public String checkRole(@PathVariable String username) {
        String role = userClient.getUserRole(username);
        return "User " + username + " has role " + role;
    }
}
