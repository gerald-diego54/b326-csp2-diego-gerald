CREATE TABLE playlists
(
    id         INT      NOT NULL AUTO_INCREMENT,
    user_id    INT      NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at DATETIME          DEFAULT NULL,

    PRIMARY KEY (id),

    KEY        user_id_playlists_idx (user_id),

    CONSTRAINT user_id_playlists
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
