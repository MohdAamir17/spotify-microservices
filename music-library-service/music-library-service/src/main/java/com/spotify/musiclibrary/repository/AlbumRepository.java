package com.spotify.musiclibrary.repository;

import com.spotify.musiclibrary.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {
}
