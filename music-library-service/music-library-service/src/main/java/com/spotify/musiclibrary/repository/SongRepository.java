package com.spotify.musiclibrary.repository;

import com.spotify.musiclibrary.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song,Long> {
}
