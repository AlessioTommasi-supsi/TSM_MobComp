-- Popolamento della tabella User
INSERT INTO `User` (`id`, `fk_finger`, `name`, `password`)
VALUES
(1, 1, 'Alice', 'password123'),
(2, 2, 'Bob', 'qwerty456'),
(3, 3, 'Charlie', 'letmein789'),
(4, 4, 'Diana', 'hunter2'),
(5, 5, 'Eve', 'passw0rd'),
(6, 6, 'Frank', '1234abcd'),
(7, 7, 'Grace', 'abcdefg'),
(8, 8, 'Hank', 'zxcvbnm'),
(9, 9, 'Ivy', 'password321'),
(10, 10, 'Jack', '9876pass');

-- Popolamento della tabella Finger
INSERT INTO `Finger` (`id`, `fk_user`, `value`)
VALUES
(1, 1, 'fingerprint_data_1'),
(2, 2, 'fingerprint_data_2'),
(3, 3, 'fingerprint_data_3'),
(4, 4, 'fingerprint_data_4'),
(5, 5, 'fingerprint_data_5'),
(6, 6, 'fingerprint_data_6'),
(7, 7, 'fingerprint_data_7'),
(8, 8, 'fingerprint_data_8'),
(9, 9, 'fingerprint_data_9'),
(10, 10, 'fingerprint_data_10');

-- Popolamento della tabella Log
INSERT INTO `Log` (`id`, `state`, `date`, `fk_user`, `fk_finger`, `device`)
VALUES
(1, 0, '2024-11-25 10:00:00', 1, 1, 'Device_A'),
(2, 1, '2024-11-25 11:00:00', 2, 2, 'Device_B'),
(3, 1, '2024-11-25 12:00:00', 3, 3, 'Device_C'),
(4, 0, '2024-11-25 13:00:00', 4, 4, 'Device_D'),
(5, 1, '2024-11-25 14:00:00', 5, 5, 'Device_E'),
(6, 1, '2024-11-25 15:00:00', 6, 6, 'Device_F'),
(7, 0, '2024-11-25 16:00:00', 7, 7, 'Device_G'),
(8, 0, '2024-11-25 17:00:00', 8, 8, 'Device_H'),
(9, 1, '2024-11-25 18:00:00', 9, 9, 'Device_I'),
(10, 0, '2024-11-25 19:00:00', 10, 10, 'Device_J'),
(11, 1, '2024-11-25 20:00:00', 1, 1, 'Device_K'),
(12, 0, '2024-11-25 21:00:00', 2, 2, 'Device_L'),
(13, 1, '2024-11-25 22:00:00', 3, 3, 'Device_M'),
(14, 0, '2024-11-25 23:00:00', 4, 4, 'Device_N'),
(15, 1, '2024-11-26 00:00:00', 5, 5, 'Device_O'),
(16, 0, '2024-11-26 01:00:00', 6, 6, 'Device_P'),
(17, 1, '2024-11-26 02:00:00', 7, 7, 'Device_Q'),
(18, 0, '2024-11-26 03:00:00', 8, 8, 'Device_R'),
(19, 1, '2024-11-26 04:00:00', 9, 9, 'Device_S'),
(20, 0, '2024-11-26 05:00:00', 10, 10, 'Device_T');
