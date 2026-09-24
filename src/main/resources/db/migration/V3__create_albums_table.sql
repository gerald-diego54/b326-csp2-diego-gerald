CREATE TABLE albums
(
    id         INT          NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    year       DATE                  DEFAULT NULL,
    artist_id  INT          NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at DATETIME              DEFAULT NULL,

    PRIMARY KEY (id),

    KEY        artist_id_album_idx (artist_id),

    CONSTRAINT artist_id_album
        FOREIGN KEY (artist_id)
            REFERENCES artists (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
