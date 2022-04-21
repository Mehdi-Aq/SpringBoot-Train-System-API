
-- Create tables

CREATE TABLE Users (
	UserID				INT,
	FirstName			VARCHAR(30)		NOT NULL,
	LastName			VARCHAR(30)		NOT NULL,
	Email				VARCHAR(100)	NOT NULL	UNIQUE,
	Password			VARCHAR(30)		NOT NULL,
	Subscription		CHAR(3)			NOT NULL,
	PostalCode			CHAR(6)			NOT NULL,
	Province			VARCHAR(50)		NOT NULL,
	Country				VARCHAR(50)		NOT NULL,
	PhoneNumber			VARCHAR(12)		NOT NULL,
	CONSTRAINT UserID_PK PRIMARY KEY (UserID),
	CONSTRAINT chk_Sub CHECK (Subscription IN ('Yes', 'No'))
	);

CREATE TABLE Receipts (
	ConfirmationNb		CHAR(6),
	UserID				INT,
	TotalFare			DECIMAL(9,2)	NOT NULL	DEFAULT 0,
	PaymentMethod		VARCHAR(30)		NOT NULL,
	ReceiptDate			DATE 			NOT NULL,
	CONSTRAINT ConfirmationNb_PK PRIMARY KEY (ConfirmationNb),
	CONSTRAINT UserID_FK FOREIGN KEY (UserID) REFERENCES Users(UserID),
	CONSTRAINT chk_PaymentMethod CHECK (PaymentMethod IN ('Cash', 'Interac', 'Visa', 'MasterCard', 'American Express'))
	);

CREATE TABLE Passengers (
	PassengerID			INT,
	FirstName			VARCHAR(30)		NOT NULL,
	LastName			VARCHAR(30)		NOT NULL,
	AgeCategory			CHAR(5)			NOT NULL,
	CONSTRAINT PassengerID_PK PRIMARY KEY (PassengerID),
	CONSTRAINT chk_Passengers_AgeCat CHECK (AgeCategory IN ('Child', 'Adult'))
	);

CREATE TABLE Routes (
	RouteID				CHAR(2),			
	DepartureCity		VARCHAR(40)		NOT NULL,
	ArrivalCity			VARCHAR(40)		NOT NULL,
	CONSTRAINT RouteID_PK PRIMARY KEY (RouteID)
	);

CREATE TABLE Trains (
	TrainNb				DECIMAL(4),
	Carrier				CHAR(8)			NOT NULL		DEFAULT "Via Rail",
	Capacity			DECIMAL(3)		NOT NULL,
	CONSTRAINT TrainNb_PK PRIMARY KEY (TrainNb)
	);

CREATE TABLE Trips (
	TripNb				CHAR(10),
	RouteID				CHAR(2),
	TrainNb				Decimal(4),
	TripDate			DATE  			NOT NULL,
	DepartureTime		TIME 			NOT NULL,
	ArrivalTime			TIME  			NOT NULL,
	Duration			TIME  			GENERATED ALWAYS AS (TIMEDIFF(ArrivalTime,DepartureTime)),		
	CONSTRAINT TripNb_PK PRIMARY KEY (TripNb),
	CONSTRAINT RouteID_FK FOREIGN KEY (RouteID) REFERENCES Routes(RouteID),
	CONSTRAINT TrainNb_FK FOREIGN KEY (TrainNb) REFERENCES Trains(TrainNb)
	);

CREATE TABLE Tickets (
	TicketNb			INT, 			
	ConfirmationNb		CHAR(6),			
	PassengerID			INT,		
	TripNb				CHAR(10),		
	Class				CHAR(8)				NOT NULL,
	Car 				DECIMAL(2),		
	SeatNb				DECIMAL(3),
	Fare				DECIMAL(9,2)	NOT NULL	DEFAULT 0,
	CONSTRAINT TicketNb_PK PRIMARY KEY (TicketNb),
	CONSTRAINT ConfirmationNb_FK FOREIGN KEY (ConfirmationNb) REFERENCES Receipts(ConfirmationNb),
	CONSTRAINT PassengerID_FK FOREIGN KEY (PassengerID) REFERENCES Passengers(PassengerID),
	CONSTRAINT TripNb_Tickets_FK FOREIGN KEY (TripNb) REFERENCES Trips(TripNb),
	CONSTRAINT chk_Tickets_Class CHECK (Class IN ('Economy', 'Business'))
	);

CREATE TABLE AvailableSeats (
	TripNb				CHAR(10),
	Class				CHAR(8)			NOT NULL,
	NbOfAvailableSeats	DECIMAL(3)		NOT NULL,
	CONSTRAINT TripNb_Class_PK PRIMARY KEY (TripNb, Class),
	CONSTRAINT TripNb_AS_FK FOREIGN KEY (TripNb) REFERENCES Trips(TripNb),
	CONSTRAINT chk_AS_Class CHECK (Class IN ('Economy', 'Business'))
	);

CREATE TABLE Fare (
	RouteID				CHAR(2),
	Class				CHAR(8)			NOT NULL,
	AgeCategory			CHAR(5)			NOT NULL,
	Price				DECIMAL(9,2)	NOT NULL,
	CONSTRAINT RouteID_Class_AgeC_PK PRIMARY KEY (RouteID, Class, AgeCategory),
	CONSTRAINT RouteID_Fare_FK FOREIGN KEY (RouteID) REFERENCES Routes(RouteID),
	CONSTRAINT chk_Fare_AgeCat CHECK (AgeCategory IN ('Child', 'Adult')),
	CONSTRAINT chk_Fare_Class CHECK (Class IN ('Economy', 'Business'))
	);


