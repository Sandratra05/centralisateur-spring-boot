CREATE TABLE client(
                       id_client SERIAL,
                       nom VARCHAR(50)  NOT NULL,
                       prenom VARCHAR(50) ,
                       date_naissance DATE,
                       adresse VARCHAR(50) ,
                       email VARCHAR(100) ,
                       contact VARCHAR(50) ,
                       PRIMARY KEY(id_client),
                       UNIQUE(email)
);

CREATE TABLE users(
                      id_users SERIAL,
                      identifiant INTEGER NOT NULL,
                      mot_de_passe VARCHAR(100)  NOT NULL,
                      role VARCHAR(50)  NOT NULL,
                      date_creation TIMESTAMP,
                      id_client INTEGER NOT NULL,
                      PRIMARY KEY(id_users),
                      FOREIGN KEY(id_client) REFERENCES client(id_client)
);