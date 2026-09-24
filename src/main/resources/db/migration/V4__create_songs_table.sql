CREATE TABLE songs
(
    id         INT          NOT NULL AUTO_INCREMENT,
    title      VARCHAR(255) NOT NULL,
    length     VARCHAR(45)  NOT NULL,
    genre      VARCHAR(45)  NOT NULL,
    album_id   INT          NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at DATETIME              DEFAULT NULL,

    PRIMARY KEY (id),

    KEY        album_id_songs_idx (album_id),

    CONSTRAINT album_id_songs
        FOREIGN KEY (album_id)
            REFERENCES albums (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
