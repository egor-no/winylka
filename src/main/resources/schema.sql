CREATE TABLE ARTIST(
  ARTIST_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  NAME VARCHAR(36) NOT NULL,
  COUNTRY CHAR(2),
  BIO VARCHAR(256)
);

CREATE TABLE ALBUM(
  ALBUM_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  TITLE VARCHAR(64),
  ARTIST_ID BIGINT,
  YEAR INT,
  INFO VARCHAR(1000)
);

CREATE TABLE RELEASE(
  RELEASE_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  ALBUM_ID BIGINT NOT NULL,
  RELEASE_DATE DATE,
  FORMAT VARCHAR (5),
  NOTES VARCHAR (20),
  LABEL VARCHAR(64),
  PRICE INT,
  IMG VARCHAR(100)
);

CREATE TABLE ORDER_INFO(
    ORDER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    CLIENT_ID BIGINT NOT NULL,
    STATUS VARCHAR(16) NOT NULL
);

CREATE TABLE ORDER_ITEMS(
    ORDER_ID BIGINT,
    RELEASE_ID BIGINT,
    QUANTITY INT,
    PRIMARY KEY(ORDER_ID, RELEASE_ID),
    STATUS VARCHAR(16) NOT NULL
);

CREATE TABLE CLIENT(
    CLIENT_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    FIRST_NAME VARCHAR(20),
    LAST_NAME VARCHAR(20),
    ADDRESS VARCHAR(100)
);

CREATE TABLE USERS(
    USERNAME varchar_ignorecase(50) not null primary key,
    PASSWORD varchar_ignorecase(500) not null,
    ENABLED  boolean not null
);

CREATE TABLE AUTHORITIES (
    USERNAME  varchar_ignorecase(50) not null,
    AUTHORITY varchar_ignorecase(50) not null,
    constraint fk_authorities_users foreign key (username) references users (username)
);

CREATE UNIQUE INDEX ix_auth_username on AUTHORITIES (username, authority);

ALTER TABLE RELEASE ADD FOREIGN KEY (ALBUM_ID) REFERENCES ALBUM(ALBUM_ID);
ALTER TABLE ALBUM ADD FOREIGN KEY (ARTIST_ID) REFERENCES ARTIST(ARTIST_ID);
ALTER TABLE ORDER_ITEMS ADD FOREIGN KEY (ORDER_ID) REFERENCES ORDER_INFO(ORDER_ID);
ALTER TABLE ORDER_ITEMS ADD FOREIGN KEY (RELEASE_ID) REFERENCES RELEASE(RELEASE_ID);
ALTER TABLE ORDER_INFO ADD FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT(CLIENT_ID);