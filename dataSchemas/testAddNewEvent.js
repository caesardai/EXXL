/**
 * 
*/

var mongoose = require('mongoose');

// ******* NOT SECURE. CHANGE EVENTUALLY ********
mongoose.connect(`mongodb+srv://Ed:SkxYx4Owdb5ohaLJ@exxl.ml3ff7t.mongodb.net/?retryWrites=true&w=majority`);

var User = require('./User.js');
var Address = require('./Address.js');
var Event = require('./Event.js');
var Group = require('./Group.js');
var Hotspot = require('./Hotspot.js');

main();

async function main() {

    var newEvent1 = new Event({
        name: "The newest Plenary", 
        date: "Jan 1st, 2001",
        location: "Haverford College 3",
        host: "eds",
        joinedUsers: [],
        certification: true,
        description: " ",
        
    });
    await newEvent1.save();


}

