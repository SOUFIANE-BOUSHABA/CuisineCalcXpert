-- Create the database
CREATE DATABASE bati_cuisine_db;


CREATE TABLE client (
                        id serial PRIMARY KEY,
                        nom varchar(100) NOT NULL,
                        adresse varchar(255) NOT NULL,
                        telephone varchar(15),
                        estprofessionnel boolean NOT NULL,
                        remise double precision DEFAULT 0.0
);

CREATE TABLE project (
                         id serial PRIMARY KEY,
                         nom_projet varchar(255) NOT NULL,
                         marge_beneficiaire double precision NOT NULL,
                         cout_total double precision NOT NULL,
                         etat_projet varchar(20) NOT NULL
                             CONSTRAINT project_etat_projet_check CHECK (etat_projet IN ('EN_COURS', 'TERMINE', 'ANNULE')),
                         client_id integer REFERENCES client
);


CREATE TABLE composants (
                            id serial PRIMARY KEY,
                            nom varchar(100) NOT NULL,
                            type_composant varchar(20) CHECK (type_composant IN ('Matériel', 'Workforce')),
                            taux_tva double precision,
                            projet_id integer REFERENCES project
);


CREATE TABLE devis (
                       id serial PRIMARY KEY,
                       montant_estime double precision,
                       date_emission date,
                       date_validite date,
                       accepte boolean,
                       projet_id integer REFERENCES project
);


CREATE TABLE materiaux (
                           cout_transport double precision,
                           coefficient_qualite double precision,
                           cout_unitaire double precision,
                           quantite double precision
) INHERITS (composants);

CREATE TABLE workforce (
                           taux_horaire double precision,
                           heures_travail double precision,
                           productivite_ouvrier double precision
) INHERITS (composants);

