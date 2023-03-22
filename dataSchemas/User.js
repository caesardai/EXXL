// username, name, location, pronouns (if desired), social media handle, list of users user has interacted with, list of event data by user, chats, permissions


var mongoose = require('mongoose');

// mongoose.connect('mongodb://localhost:27017/database');  // if localhost doesnt work, try 127.0.0.1
// mongoose.connect('mongodb://127.0.0.1:27017/test') // test database
// mongoose.connect('mongodb://127.0.0.1:27017/database')


//user Ed pass moFq4GrjaBtBeJYE

console.log("hello");


// Each Schema maps to a MongoDB collection, defines shape of documents within that collection
var Schema = mongoose.Schema;

var userSchema = new Schema({
    firstName: {type : String, required : true},
    lastName: {type : String, required : true},
    username: {type : String, required : true, unique : true},
    pronouns: {type : String},
    smHandle: {type : String},
    eventPostPermissions: {type : Boolean},
    eventRegisterPermissions: {type: Boolean},
    // list of users this user has interacted with
    interactedUsers: {type : {}},
    // list of event data for this user
    userEvents: {type : {}}
    // INSERT HERE chats
});

// export userSchema as a class called User
module.exports = mongoose.model('User', userSchema);




