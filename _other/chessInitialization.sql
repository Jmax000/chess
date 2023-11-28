DROP DATABASE chess;
CREATE DATABASE chess;
use chess;

CREATE TABLE authtokens (
    token varchar(100) NOT NULL PRIMARY KEY,
    username varchar(100) NOT NULL
);

CREATE TABLE users (
    username varchar(100) NOT NULL PRIMARY KEY,
    password varchar(100) NOT NULL,
    email varchar(100) NOT NULL
);

CREATE TABLE games (
    gameID int AUTO_INCREMENT PRIMARY KEY,
    whiteUsername TEXT,
    blackUsername TEXT,
    gameName TEXT NOT NULL,
    game TEXT NOT NULL
);
