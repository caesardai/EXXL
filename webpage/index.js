var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({extended : true}));
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
var User = require('../dataSchemas/User.js');

// import the Event data schema
var Event = require('../dataSchemas/Event.js');

// import the Hotspot data schema
var Hotspot = require('../dataSchemas/Hotspot.js');

var Group = require('../dataSchemas/Group.js');

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

app.get('/test', function(req, res) {
	console.log('testing')
	res.json({'message' : 'made it here'})
})

// users endpoint
app.use('/users', async(req, res) => {
	res.sendFile(path.join(__dirname + '/users.html'));
});

app.use('/findUsers', async(req, res) => {
	var users = await User.find({});
	if (users.length === 0) {
		res.send([]);
	}
	else {
		res.send(users);
	}
})

app.get('/deleteUser', async function(req, res)  {
	var user = req.query.username;
	if (!user) {
		console.log("User not specified");
	}
	else {
		var deleted = await User.findOneAndRemove({ 'username' : user });
		if (!deleted) {
			console.log("Something went wrong deleting user @" + user);
		}
		else { console.log("Deleted!"); }
	}
	res.redirect('/users');
})

// events endpoint
app.use('/events', async(req, res) => {
	res.sendFile(path.join(__dirname + '/events.html'));
});

// find events endpoint
app.use('/findEvents', async(req, res) => {
	var events = await Event.find({});
	if (events.length === 0) {
		console.log([])
		res.send([]);
	}
	else {
		console.log(events)
		res.send(events);
	}
})

// find single event endpoint
app.use('/findSingleEvent', async(req, res) => {
	var event = await Event.find({"_id": req.query.id})
	if (!event) {
		res.send( { "response" : "error: event not found" }); 
	}
	else {
		res.send(event);
	}
})

app.use('/addUserEvent',  async function(req, res) {
	var eventId = req.query.eventId
	var userId = req.query.userId
	if (!eventId) {
		console.log("error: event not specified"); 
	}
	else {
		if (!userId) {
			console.log("error: user not specified"); 
		}
		else {
			try {
				var event = await Event.find({"_id": eventId})
				if (!event) {
					console.log("error: event not found"); 
				}
				else {
					var action = { '$push' : {'userEvents' : event} }
					var updatedUser = await User.findOneAndUpdate({_id: userId}, action, { new : true});
					if (!updatedUser) {
						console.log("User not found");
					}
					else {
						console.log("Update success!");
						console.log(updatedUser);
					}
					console.log(updatedUser)
				}
			}
			catch (err) {
				console.error("Error in updating", err);
			}
		}
	}
})

// delete event endpoint
app.get('/deleteEvent', async function(req, res) {
	var event = req.query.name;
	if (!event) {
		console.log("The event you picked does not exist");
	} else {
		// console.log('req.get("name"):' + req.get("name"));
		var deleted = await Event.findOneAndRemove({"name" : event});
		if (!deleted) {
			console.log("Unable to delete event: " + event.name);
		} else { 
			console.log("Event deletion was successful"); 
		}
	}
	res.redirect('/events');
});

// edit event endpoint
app.get('/editEvent', async function(req, res) {
	res.sendFile(path.join(__dirname + '/editEvents.html'));
});

// update event endpoint
app.use('/updateEvent', async function(req, res) {
	var filter = req.body.name;
	var action = {'$set' : {'name' : req.body.name, 'date' :  req.body.date, 'loaction' : req.body.location, 'host' : req.body.location, 'certification' : req.body.certification} }
	
	await Event.findOneAndUpdate(filter, action, async function(err, orig) {
		if (err) {
			console.log("Error in updating");
		} else if (!orig) {
			console.log("Event not found");
		} else {
			console.log("Update success!");
		}
 	});
});

// Groups endpoint
app.get('/groups', async function(req, res) {
	res.sendFile(path.join(__dirname + '/groups.html'));
})

app.get('/findGroups', async function(req, res) {
	var groups = await Group.find({});
	if (groups.length === 0) {
		res.send([]);
	}
	else {
		res.send(groups);
	}
})

app.get('/deleteGroup', async(req, res) => {

	await Group.deleteOne({ _id: req.query.id}).then(function(){
		// success
		res.send( { "response" : "Data deleted"} ); 
	}).catch(function(error){
		// failure
		res.send( { "response" : error }); 
	});

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

// delete hotspot endpoint 
app.get('/deleteHotspot', async(req, res) => {
	Hotspot.deleteOne({ _id: req.query.id }).then(function(){
		// success
		res.send( { "response" : "Data deleted"} ); 
	}).catch(function(error){
		// failure
		res.send( { "response" : error }); 
	});

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