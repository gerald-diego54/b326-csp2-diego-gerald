CREATE TABLE playlist_songs
(
    id          INT      NOT NULL AUTO_INCREMENT,
    playlist_id INT      NOT NULL,
    song_id     INT      NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at  DATETIME          DEFAULT NULL,

    PRIMARY KEY (id),

    KEY         song_id_playlist_song_idx (song_id),
    KEY         playlist_id_playlist_songs_idx (playlist_id),

    CONSTRAINT playlist_id_playlist_songs
        FOREIGN KEY (playlist_id)
            REFERENCES playlists (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    CONSTRAINT song_id_playlist_song
        FOREIGN KEY (song_id)
            REFERENCES songs (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
