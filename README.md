# Oracle Database Setup

Ce projet configure une instance Oracle Database utilisant Docker.

## Prérequis

- Docker
- Docker Compose

## Configuration

1. Créez un fichier `docker-compose.yml` avec le contenu suivant :

```yaml
version: '3'
services:
  db:
    image: container-registry.oracle.com/database/free:latest
    ports:
      - "1521:1521"
    environment:
      - ORACLE_PWD=password123
    volumes:
      - ./oradata:/opt/oracle/oradata
```

## Commandes

### Démarrer la base de données

```bash
docker-compose up -d
```

### Vérifier le statut

```bash
docker-compose ps
```

### Voir les logs

```bash
docker-compose logs -f
```

### Arrêter la base de données

```bash
docker-compose down
```

### Se connecter à la base de données

1. Entrez dans le conteneur :
   ```bash
   docker exec -it $(docker-compose ps -q db) bash
   ```

2. Connectez-vous en superadmin :
   ```bash
   cd $ORACLE_HOME/bin
   ```
   ```bash
   ./sqlplus / as sysdba
   ```
3. Créer un utilisateur  :

   ```sql
   CREATE USER c##airfly IDENTIFIED BY airfly;
   GRANT CONNECT, RESOURCE TO c##airfly;
   GRANT CREATE SESSION TO c##airfly;
   GRANT UNLIMITED TABLESPACE TO c##airfly;
   ```
### Créer une table exemple

```sql
-- Création de la séquence pour les IDs auto-incrémentés
CREATE SEQUENCE global_seq START WITH 1 INCREMENT BY 1;

-- Création de la table AIRCRAFT
CREATE TABLE AIRCRAFT (
                         id NUMBER DEFAULT global_seq.NEXTVAL PRIMARY KEY,
                         aircraft_type VARCHAR2(100) NOT NULL,
                         capacity NUMBER NOT NULL,
                         manufacturer VARCHAR2(100)
);

-- Creation of the PASSENGER table
CREATE TABLE PASSENGER (
                          id NUMBER DEFAULT global_seq.NEXTVAL PRIMARY KEY,
                          first_name VARCHAR2(100) NOT NULL,
                          last_name VARCHAR2(100) NOT NULL,
                          email VARCHAR2(100) NOT NULL,
                          phone_number VARCHAR2(20),
                          birthday_date DATE
);

-- Creation of the FLIGHT table
CREATE TABLE FLIGHT (
                       id NUMBER DEFAULT global_seq.NEXTVAL PRIMARY KEY,
                       departure_airport VARCHAR2(100) NOT NULL,
                       arrival_airport VARCHAR2(100) NOT NULL,
                       departure_time TIMESTAMP NOT NULL,
                       arrival_time TIMESTAMP NOT NULL,
                       flight_number VARCHAR2(20) NOT NULL,
                       airline VARCHAR2(100) NOT NULL,
                       aircraft_id NUMBER,
                       CONSTRAINT fk_flight_aircraft FOREIGN KEY (aircraft_id) REFERENCES AIRCRAFT(id)
);

-- Creation of the BOOKING table
CREATE TABLE BOOKING (
                        id INT PRIMARY KEY,
                        flight_id INT,
                        passenger_id INT,
                        booking_date TIMESTAMP,
                        FOREIGN KEY (flight_id) REFERENCES FLIGHT(id),
                        FOREIGN KEY (passenger_id) REFERENCES PASSENGER(id)
);

INSERT INTO AIRCRAFT (id, aircraft_type, capacity, manufacturer)
VALUES (1, 'A320', 180, 'Airbus'),
       (2, 'B737', 150, 'Boeing'),
       (3, 'A330', 250, 'Airbus');

INSERT INTO PASSENGER (id, first_name, last_name, email, phone_number, birthday_date)
VALUES (1, 'John', 'Doe', 'john.doe@example.com', '+1234567890', TO_DATE('1990-01-01', 'YYYY-MM-DD')),
       (2, 'Jane', 'Smith', 'jane.smith@example.com', '+1234567891', TO_DATE('1992-02-02', 'YYYY-MM-DD')),
       (3, 'Alice', 'Johnson', 'alice.johnson@example.com', '+1234567892', TO_DATE('2000-03-03', 'YYYY-MM-DD')),
       (4, 'Bob', 'Brown', 'bob.brown@example.com', '+1234567893', TO_DATE('2010-04-04', 'YYYY-MM-DD')),
       (5, 'Charlie', 'Davis', 'charlie.davis@example.com', '+1234567894', TO_DATE('2023-05-05', 'YYYY-MM-DD'));

INSERT INTO FLIGHT (departure_airport, arrival_airport, departure_time, arrival_time, flight_number, airline, aircraft_id)
VALUES ('JFK', 'LAX', TIMESTAMP '2024-10-15 10:00:00', TIMESTAMP '2024-10-15 13:00:00', 'FL123', 'Example Airlines', 1),
       ('LAX', 'ORD', TIMESTAMP '2024-10-16 14:00:00', TIMESTAMP '2024-10-16 20:00:00', 'FL456', 'Another Airlines', 2),
       ('ORD', 'DFW', TIMESTAMP '2024-10-17 09:00:00', TIMESTAMP '2024-10-17 11:30:00', 'FL789', 'Third Airlines', 1),
       ('DFW', 'SEA', TIMESTAMP '2024-10-20 15:00:00', TIMESTAMP '2024-10-20 18:00:00', 'FL101', 'Fourth Airlines', 3),
       ('SEA', 'MIA', TIMESTAMP '2024-10-20 12:00:00', TIMESTAMP '2024-10-20 20:00:00', 'FL202', 'Fifth Airlines', 2);

INSERT INTO BOOKING (id, flight_id, passenger_id, booking_date)
VALUES (1, 1, 1, TIMESTAMP '2024-10-01 10:00:00'),
       (2, 1, 2, TIMESTAMP '2024-10-02 11:00:00'),
       (3, 2, 3, TIMESTAMP '2024-10-03 12:00:00'),
       (4, 3, 4, TIMESTAMP '2024-10-04 13:00:00'),
       (5, 4, 5, TIMESTAMP '2024-10-05 14:00:00');

COMMIT;
```

### Vérifier les données

```sql
SELECT * FROM FLIGHT;
```

## Informations de connexion

- URL JDBC : `jdbc:oracle:thin:@localhost:1521/FREEPDB1`
- Utilisateur : `airfly`
- Mot de passe : `airfly`

Note : La première initialisation de la base de données peut prendre plusieurs minutes.
