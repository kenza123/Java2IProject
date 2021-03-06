/*
DB Parameters :
    DB_Name : Java_2I_Project
    Username : Java_2I_Project
    Password : Java_2I_Project
*/

ALTER TABLE type_produit DROP CONSTRAINT P_Prod_Sup0;
ALTER TABLE type_produit DROP CONSTRAINT P_Haut_Sup0;
ALTER TABLE type_produit DROP CONSTRAINT P_Long_Sup0;
ALTER TABLE type_produit DROP CONSTRAINT P_NbEmpil_Sup0;
ALTER TABLE type_produit DROP CONSTRAINT P_SetUp_N;

ALTER TABLE commande DROP CONSTRAINT C_Stock_Sup0;
ALTER TABLE commande DROP CONSTRAINT C_Envoi_Sup0;
ALTER TABLE commande DROP CONSTRAINT C_Penalite_Sup0;

ALTER TABLE produit_commande DROP CONSTRAINT PC_Unites_N;
ALTER TABLE produit_commande DROP CONSTRAINT PC_FK_Commande;
ALTER TABLE produit_commande DROP CONSTRAINT PC_FK_Produit;

ALTER TABLE type_box DROP CONSTRAINT B_Lbox_Sup0;
ALTER TABLE type_box DROP CONSTRAINT B_Hbox_Sup0;
ALTER TABLE type_box DROP CONSTRAINT B_Prix_Sup0;

ALTER TABLE box_achete DROP CONSTRAINT BA_FK_Type_box;
ALTER TABLE box_achete DROP CONSTRAINT P_libre_boolean;

ALTER TABLE produit DROP CONSTRAINT P_FK_P_C;
ALTER TABLE produit DROP CONSTRAINT P_FK_pile;
ALTER TABLE produit DROP CONSTRAINT P_FK_LP;

ALTER TABLE pile DROP CONSTRAINT P_LongPile;
ALTER TABLE pile DROP CONSTRAINT P_LargPile;
ALTER TABLE pile DROP CONSTRAINT P_FK_BoxA;

DROP TABLE type_produit;
DROP TABLE commande;
DROP TABLE produit_commande;
DROP TABLE type_box;
DROP TABLE box_achete;
DROP TABLE ligne_production;
DROP TABLE produit;
DROP TABLE pile;



CREATE TABLE type_produit (
    id   VARCHAR(55) PRIMARY KEY,
    t_setup INTEGER,
    t_production INTEGER,
    color VARCHAR(45),
    hauteur INTEGER,
    longueur INTEGER,
    nbEmpileMax INTEGER
);

CREATE TABLE commande (
    id   VARCHAR(55) PRIMARY KEY,
    stockMin INTEGER,
    dEnvoiPrevue INTEGER,
    dEnvoiReel INTEGER,
    penalite FLOAT
);

CREATE TABLE produit_commande (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_commande VARCHAR(55) NOT NULL,
    id_type_produit VARCHAR(55)NOT NULL,
    nb_unites INTEGER
);

CREATE TABLE type_box (
    id   VARCHAR(55) PRIMARY KEY,
    Lbox INTEGER NOT NULL,
    Hbox INTEGER NOT NULL,
    prixBox FLOAT NOT NULL
);

CREATE TABLE box_achete (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_type_box VARCHAR(55) NOT NULL,
    num_box INTEGER NOT NULL,
    d_libre INTEGER NOT NULL,
    libre INTEGER
);

CREATE TABLE pile (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_box_achete INTEGER,
    longueur_pile INTEGER,
    largeur_pile INTEGER
);

CREATE TABLE produit (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_produit_commande INTEGER NOT NULL,
    id_pile INTEGER,
    date_debut_prod INTEGER,
    nbLignes INTEGER
);

CREATE TABLE ligne_production (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nbLignes INTEGER NOT NULL
);



ALTER TABLE type_produit ADD CONSTRAINT P_Prod_Sup0 CHECK (t_production > 0);
ALTER TABLE type_produit ADD CONSTRAINT P_Haut_Sup0 CHECK (hauteur > 0);
ALTER TABLE type_produit ADD CONSTRAINT P_Long_Sup0 CHECK (longueur > 0);
ALTER TABLE type_produit ADD CONSTRAINT P_NbEmpil_Sup0 CHECK (nbEmpileMax > 0);
ALTER TABLE type_produit ADD CONSTRAINT P_SetUp_N CHECK (nbEmpileMax >= 0);

ALTER TABLE type_box ADD CONSTRAINT B_Lbox_Sup0 CHECK (Lbox > 0);
ALTER TABLE type_box ADD CONSTRAINT B_Hbox_Sup0 CHECK (Hbox > 0);
ALTER TABLE type_box ADD CONSTRAINT B_Prix_Sup0 CHECK (prixBox >= 0);

ALTER TABLE commande ADD CONSTRAINT C_Stock_Sup0 CHECK (stockMin > 0);
ALTER TABLE commande ADD CONSTRAINT C_Envoi_Sup0 CHECK (dEnvoiPrevue > 0);
ALTER TABLE commande ADD CONSTRAINT C_Penalite_Sup0 CHECK (penalite > 0);

ALTER TABLE produit_commande ADD CONSTRAINT PC_Unites_N CHECK (nb_unites >= 0);
ALTER TABLE produit_commande ADD CONSTRAINT PC_FK_Commande FOREIGN KEY(id_commande) REFERENCES commande(id);
ALTER TABLE produit_commande ADD CONSTRAINT PC_FK_Produit FOREIGN KEY(id_type_produit) REFERENCES type_produit(id);

ALTER TABLE box_achete ADD CONSTRAINT BA_FK_Type_box FOREIGN KEY(id_type_box) REFERENCES type_box(id);
ALTER TABLE box_achete ADD CONSTRAINT P_libre_boolean CHECK (libre = 0 OR libre = 1);

ALTER TABLE pile ADD CONSTRAINT P_FK_BoxA FOREIGN KEY(id_box_achete) REFERENCES box_achete(id);
ALTER TABLE pile ADD CONSTRAINT P_LargPile CHECK (largeur_pile >= 0);
ALTER TABLE pile ADD CONSTRAINT P_LongPile CHECK (longueur_pile >= 0);

ALTER TABLE produit ADD CONSTRAINT P_FK_P_C FOREIGN KEY(id_produit_commande) REFERENCES produit_commande(id);
ALTER TABLE produit ADD CONSTRAINT P_FK_pile FOREIGN KEY(id_pile) REFERENCES pile(id);
ALTER TABLE produit ADD CONSTRAINT P_FK_LP FOREIGN KEY(nbLignes) REFERENCES ligne_production(id);
