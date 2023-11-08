# csvmanager
A microservice which serves the contents of player.csv through REST API.

The service should expose two REST endpoints:
GET /api/players - returns the list of all players.
GET /api/players/{playerID} - returns a single player by ID.

(1) Run CSVManagerApplication.java
(2) Postman- New Collection - Intuit
(3) Add new request - POST - "localhost:8080/upload"
  (3.1) Add Body - form-data with Key "file" and Value "player.csv" (assuming this is the name of the csv uploaded file)
  (3.2) Send Request
  (3.3) Expected Valid Body Response - Status 202 Accepted : {
    "message": "File upload received and is being processed.",
    "jobId": "b259ae93-a711-4a01-be19-ec0d76cf00e1"
}
(4) Add new request - GET - "localhost:8080/all".
  (4.1) Expected response is 200 OK with a Body of a list of Player objects represented as JSON's. 
        Each JSON object corresponds to a CSV Record.
(4) Add new request - GET - "localhost:8080/{playerId}". Instead of playerId enter a specific id for example "aardsda01"
  (4.1) Expected valid response is 200 OK with a specific player object fetched by the given id input parameter and represented as JSON.
        For example : {
    "playerId": "aardsda01",
    "birthYear": 1981,
    "birthMonth": 12,
    "birthDay": 27,
    "birthCountry": "USA",
    "birthState": "CO",
    "birthCity": "Denver",
    "deathCountry": "",
    "deathState": "",
    "deathCity": "",
    "nameFirst": "David",
    "nameLast": "Aardsma",
    "nameGiven": "David Allan",
    "weight": 215,
    "height": 75,
    "bats": "R",
    "throws_": "R",
    "debut": "2004-04-06",
    "finalGame": "2015-08-23",
    "retroId": "aardd001",
    "bbrefId": "aardsda01"
}
