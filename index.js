const express = require('express');
const path = require('path');
const fs = require('fs');
const jsdom = require('jsdom');
const app = express();
const port = 3000;

console.log(express);

var mongoose = require('mongoose');
mongoose.connect(`mongodb+srv://Ed:SkxYx4Owdb5ohaLJ@exxl.ml3ff7t.mongodb.net/?retryWrites=true&w=majority`);
var User = require('./dataSchemas/User.js');

// using jsdom to get HTML information from index.html
const html = fs.readFileSync(path.join(__dirname, '/index.html'))
const htmlDocument = new jsdom.JSDOM(html).window.document;

// import the Event class from Person.js
var Event = require('./dataSchemas/Event.js');

// using current directory
app.use(express.static(__dirname));

// sendFile will go here
app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname, '/index.html'));
});

app.use('/users', async(req, res) => {
	console.log(`User Page!`)
	var users = await User.find({});
	console.log(users);
});

app.get('/events', function(req, res)  {
  res.sendFile(path.join(__dirname + '/events.html'));
})


app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
});