-- Hotels
INSERT INTO hotel (name, address, star_rating, created_at, updated_at) VALUES
                                                                           ('Sunset Hotel', 'Downtown Ave 123', 4, now(), now()),
                                                                           ('Mountain View Resort', 'Hilltop Blvd 456', 5, now(), now()),
                                                                           ('City Lights Inn', 'Urban St 789', 3, now(), now()),
                                                                           ('Lakeside Retreat', 'Lakeview Rd 321', 4, now(), now()),
                                                                           ('Desert Oasis Hotel', 'Sahara Lane 99', 5, now(), now());

-- Rooms
INSERT INTO room (hotel_id, room_number, capacity, price_per_night, created_at, updated_at) VALUES
                                                                                                (1, '101', 2, 750.00, now(), now()),
                                                                                                (1, '102', 4, 1200.00, now(), now()),
                                                                                                (2, '201', 2, 950.00, now(), now()),
                                                                                                (2, '202', 3, 1100.00, now(), now()),
                                                                                                (3, '301', 1, 500.00, now(), now()),
                                                                                                (3, '302', 2, 650.00, now(), now()),
                                                                                                (4, '401', 2, 700.00, now(), now()),
                                                                                                (4, '402', 4, 1350.00, now(), now()),
                                                                                                (5, '501', 3, 1000.00, now(), now()),
                                                                                                (5, '502', 2, 900.00, now(), now());
