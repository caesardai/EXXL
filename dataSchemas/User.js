var mongoose = require('mongoose');

var userSchema = new mongoose.Schema({
    firstName: {type : String, required : true},
    lastName: {type : String, required : true},
    username: {type : String, required : true, unique : true},
    password: {type : String, required : true},
    pronouns: {type : String},
    smHandle: {type : String},
    eventPostPermissions: {type : Boolean},
    eventRegisterPermissions: {type : Boolean},
    // list of users this user has interacted with
    interactedUsers: {type : [] },
    // list of event data for this user
    userEvents: {type : [] }
    // INSERT HERE chats
});

// export userSchema as a class called User
module.exports = mongoose.model('User', userSchema);




