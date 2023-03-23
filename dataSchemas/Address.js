
var mongoose = require('mongoose');
var User = require('./User.js');

var AddressSchema = new mongoose.Schema({
    streetNum: {type: Number},
    streetAddress: {type : String},
    city: {type: String},
    state: {type: String},
    zip: {type: Number}
});

// export userSchema as a class called User
module.exports = mongoose.model('Address', AddressSchema);