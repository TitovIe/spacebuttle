CREATE TABLE gameUser
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nick_name VARCHAR(250) NOT NULL,
    hash      VARCHAR(250) NOT NULL
);

CREATE TABLE player
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    game_id   INT NOT NULL,
    nick_name VARCHAR(250) NOT NULL
);

CREATE TABLE gameDto
(
    id INT AUTO_INCREMENT PRIMARY KEY
);

ALTER TABLE player
    ADD FOREIGN KEY (game_id)
        REFERENCES gameDto(id);