/**
 * 
*/


// username: jshin@haverford.edu
// password: Nl8j30d@5tOX
var mongoose = require('mongoose');
// mongoose.connect('mongodb://localhost:27017/database');  // if localhost doesnt work, try 127.0.0.1
// mongoose.connect('mongodb://127.0.0.1:27017/test') // test database
// mongoose.connect('mongodb://127.0.0.1:27017/database')

mongoose.connect(
  `mongodb+srv://Ed:SkxYx4Owdb5ohaLJ@exxl.ml3ff7t.mongodb.net/?retryWrites=true&w=majority`);


const { db } = require('./dataSchemas/User.js');
var User = require('./dataSchemas/User.js');
main();

async function main() {
    
    // clear existing Users in collection
    await User.deleteMany({});

    // create user
    var newUser1 = new User({
        firstName: "Ed",
        lastName: "Shin",
        username: "Ed2Shin", 
        pronouns: "He/Him", 
        smHandle: "Ed2Shin",
        eventPostPermissions: true,
        eventRegisterPermissions: true,
        // list of users this user has interacted with
        interactedUsers: [],
        // list of event data for this user
        userEvents: []
    });

    // store user in collection
    await newUser1.save();
    console.log("saved");

    // print users currently in database
    var users = await User.find({});
    console.log(users);

}


