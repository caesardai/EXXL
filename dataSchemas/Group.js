
var mongoose = require('mongoose');
var User = require('./User.js');

var groupSchema = new mongoose.Schema({
    groupName: {type: String},
    assocActivity: {type: String},
    chat: {type: {} },
    users: {type: {} }

});

// export userSchema as a class called User
module.exports = mongoose.model('Group', groupSchema);