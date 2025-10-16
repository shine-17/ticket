-- member
CREATE SEQUENCE member_id_seq;
CREATE TABLE member (
    id int PRIMARY KEY DEFAULT nextval('member_id_seq'),
    login_id VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(200) NOT NULL
);

-- seat
CREATE SEQUENCE seat_id_seq;
CREATE TABLE seat (
    id int PRIMARY KEY DEFAULT nextval('seat_id_seq'),
    ZONE VARCHAR(100) NOT NULL,
    "row" int NOT NULL,
    number int NOT NULL
);

-- booking
CREATE SEQUENCE booking_id_seq;
CREATE TABLE booking (
    id int PRIMARY KEY DEFAULT nextval('booking_id_seq'),
    member_id int NOT NULL REFERENCES MEMBER(id),
    seat_id int NOT NULL REFERENCES seat(id)
);