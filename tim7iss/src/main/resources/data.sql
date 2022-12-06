-- location creation
INSERT INTO location (id, name, longitude, latitude)
    VALUES (1, 'Neka tamo lokacija', 1.5, 1.5);

-- vehicle type creation
INSERT INTO vehicle_type (id, price_per_km, name)
    VALUES (1, 100, 'STANDARDNO');

-- driver creation
INSERT INTO driver (id, first_name, last_name, profile_picture, phone_number,
                    email_address, address, password, is_blocked, is_active)
    VALUES (1, 'Mika', 'Mikic', 'saimse', '8149081249081', 'mika@mikic.rs',
            'Mikina kuca', 'nekasifra', FALSE, FALSE);

-- vehicles creation
INSERT INTO vehicle (id, model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
    VALUES (1, 'Neki tamo model 1', 'Redzistrejsn plejt 1', 5, FALSE, TRUE,
        1, 1, 1);
INSERT INTO vehicle (id, model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
    VALUES (2, 'Neki tamo model 2', 'Redzistrejsn plejt 2', 5, FALSE, TRUE,
        1, NULL, 1);
INSERT INTO vehicle (id, model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
    VALUES (3, 'Neki tamo model 3', 'Redzistrejsn plejt 3', 5, FALSE, TRUE,
        1, NULL, 1);
INSERT INTO vehicle (id, model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
    VALUES (4, 'Neki tamo model 4', 'Redzistrejsn plejt 4', 5, FALSE, TRUE,
        1, NULL, 1);
INSERT INTO vehicle (id, model, registration_plate, seat_number, baby_allowed, pets_allowed,
                     vehicle_type_id, driver_id, location_id)
    VALUES (5, 'Neki tamo model 5', 'Redzistrejsn plejt 5', 5, FALSE, TRUE,
        1, NULL, 1);

-- rides creation
INSERT INTO ride (id, price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (1, 154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);
INSERT INTO ride (id, price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (2, 154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);
INSERT INTO ride (id, price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (3, 154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);
INSERT INTO ride (id, price, start_date, end_date, estimated_time_in_minutes,
                  baby_on_board, pet_on_board, split_fare, status, driver_id,
                  vehicle_type_id)
    VALUES (4, 154, '2022-12-21', '2022-12-21', 10, FALSE, TRUE, FALSE,
            0, 1, 1);

-- passengers creation
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (2, 'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (3, 'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (4, 'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);
INSERT INTO passenger (id, first_name, last_name, profile_picture, phone_number,
                       email_address, address, password, is_blocked, is_active)
    VALUES (5, 'Zika', 'Zikic', 'saimse', '8149081249081', 'zika@zikic.rs',
            'Zikina kuca', 'nekasifra', FALSE, FALSE);

-- rides and passengers finished rides creation
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (2, 1);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (2, 2);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (2, 3);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (2, 4);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (3, 3);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (3, 4);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (4, 1);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (4, 2);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (4, 3);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (5, 2);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (5, 3);
INSERT INTO finished_rides (passenger_id, ride_id)
    VALUES (5, 4);

-- user activation creation
INSERT INTO user_activation (id, creation_date, expiration_date, user_id)
    VALUES (1, '2022-12-21', '2022-12-21', 2);

-- documents creation
INSERT INTO document (id, driver_id, name, picture_path)
    VALUES (1, 1, 'saobracajna', '');
INSERT INTO document (id, driver_id, name, picture_path)
    VALUES (2, 1, 'vozacka', '');
INSERT INTO document (id, driver_id, name, picture_path)
    VALUES (3, 1, 'licna', '');

-- work hour creation
INSERT INTO work_hour (id, driver_id, start_date, end_date)
    VALUES (1, 1, '2022-12-21', '2022-12-21');
