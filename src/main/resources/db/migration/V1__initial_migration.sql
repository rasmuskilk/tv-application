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

-- Create channels
INSERT INTO channels (id, name, description)
VALUES (1, 'Kanal 1', 'Kanal 1 description');
INSERT INTO channels (id, name, description)
VALUES (2, 'Kanal 2', 'Kanal 2 description');

-- Create programs
INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (1, 'Evening news', 'Your everyday news', 30, null, 'NEWS');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (2, 'Kids show', 'Fun show for kids weekdays', 15, null, 'KIDS_SHOW');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (3, 'Weekend kids show', 'Fun show for kids weekends', 15, null, 'KIDS_SHOW');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (4, 'Daily show', 'Episode 12/25', 15, 5, 'TV_SHOW');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (5, 'Weekly show', 'Show airing once a week', 30, null, 'TV_SHOW');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (6, 'Action movie', 'Action movie shown only once', 120, null, 'MOVIE');

-- Create scheduled programs for 'Kanal 1'
-- Daily news
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 1, '["ALL"]', '21:00:00', '21:30:00', '2023-08-19', null);

-- Week days kids show
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 2, '["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" ]', '07:00:00', '07:15:00', '2023-08-19', null);

-- Weekends kids show
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 3, '["SATURDAY", "SUNDAY"]', '07:00:00', '07:15:00', '2023-08-19', null);

-- Action movie
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 6, '["NONE"]', '12:00:00', '14:00:00', '2023-08-21', '2023-08-21')

# -- Create scheduled programs for 'Kanal 2'
# -- Daily show
# INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
# VALUES (2, 3, '["ALL"]', '20:00:00', '20:15:00', '2023-08-20', '2023-08-23');
#
# -- Weekly show
# INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
# VALUES (2, 4, '["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]', '22:00:00', '22:30:00','2023-08-20', null);
#
# -- Weekly show
# INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
# VALUES (2, 4, '["SATURDAY", "SUNDAY"]', '22:00:00', '22:30:00', '2023-08-20', null);
