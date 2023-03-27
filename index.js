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

// import the User data schema
var User = require('./dataSchemas/User.js');

// import the Event data schema
var Event = require('./dataSchemas/Event.js');

// import the Hotspot data schema
var Hotspot = require('./dataSchemas/Hotspot.js');

// using jsdom to get HTML information from index.html
var html = fs.readFileSync(path.join(__dirname, '/index.html'))
var document = new jsdom.JSDOM(html).window.document;


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
	var userHTML = fs.readFileSync(path.join(__dirname, '/users.html'));
	var documentUser = new jsdom.JSDOM(userHTML).window.document;
	
	var users = await User.find({});

	// TODO: Test case of empty database
	if (users.length == 0) {
		res.type('html').status(200);
		res.write('There are no people');
		res.end();
		return;
	}
	else {
		res.type('html').status(200);
		console.log(documentUser.getElementById("p1").innerHTML);
		documentUser.getElementById("p1").textContent = "HELLO";
		// res.write('Here are the users in the database:');
	}

	res.sendFile(path.join(__dirname, '/users.html'));
	
	// console.log(users);
});

// events endpoint
app.get('/events', function(req, res)  {
	res.sendFile(path.join(__dirname + '/events.html'));
})

// hotspots page endpoint
app.get('/hotspots', function(req, res)  {
	res.sendFile(path.join(__dirname + '/hotspots.html'));
})

// find hotspots endpoint
app.get('/findHotspots', async(req, res) => {
	var hotspots = await Hotspot.find({});
	if (hotspots.length === 0) {
		res.send([]);
	}
	else {
		res.send(hotspots);
	}
})

// login endpoint
app.use('/login', async(req, res) => {
	var users = await User.find({});
	if (users.length === 0) {
		res.send([]);
	}
	else {
		var returnArray = [];
		users.forEach(element => {
			returnArray.push(element.username);
		});
		res.send(returnArray);
	}

});

app.listen(port, () => {
	console.log(`Example app listening on port ${port}`)
});