-- Create Views

-- View that displays Mehdi's boarding pass for Trip# MT12162108
CREATE VIEW MehdiBoardingPass AS 
SELECT Tkt.TicketNb, P.FirstName, P.LastName, P.AgeCategory,  Trp.TripDate,
R.DepartureCity, Trp.DepartureTime, R.ArrivalCity, Trp.ArrivalTime,
Trp.TrainNb, Trn.Carrier, Tkt.Class, Tkt.Car, Tkt.SeatNb, Tkt.ConfirmationNb
FROM Tickets Tkt
JOIN Passengers P ON P.PassengerID = Tkt.PassengerID
JOIN Trips Trp ON Trp.TripNb = Tkt.TripNb
JOIN Routes R ON R.RouteID = Trp.RouteID
JOIN Trains Trn ON Trn.TrainNb = Trp.TrainNb
WHERE P.FirstName = 'Mehdi' AND P.LastName = 'Aqdim' AND Trp.TripNb = 'MT12162108';

-- View that displays David's boarding pass for Trip# MT12162108
CREATE VIEW DavidBoardingPass AS
Select TicketNb, p.FirstName, p.LastName, p.AgeCategory, trn.Carrier, trp.TrainNb, t.Class, t.Car, t.SeatNb, 
trp.TripDate, r.DepartureCity, trp.DepartureTime, r.ArrivalCity, trp.ArrivalTime, t.ConfirmationNb
From Tickets t join Passengers p on t.PassengerID = p.PassengerID
join Trips trp on t.TripNb = trp.TripNb
join Routes r on trp.RouteID = r.RouteID
join Trains trn on trp.TrainNb = trn.TrainNb
where p.FirstName = 'David' and p.LastName = 'Krushnisky' AND trp.TripNb = 'MT12162108';

-- View that lists all purchases made by Sina
CREATE VIEW SinaReceipts AS
SELECT R.ConfirmationNb, U.FirstName, U.LastName, R.TotalFare, R.PaymentMethod, R.ReceiptDate
FROM Receipts R
JOIN Users U on U.UserID = R.UserID
WHERE U.FirstName = 'Sina' AND U.LastName = 'Kooshesh';

-- View that displays users who never made a purchase
CREATE VIEW UsersWithNoReceipt AS
Select u.UserID, FirstName, LastName, Email, PostalCode, Province, Country, PhoneNumber
From Users u left join Receipts r on u.UserID = r.UserID
where r.ConfirmationNb is null;

-- View that lists adults' fare for all routes
CREATE VIEW AdultFareForAllRoutes AS
SELECT R.DepartureCity, R.ArrivalCity, F.Class, F.Price
FROM Routes R
JOIN Fare F ON F.RouteID = R.RouteID
WHERE F.AgeCategory = 'Adult';

-- View that lists all passengers on Trip# MT12162108
CREATE VIEW MT12162108_PassengersList AS
SELECT CONCAT(P.LastName,', ',P.FirstName) AS FullName, P.AgeCategory, T.Class, T.Car, T.SeatNb
FROM Tickets T 
JOIN Passengers P ON T.PassengerID=P.PassengerID
WHERE T.TripNb = 'MT12162108'
ORDER BY SeatNb;

-- View that lists the number of available seats per train leaving from Montreal
CREATE VIEW NbOfAvailableSeatsFromMontreal AS
SELECT T.TripNb, R.DepartureCity, R.ArrivalCity, T.TripDate,
T.DepartureTime, A.Class, A.NbOfAvailableSeats
FROM AvailableSeats A
JOIN Trips T ON T.TripNb = A.TripNb
JOIN Routes R ON R.RouteID = T.RouteID
WHERE R.DepartureCity = 'Montreal'
ORDER BY T.TripDate, T.DepartureTime;


-- Add necessary triggers

/*This trigger get executed before every insert to:
  1. Verify if there are available seats in the chosen trip and class
  2. If not, abort the insert and display an error message
  3. If seats are available, Decrement the number of available seats
  4. Fetch the fare for the trip and class and update it in the price column
  5. Add the fare to the TotalFare column in the Receipts table
*/
DELIMITER $$
CREATE TRIGGER Tickets_BEFORE_INSERT
BEFORE INSERT ON Tickets FOR EACH ROW
BEGIN
IF (SELECT NbOfAvailableSeats FROM AvailableSeats
	WHERE TripNb = NEW.TripNb AND Class = NEW.Class) = 0
	THEN SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'No More
	 Available seats in this train for the selected class !';
ELSE
	UPDATE AvailableSeats
	SET NbOfAvailableSeats = NbOfAvailableSeats - 1
	WHERE TripNb = NEW.TripNb AND Class = NEW.Class;
	SET NEW.Fare = (SELECT Price FROM Fare F 
	                JOIN Trips T ON T.RouteID = F.RouteID
									WHERE T.TripNb = NEW.TripNb AND F.Class = NEW.Class AND 
	                F.AgeCategory = (SELECT AgeCategory FROM Passengers 
									 WHERE PassengerID = NEW.PassengerID)
	                );
	UPDATE Receipts
	SET TotalFare = TotalFare + NEW.Fare
	WHERE ConfirmationNb = NEW.ConfirmationNb;
END IF;                
END$$
DELIMITER ;