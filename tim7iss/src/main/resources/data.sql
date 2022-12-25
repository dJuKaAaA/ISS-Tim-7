-- location creation
INSERT INTO location (name, longitude, latitude)
VALUES ('Valentina Vodnika 10, Novi Sad', 19.8366902, 45.255956);

INSERT INTO location (name, longitude, latitude)
VALUES ('Beogradska 7, Petrovaradin', 19.8612956, 45.254896);

INSERT INTO location (name, longitude, latitude)
VALUES ('Fakultet tehnickih nauka Univerziteta u Novom Sadu, Trg Dositeja Obradovica, Novi Sad', 19.8516641,
        45.24648813);

-- vehicle type creation
INSERT INTO vehicle_type (price_per_km, name)
VALUES (100, 'STANDARDNO');

-- driver creation
INSERT INTO ggcj_users (first_name, last_name, profile_picture, phone_number,
                        email_address, address, password, is_blocked, is_active, enabled, last_password_reset_date,
                        dtype)
VALUES ('Mika', 'Mikic', 'saimse', '8149081249081', 'mika1@mikic.rs',
        'Mikina kuca', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', FALSE, FALSE, TRUE,
        '2017-10-01 18:57:58.508-07', 'Driver');
INSERT INTO ggcj_users (first_name, last_name, profile_picture, phone_number,
                        email_address, address, password, is_blocked, is_active, enabled, last_password_reset_date,
                        dtype)
VALUES ('Zoran', 'Zoranovic', 'saimse', '8149081249081', 'zoran1@zoranovic.rs',
        'Zoranova kuca', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', FALSE, FALSE, TRUE,
        '2017-10-01 18:57:58.508-07', 'Driver');

-- rides creation
INSERT INTO ride (price, start_time, end_time, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
        0, 1, 1);
INSERT INTO ride (price, start_time, end_time, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
        0, 1, 1);
INSERT INTO ride (price, start_time, end_time, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
        3, 1, 1);
INSERT INTO ride (price, start_time, end_time, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
        3, 2, 1);

-- route creation
INSERT INTO route(starting_point_id, end_point_id, distance)
VALUES (1, 2, 150);
INSERT INTO route(starting_point_id, end_point_id, distance)
VALUES (2, 3, 150);

INSERT INTO ggcj_users (first_name, last_name, profile_picture, phone_number,
                 email_address, address, password, is_blocked, is_active, enabled, last_password_reset_date, dtype)
VALUES ('Zika', 'Zikic', 'saimse', '8149081249081',
        'zika1@zikic.rs', 'Zikina kuca', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', FALSE,
        FALSE, TRUE, '2017-10-01 18:57:58.508-07', 'Passenger');
INSERT
INTO ggcj_users (first_name, last_name, profile_picture, phone_number,
                 email_address, address, password, is_blocked, is_active, enabled, last_password_reset_date, dtype)
VALUES ('Zika', 'Zikic', 'saimse', '8149081249081', 'zika2@zikic.rs',
        'Zikina kuca', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', FALSE, FALSE, TRUE,
        '2017-10-01 18:57:58.508-07', 'Passenger');

INSERT
INTO ggcj_users (first_name, last_name, profile_picture, phone_number,
                 email_address, address, password, is_blocked, is_active, enabled, last_password_reset_date, dtype)
VALUES ('Zika', 'Zikic', 'saimse', '8149081249081', 'zika3@zikic.rs',
        'Zikina kuca', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', FALSE, FALSE, TRUE,
        '2017-10-01 18:57:58.508-07', 'Passenger');

INSERT
INTO ggcj_users (first_name, last_name, profile_picture, phone_number,
                 email_address, address, password, is_blocked, is_active, enabled, last_password_reset_date, dtype)
VALUES ('Zika', 'Zikic', 'saimse', '8149081249081', 'zika4@zikic.rs',
        'Zikina kuca', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', FALSE, FALSE, TRUE,
        '2017-10-01 18:57:58.508-07', 'Passenger');

-- rides and passengers passenger rides creation
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (6, 1);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (6, 2);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (6, 4);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (3, 4);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (4, 1);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (4, 2);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (5, 2);
INSERT INTO passenger_rides (passenger_id, ride_id)
VALUES (5, 4);

