USE master
GO

drop database MovieDB
GO

CREATE DATABASE MovieDB
GO

USE MovieDB
GO

--TABLES
CREATE TABLE Account (
    Username nvarchar(20) unique NOT NULL,
    Password nvarchar(20) NOT NULL,
	Admin bit NOT NULL
)
GO

CREATE TABLE Person (
	IDPerson int IDENTITY PRIMARY KEY,
    FirstName nvarchar(20),
    LastName nvarchar(20),
	CONSTRAINT UC_Person UNIQUE (FirstName, LastName)
)
GO

CREATE TABLE Movie (
	IDMovie int IDENTITY PRIMARY KEY,
    Title nvarchar(50) NOT NULL,
    PublishedDate datetime NOT NULL,
    Description nvarchar(4000) NOT NULL,
    OrgTitle nvarchar(50) NOT NULL,
    Duration int NOT NULL,
    Genres nvarchar(200) NOT NULL,
    PicturePath nvarchar(500) NOT NULL
)
GO

CREATE TABLE Actor (
	PersonID int FOREIGN KEY REFERENCES Person(IDPerson) NOT NULL,
	MovieID int FOREIGN KEY REFERENCES Movie(IDMovie) NOT NULL
)
GO

CREATE TABLE Director (
	PersonID int FOREIGN KEY REFERENCES Person(IDPerson) NOT NULL,
	MovieID int FOREIGN KEY REFERENCES Movie(IDMovie) NOT NULL
)
GO

--PROCEDURES

--MOVIE PROCEDURES
CREATE proc createMovie
@Title nvarchar(50),
@PublishedDate datetime,
@Description nvarchar(4000),
@OrgTitle nvarchar(50),
@Duration int,
@Genres nvarchar(200),
@PicturePath nvarchar(500),
@IDMovie int output
as
INSERT into Movie(Title, PublishedDate, Description, OrgTitle, Duration, Genres, PicturePath)
values(@Title, @PublishedDate, @Description, @OrgTitle, @Duration, @Genres, @PicturePath)
SET @IDMovie = scope_identity()
GO

CREATE proc selectMovies
as
SELECT IDMovie, Title, CONVERT(nvarchar,PublishedDate, 126) as 'PublishedDate', Description, OrgTitle, Duration, Genres, PicturePath from Movie
GO

CREATE proc selectMovie
@ID int
as
SELECT IDMovie, Title, CONVERT(nvarchar,PublishedDate, 126)as 'PublishedDate', Description, OrgTitle, Duration, Genres, PicturePath from Movie
where Movie.IDMovie = @ID
GO

CREATE proc updateMovie
@ID int,
@Title nvarchar(50),
@PublishedDate datetime,
@Description nvarchar(4000),
@OrgTitle nvarchar(50),
@Duration int,
@Genres nvarchar(200),
@PicturePath nvarchar(500)
as
UPDATE Movie
SET Movie.Title = @Title, Movie.PublishedDate = @PublishedDate, Movie.Description = @Description, Movie.OrgTitle = @OrgTitle, Movie.Duration = @Duration, Movie.Genres = @Genres, Movie.PicturePath = @PicturePath
where Movie.IDMovie = @ID
GO

CREATE proc deleteMovie
@ID int
as
DELETE from Actor
where Actor.MovieID = @ID
DELETE from Director
where Director.MovieID = @ID
DELETE from Movie
where Movie.IDMovie = @ID
GO

CREATE proc deleteMovies
as
DELETE from Actor
DELETE from Director
DELETE from Person
DELETE from Movie
GO

--PERSON PROCEDURES
CREATE proc createPerson
@FirstName nvarchar(20),
@LastName nvarchar(20),
@IDPerson int output
as
IF (select p.IDPerson from Person as p where p.FirstName=@FirstName and p.LastName=@LastName) is null
	BEGIN
		INSERT into Person(FirstName, LastName)
		values(@FirstName, @LastName)
		SET @IDPerson = scope_identity()
	END
ELSE
	BEGIN
		select @IDPerson = p.IDPerson from Person as p 
		where p.FirstName=@FirstName and p.LastName=@LastName
	END

GO

CREATE proc selectPerson
@ID int
as
SELECT * from Person
where Person.IDPerson = @ID
go

CREATE proc selectPersons
as
SELECT * from Person
go

CREATE proc updatePerson
@ID int,
@FirstName nvarchar(20),
@LastName nvarchar(20)
as
UPDATE Person
SET Person.FirstName = @FirstName, Person.LastName = @LastName
where Person.IDPerson = @ID
GO

CREATE proc deletePerson
@ID int
as
DELETE from Actor
where Actor.PersonID = @ID
DELETE from Person
where Person.IDPerson = @ID
GO

--ACCOUNT PROCEDURES
CREATE proc createAccount
@Username nvarchar(20),
@Password nvarchar(20),
@Admin bit
as
INSERT into Account(Username, Password, Admin)
values(@Username, @Password, @Admin)
GO

CREATE proc checkAccount
@Username nvarchar(20)
as
SELECT CASE WHEN EXISTS (
SELECT * from Account
WHERE Account.Username = @Username
)
THEN CAST(1 AS BIT)
ELSE CAST(0 AS BIT)
END
as 'Check'
GO

CREATE proc selectAccount
@Username nvarchar(20)
as
SELECT * from Account
where Account.Username = @Username
GO

CREATE proc loginAccount
@Username nvarchar(20),
@Password nvarchar(20)
as
SELECT * from Account
where Account.Username = @Username and Account.Password = @Password
GO

--ACTOR PROCEDURES
CREATE proc createActor
@IDPerson int,
@IDMovie int
as
INSERT into Actor(PersonID, MovieID)
values(@IDPerson, @IDMovie)
GO

CREATE proc selectActors
@ID int
as
SELECT Person.IDPerson, Person.FirstName, Person.LastName from Actor inner join Person on Actor.PersonID = Person.IDPerson
where Actor.MovieID = @ID
GO

CREATE proc deleteActor
@IDPerson int,
@IDMovie int
as
DELETE from Actor
where Actor.MovieID = @IDMovie and Actor.PersonID = @IDPerson
GO

--DIRECTOR PROCEDURES
CREATE proc createDirector
@IDPerson int,
@IDMovie int
as
INSERT into Director(PersonID, MovieID)
values(@IDPerson, @IDMovie)
GO

CREATE proc selectDirectors
@ID int
as
SELECT Person.IDPerson, Person.FirstName, Person.LastName from Director inner join Person on Director.PersonID = Person.IDPerson
where Director.MovieID = @ID
GO

CREATE proc deleteDirector
@IDPerson int,
@IDMovie int
as
DELETE from Director
where Director.MovieID = @IDMovie and Director.PersonID = @IDPerson
GO

--Creating admin
exec createAccount 'admin', 'admin', 1


DECLARE @ID int
exec createPerson 'admin', 'admin', @ID output
select @ID