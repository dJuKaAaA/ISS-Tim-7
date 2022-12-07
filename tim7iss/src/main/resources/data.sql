
-- location creation
INSERT INTO location (name, longitude, latitude)
    VALUES ('Neka tamo lokacija', 1.5, 1.5);

-- vehicle type creation
INSERT INTO vehicle_type (price_per_km, name)
    VALUES (100, 'STANDARDNO');

-- driver creation
INSERT INTO driver (id,first_name, last_name, profile_picture, phone_number,
                    email_address, address, password, is_blocked, is_active)
    VALUES (1,'Mika', 'Mikic', 'saimse', '8149081249081', 'mika@mikic.rs',
            'Mikina kuca', 'nekasifra', FALSE, FALSE);

-- vehicles creation
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
    VALUES ('Neki tamo model 1', 'Redzistrejsn plejt 1', 5, FALSE, TRUE,
        1, 1, 1);
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
        1, NULL, 1);

-- rides creation
INSERT INTO ride (price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);
INSERT INTO ride (price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);
INSERT INTO ride (price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            3, 1, 1);
INSERT INTO ride (price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);

-- insert admin
INSERT INTO admin (id, email_address, first_name, is_active, is_blocked, last_name, password, phone_number,
                   profile_picture, username)
VALUES (1, 'ivanmartic@gamil.com', 'Ivan', false, false, 'Martic', 'password', '0604672999', '', 'ivanmartic');

-- insert driver
INSERT INTO driver (id, first_name, last_name, profile_picture, phone_number,
                    email_address, address, password, is_blocked, is_active)
VALUES (2, 'Mika', 'Mikic', 'saimse', '8149081249081', 'mika@mikic.rs',
        'Mikina kuca', 'nekasifra', FALSE, FALSE);

-- insert document
INSERT INTO document (driver_id, name, picture_path)
VALUES (2, 'saobracajna', '');
INSERT INTO document (driver_id, name, picture_path)
VALUES (2, 'vozacka', '');
INSERT INTO document (driver_id, name, picture_path)
VALUES (2, 'licna', '');

-- insert vehicle type
INSERT INTO vehicle_type (price_per_km, name)
VALUES (100, 'STANDARDNO');

-- insert location
INSERT
INTO location (name, longitude, latitude)
VALUES ('Neka tamo lokacija', 1.5, 1.5);

-- inset vehicle
INSERT INTO vehicle (model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
VALUES ('Neki tamo model 1', 'Redzistrejsn plejt 1', 5, FALSE, TRUE,
        1, 2, 1);

-- insert passengers
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
VALUES (3, 'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
        'Zikina kuca', 'nekasifra', FALSE, FALSE);
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (4,'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (5,'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (123,'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);

-- user activation creation
INSERT INTO user_activation (creation_date, expiration_date, user_id)
VALUES ('2022-12-21', '2022-12-21', 3);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
VALUES ('2022-12-21', '2022-12-21', 4);

-- insert ride
INSERT INTO ride (price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
VALUES (154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
        0, 2, 1);

-- rides and passengers finished rides creation
INSERT INTO finished_rides (passenger_id, ride_id)
VALUES (3, 1);
INSERT INTO finished_rides (passenger_id, ride_id)
VALUES (4, 1);

-- user activation creation
INSERT INTO user_activation (creation_date, expiration_date, user_id)
    VALUES ('2022-12-21', '2023-12-21', 2);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
    VALUES ('2022-12-21', '2023-12-21', 3);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
    VALUES ('2022-12-21', '2023-12-21', 4);
INSERT INTO user_activation (creation_date, expiration_date, user_id)
    VALUES ('2022-12-21', '2023-12-21', 5);

-- documents creation
INSERT INTO document (driver_id, name, picture_path)
    VALUES (1, 'saobracajna', '');
INSERT INTO document (driver_id, name, picture_path)
    VALUES (1, 'vozacka', '');
INSERT INTO document (driver_id, name, picture_path)
    VALUES (1, 'licna', '');

-- work hour creation
INSERT INTO work_hour (driver_id, start_date, end_date)
VALUES (1, '2022-12-21', '2022-12-21');
INSERT INTO work_hour (driver_id, start_date, end_date)
VALUES (2, '2022-12-21', '2022-12-21');

-- panic creation
INSERT INTO panic (reason, sent_time, ride_id, user_id) values ('Reason','2022-12-21',3,2);

-- create messages
INSERT INTO message(content, receiver_id, ride_id, sender_id, sent_date, type)
VALUES ('message1', 2, 1, 3, '2022-12-21', 'STANDARD');
INSERT
INTO message(content, receiver_id, ride_id, sender_id, sent_date, type)
VALUES ('message2', 3, 1, 2, '2022-12-21', 'STANDARD');


