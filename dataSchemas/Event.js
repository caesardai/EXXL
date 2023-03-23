
var mongoose = require('mongoose');
var User = require('./User.js');

var eventSchema = new mongoose.Schema({
    date: {type : Date},
    location: {type: String},
    activity: {type : String},
    owner: {type : String},
    interestedUsers: {type : []},
    eventChat: {type : {}}
});

// export userSchema as a class called User
module.exports = mongoose.model('Event', eventSchema);