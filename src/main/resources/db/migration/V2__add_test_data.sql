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
VALUES (4, 'Daily show', 'Episode 12/25', 30, 10, 'TV_SHOW');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (5, 'Weekly show', 'Show airing once a week', 30, null, 'TV_SHOW');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (6, 'Action movie', 'Action movie shown only once', 120, null, 'MOVIE');

INSERT INTO programs (id, name, description, duration, number_of_episodes, program_type)
VALUES (7, 'Documentary show', 'Awesome documentary show', 60, null, 'DOCUMENTARY');

-- Create scheduled programs for 'Kanal 1'
-- Evening news - every day
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 1, '["ALL"]', '21:00:00', '21:30:00', '2023-08-21', null);

-- Kids show - week days
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 2, '["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" ]', '07:00:00', '07:15:00', '2023-08-21', null);

-- Weekend kids show - weekends
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 3, '["SATURDAY", "SUNDAY"]', '07:00:00', '07:15:00', '2023-08-21', null);

-- Action movie - once
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (1, 6, '["NONE"]', '12:00:00', '14:00:00', '2023-08-28', '2023-08-28');

-- Create scheduled programs for 'Kanal 2'
-- Daily show - every day
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (2, 4, '["ALL"]', '20:00:00', '20:30:00', '2023-08-17', '2023-08-31');

-- Weekly show - week days
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (2, 5, '["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"]', '22:00:00', '22:30:00','2023-08-21', null);

-- Documentary show - once
INSERT INTO scheduled_programs (channel_id, program_id, recurring_days, start_time, end_time, start_date, end_date)
VALUES (2, 5, '["NONE"]', '12:00:00', '13:00:00','2023-08-31', null);