INSERT INTO ggcj_users (email_address, first_name, is_active, is_blocked, last_name, password, phone_number,
                        profile_picture, username, enabled, last_password_reset_date, dtype)
VALUES ('ivanmartic@gamil.com', 'Ivan', false, false, 'Martic',
        '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', '0604672999', '', 'ivanmartic', true,
        '2017-10-01 18:57:58.508-07',
        'Admin');

-- Dodati sliku u bajtovima
-- -- insert document
-- INSERT INTO document (driver_id, name, picture_path)
-- VALUES (1, 'saobracajna', '');
-- INSERT INTO document (driver_id, name, picture_path)
-- VALUES (1, 'vozacka', '');
-- INSERT INTO document (driver_id, name, picture_path)
-- VALUES (1, 'licna', '');

-- insert location
INSERT INTO location (name, latitude, longitude)
VALUES ('Neka tamo lokacija', 1.5, 1.5);
INSERT INTO location (name, latitude, longitude)
VALUES ('Zmaj Jovina 26', 45.25685617386568, 19.84799528633145);
INSERT INTO location (name, latitude, longitude)
VALUES ('Bulevar cara Lazara', 45.24863618765179, 19.85191711614038);

---- insert route
INSERT INTO route (distance, end_point_id, starting_point_id)
VALUES (1200, 4, 3);
INSERT INTO route (distance, end_point_id, starting_point_id)
VALUES (1200, 3, 4);

-- vehicles creation
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
VALUES ('Neki tamo model 1', 'Redzistrejsn plejt 1', 5, FALSE, TRUE,
        1, 1, 3);
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
VALUES ('Neki tamo model 2', 'Redzistrejsn plejt 2', 5, FALSE, TRUE,
        1, NULL, 1);
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
VALUES ('Neki tamo model 3', 'Redzistrejsn plejt 3', 5, FALSE, TRUE,
        1, NULL, 1);
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
VALUES ('Neki tamo model 4', 'Redzistrejsn plejt 4', 5, FALSE, TRUE,
        1, NULL, 1);
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
VALUES ('Neki tamo model 5', 'Redzistrejsn plejt 5', 5, FALSE, TRUE,
        1, 2, 4);

-- user activation creation
INSERT INTO user_activation (creation_date, expiration_date, user_id)
VALUES ('2022-12-21', '2022-12-21', 3);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
VALUES ('2022-12-21', '2023-12-21', 2);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
VALUES ('2022-12-21', '2023-12-21', 4);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
VALUES ('2022-12-21', '2023-12-21', 5);


-- work hour creation
INSERT INTO work_hour (driver_id, start_date, end_date)
VALUES (1, '2022-12-21', '2022-12-21');
INSERT INTO work_hour (driver_id, start_date, end_date)
VALUES (2, '2022-12-21', '2022-12-21');

-- panic creation
INSERT INTO panic (reason, sent_time, ride_id, user_id)
values ('Reason', '2022-12-21', 3, 2);

-- create messages
INSERT INTO message(content, receiver_id, ride_id, sender_id, sent_date, type)
VALUES ('message1', 2, 1, 3, '2022-12-21', 'STANDARD');
INSERT INTO message(content, receiver_id, ride_id, sender_id, sent_date, type)
VALUES ('message2', 2, 1, 3, '2022-12-21', 'STANDARD');

-- create review
INSERT INTO reviews (DTYPE, PASSENGER_ID, DRIVER_ID, VEHICLE_ID, RIDE_ID, RATING, COMMENT)
VALUES ('VehicleReview', 6, 1, 1, 1, 3, 'Comment');
INSERT INTO reviews (DTYPE, PASSENGER_ID, DRIVER_ID, VEHICLE_ID, RIDE_ID, RATING, COMMENT)
VALUES ('DriverReview', 6, 1, 1, 1, 3, 'Comment');


INSERT INTO ROLE(name)
VALUES ('ROLE_PASSENGER');
INSERT INTO ROLE(name)
VALUES ('ROLE_DRIVER');
INSERT INTO ROLE(name)
VALUES ('ROLE_ADMIN');

INSERT INTO USER_ROLE (user_id, role_id)
VALUES (1, 2);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (2, 2);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (3, 1);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (4, 1);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (5, 1);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (6, 1);
INSERT INTO USER_ROLE (user_id, role_id)
VALUES (7, 3);