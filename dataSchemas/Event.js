
var mongoose = require('mongoose');
// var User = require('./User.js');

var eventSchema = new mongoose.Schema({
    name: {type : String, required : true, unique : true},
    date: {type : String, required : true},
    location: {type: String, required : true, unique : true},
    host: {type : String, required : true},
    joinedUsers: {type : [], required : true},
    certification: {type : Boolean, required : true},
    description: {type: String},
    // eventChat: {type : {}}
});

// export userSchema as a class called User
module.exports = mongoose.model('Event', eventSchema);