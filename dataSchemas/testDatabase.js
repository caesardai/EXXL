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
    await clearDatabase();
    await populateUserDatabase();
    await populateEventDatabase();
    await populateHotspotDatabase();
    await populateGroupDatabase();

    // print users currently in database
    var users = await Event.find({});
    console.log(users);

    // var hotspots = await Hotspot.find({});
    // console.log(hotspots);

    // // update ed
    // const doc = await User.findOne({username: 'eds'});
    // const update = {smHandle: 'edis2coolforthis'};
    // await doc.updateOne(update);

    // const updatedDoc = await User.findOne({username: 'eds'});
    // console.log(updatedDoc);
;
}

// nuke database
async function clearDatabase() {
    await User.deleteMany({});
    await Group.deleteMany({});
    await Hotspot.deleteMany({});
    await Event.deleteMany({});

}

async function populateUserDatabase() {

    // USERS
    var newUser1 = new User({
        firstName: "Ed",
        lastName: "Shin",
        username: "eds", 
        pronouns: "He/Him", 
        // smHandle: "EdisCool",
        eventPostPermissions: true,
        eventRegisterPermissions: true,
        // list of users this user has interacted with
        interactedUsers: [],
        // list of event data for this user
        userEvents: []
    });
    await newUser1.save();

    var newUser2 = new User({
        firstName: "Xavier",
        lastName: "DeVore",
        username: "xavierd", 
    });
    await newUser2.save()

    var newUser3 = new User({
        firstName: "Leo",
        lastName: "Costa",
        username: "leoc", 
    });
    await newUser3.save()

    var newUser4 = new User({
        firstName: "Xufeng",
        lastName: "Dai",
        username: "xufengd", 
    });
    await newUser4.save()
    

}

async function populateEventDatabase() {
    var newEvent1 = new Event({
        name: "Plenary", 
        date: "Jan 1st, 2001",
        location: "Haverford College",
        host: "Wendy Raymond",
        certification: true,
        description: " ",
        // interestedUsers: ["Alice", "Bob", "Cathy"],
        // eventChat:"", skipping event chat for this iteration
    });
    await newEvent1.save();

    var newEvent2 = new Event({
        name: "Bryn Mawr Plenary", 
        date: "Feb 2nd, 2002",
        location: "Bryn Mawr College",
        host: "Kimberly Cassidy",
        certification: true,
        description: " "
        // interestedUsers: ["Alice", "Bob", "Cathy"]
        // eventChat:"", skipping event chat for this iteration
    });
    await newEvent2.save();

    var newEvent3 = new Event({
        name: "No Plenary",
        date: "March 3rd, 2003",
        location: "Swarthmore College",
        host: "Valerie Smith",
        certification: false,
        description: " ",
        // interestedUsers: [],
        // eventChat:"", skipping event chat for this iteration
    });
    await newEvent3.save();
}

/**
 * function to populate the hotspot database
 */
async function populateHotspotDatabase() {
    var newHotspot1 = new Hotspot({
        longitude: "-75.285462",
        latitude: "40.0068"
    })
    await newHotspot1.save();

    var newHotspot2 = new Hotspot({
        longitude: "-75.1720",
        latitude: "39.9012"
    })
    await newHotspot2.save();
}

async function populateGroupDatabase(){
        // GROUPS
        var newGroup1 = new Group({
            groupName: "CSBuddies",
            assocActivity: "Programming",
            users: [
                new User({
                    firstName: "Ed",
                    lastName: "Shin",
                    username: "eds", 
                    pronouns: "He/Him", 
                    // smHandle: "EdisCool",
                    eventPostPermissions: true,
                    eventRegisterPermissions: true,
                    // list of users this user has interacted with
                    interactedUsers: [],
                    // list of event data for this user
                    userEvents: []
                }),
                new User({
                    firstName: "Xufeng",
                    lastName: "Dai",
                    username: "xufengd", 
                })
            ]
        })
        await newGroup1.save();
    
        var newGroup2 = new Group({
            groupName: "Ballerz",
            assocActivity: "Basketaball",
            users: [
                new User({
                    firstName: "Xavier",
                    lastName: "DeVore",
                    username: "xavierd", 
                }),
                new User({
                    firstName: "Xufeng",
                    lastName: "Dai",
                    username: "xufengd", 
                })
            ]
        })
        await newGroup2.save();
}


