-- Séquences ajustées pour éviter de dépasser 12 chiffres
CREATE SEQUENCE seq_bloc START 1 MAXVALUE 999999999999;
CREATE SEQUENCE seq_usuel START 1 MAXVALUE 999999999999;
CREATE SEQUENCE seq_transformation START 1 MAXVALUE 999999999999;

-- Création des tables avec préfixes et séquences
CREATE TABLE Bloc(
   id_bloc VARCHAR(15) DEFAULT 'BLO' || LPAD(nextval('seq_bloc')::TEXT, 12, '0'),
   longueur NUMERIC(15,5)  NOT NULL,
   largeur NUMERIC(15,5)   NOT NULL,
   hauteur NUMERIC(15,5)   NOT NULL,
   prix_production NUMERIC(15,2)   NOT NULL,
   is_decoupe BOOLEAN,
   id_bloc_decoupe VARCHAR(15) ,
   PRIMARY KEY(id_bloc),
   FOREIGN KEY(id_bloc_decoupe) REFERENCES Bloc(id_bloc)
);

CREATE TABLE Usuel(
   id_usuel VARCHAR(15) DEFAULT 'USU' || LPAD(nextval('seq_usuel')::TEXT, 12, '0'),
   longueur NUMERIC(15,5)NOT NULL,
   largeur NUMERIC(15,5) NOT NULL,
   hauteur NUMERIC(15,5) NOT NULL,
   prix_vente NUMERIC(15,2),
   nom_usuel VARCHAR(50), 
   PRIMARY KEY(id_usuel)
);

CREATE TABLE Transformation(
   id_transformation VARCHAR(15) DEFAULT 'TRA' || LPAD(nextval('seq_transformation')::TEXT, 12, '0'),
   quantite INTEGER,
   date_transformation TIMESTAMP,
   id_bloc VARCHAR(15)  NOT NULL,
   id_usuel VARCHAR(15)  NOT NULL,
   PRIMARY KEY(id_transformation),
   FOREIGN KEY(id_bloc) REFERENCES Bloc(id_bloc),
   FOREIGN KEY(id_usuel) REFERENCES Usuel(id_usuel)
);

INSERT INTO Bloc (longueur, largeur, hauteur, prix_production, is_decoupe, id_bloc_decoupe) 
VALUES(200, 300, 40, 1000.00, FALSE, NULL), (250, 300, 50, 1200.00, FALSE, NULL);

INSERT INTO Usuel (longueur, largeur, hauteur, prix_vente, nom_usuel)
VALUES (90, 190, 20, 500.00, 'Matelas Simple'),  
(140, 190, 20, 800.00, 'Matelas Double'),  
(180, 200, 25, 1200.00, 'Matelas King Size'),
(90, 190, 15, 450.00, 'Matelas Simple Epaisseur Reduite'),
(160, 200, 25, 1000.00, 'Matelas Queen Size');