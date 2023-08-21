-- Channels table
CREATE TABLE programs
(
    id                 INT         NOT NULL AUTO_INCREMENT,
    name               VARCHAR(64) NOT NULL,
    description        VARCHAR(255),
    duration           INT         NOT NULL,
    number_of_episodes INT,
    program_type       ENUM ('NEWS', 'MOVIE', 'DOCUMENTARY', 'ANIMATION', 'KIDS_SHOW', 'TV_SHOW') NOT NULL,
    PRIMARY KEY (id)
);

-- Programs table
CREATE TABLE channels
(
    id          INT AUTO_INCREMENT,
    name        VARCHAR(32) NOT NULL UNIQUE,
    description VARCHAR(128),
    PRIMARY KEY (id)
);

-- Scheduled programs table
CREATE TABLE scheduled_programs
(
    id         INT NOT NULL AUTO_INCREMENT,
    channel_id INT NOT NULL ,
    program_id INT NOT NULL ,
    recurring_days JSON NOT NULL ,
    start_time TIME NOT NULL ,
    end_time   TIME NOT NULL ,
    start_date DATE NOT NULL ,
    end_date DATE ,
    PRIMARY KEY (id),
    FOREIGN KEY (channel_id) REFERENCES channels (id),
    FOREIGN KEY (program_id) REFERENCES programs (id)
);
