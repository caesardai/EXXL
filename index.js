var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();
const port = 3000;

// setting opening window to index page
var options = {
  index: "index.html"
}

// using current directory
app.use(express.static(__dirname, options))

// connecting to mongo database
var mongoose = require('mongoose');
mongoose.connect(`mongodb+srv://Ed:SkxYx4Owdb5ohaLJ@exxl.ml3ff7t.mongodb.net/?retryWrites=true&w=majority`);
var User = require('./dataSchemas/User.js');

// using jsdom to get HTML information from index.html
var html = fs.readFileSync(path.join(__dirname, '/index.html'))
var document = new jsdom.JSDOM(html).window.document;

// import the Event class from Person.js
var Event = require('./dataSchemas/Event.js');

/**
 * function that sets the background color of body of index.html to a random color
 */
function changeBackgroundColor() {
  var randomColor = Math.floor(Math.random()*16777215).toString(16);
  document.getElementById("body").style.backgroundColor = "#" + randomColor;
}


// empty endpoint (default)
app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname, '/index.html'));
});

// users endpoint
app.use('/users', async(req, res) => {
	console.log(`User Page!`)
	var users = await User.find({});
	console.log(users);
});

// events endpoint
app.get('/events', function(req, res)  {
  res.sendFile(path.join(__dirname + '/events.html'));
})

// hotspots endpoint
app.get('/hotspots', function(req, res)  {
  res.sendFile(path.join(__dirname + '/hotspots.html'));
})


// login endpoint
app.use('/login', async(req, res) => {
  var users = await User.find({});
  var returnArray = [];
  users.forEach(element => {
    returnArray.push(element.username);
  });
  res.send(returnArray);
});

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
});