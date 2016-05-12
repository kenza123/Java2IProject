ALTER TABLE produit DROP CONSTRAINT P_Prod_Sup0;
ALTER TABLE produit DROP CONSTRAINT P_Haut_Sup0;
ALTER TABLE produit DROP CONSTRAINT P_Long_Sup0;
ALTER TABLE produit DROP CONSTRAINT P_NbEmpil_Sup0;
ALTER TABLE produit DROP CONSTRAINT P_SetUp_N;

ALTER TABLE commande DROP CONSTRAINT C_Stock_Sup0;
ALTER TABLE commande DROP CONSTRAINT C_Envoi_Sup0;
ALTER TABLE commande DROP CONSTRAINT C_Penalite_Sup0;

ALTER TABLE production DROP CONSTRAINT P_NbLigne_N;

ALTER TABLE box DROP CONSTRAINT B_Lbox_Sup0;
ALTER TABLE box DROP CONSTRAINT B_Hbox_Sup0;
ALTER TABLE box DROP CONSTRAINT B_Prix_Sup0;

ALTER TABLE produit_commande DROP CONSTRAINT PC_Unites_N;
ALTER TABLE produit_commande DROP CONSTRAINT PC_FK_Commande;
ALTER TABLE produit_commande DROP CONSTRAINT PC_FK_Produit;

DROP TABLE produit;
DROP TABLE commande;
DROP TABLE production;
DROP TABLE box;
DROP TABLE produit_commande;

CREATE TABLE produit (
    id   VARCHAR(55) PRIMARY KEY,
    t_setup INTEGER,
    t_production INTEGER,
    hauteur INTEGER,
    longueur INTEGER,
    nbEmpileMax INTEGER
);

ALTER TABLE produit ADD CONSTRAINT P_Prod_Sup0 CHECK (t_production > 0);
ALTER TABLE produit ADD CONSTRAINT P_Haut_Sup0 CHECK (hauteur > 0);
ALTER TABLE produit ADD CONSTRAINT P_Long_Sup0 CHECK (longueur > 0);
ALTER TABLE produit ADD CONSTRAINT P_NbEmpil_Sup0 CHECK (nbEmpileMax > 0);
ALTER TABLE produit ADD CONSTRAINT P_SetUp_N CHECK (nbEmpileMax >= 0);


CREATE TABLE commande (
    id   VARCHAR(55) PRIMARY KEY,
    stockMin INTEGER,
    dEnvoiPrevue INTEGER,
    penalite FLOAT
);


ALTER TABLE commande ADD CONSTRAINT C_Stock_Sup0 CHECK (stockMin > 0);
ALTER TABLE commande ADD CONSTRAINT C_Envoi_Sup0 CHECK (dEnvoiPrevue > 0);
ALTER TABLE commande ADD CONSTRAINT C_Penalite_Sup0 CHECK (penalite > 0);

CREATE TABLE production (
    nbLignes INTEGER
);

ALTER TABLE production ADD CONSTRAINT P_NbLigne_N CHECK (nbLignes >= 0);

CREATE TABLE box (
    id   VARCHAR(55) PRIMARY KEY,
    Lbox INTEGER NOT NULL,
    Hbox INTEGER NOT NULL,
    prixBox FLOAT NOT NULL
);


ALTER TABLE box ADD CONSTRAINT B_Lbox_Sup0 CHECK (Lbox > 0);
ALTER TABLE box ADD CONSTRAINT B_Hbox_Sup0 CHECK (Hbox > 0);
ALTER TABLE box ADD CONSTRAINT B_Prix_Sup0 CHECK (prixBox >= 0);

CREATE TABLE produit_commande (
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_commande VARCHAR(55) NOT NULL,
    id_produit VARCHAR(55)NOT NULL,
    nb_unites INTEGER
);

ALTER TABLE produit_commande ADD CONSTRAINT PC_Unites_N CHECK (nb_unites >= 0);
ALTER TABLE produit_commande ADD CONSTRAINT PC_FK_Commande FOREIGN KEY(id_commande) REFERENCES commande(id);
ALTER TABLE produit_commande ADD CONSTRAINT PC_FK_Produit FOREIGN KEY(id_produit) REFERENCES produit(id);
