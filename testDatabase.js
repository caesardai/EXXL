/**
 * 
*/

var mongoose = require('mongoose');

// ******* NOT SECURE. CHANGE EVENTUALLY ********
mongoose.connect(`mongodb+srv://Ed:SkxYx4Owdb5ohaLJ@exxl.ml3ff7t.mongodb.net/?retryWrites=true&w=majority`);

var User = require('./dataSchemas/User.js');
var Address = require('./dataSchemas/Address.js');
var Event = require('./dataSchemas/Event.js');
var Group = require('./dataSchemas/Group');
var Hotspot = require('./dataSchemas/Hotspot.js');

main();

async function main() {
    await clearDatabase();
    await populateDatabase();
    await populateHotspotDatabase()

    // print users currently in database
    var users = await User.find({});
    console.log(users);

    var hotspots = await Hotspot.find({});
    console.log(hotspots);

    // update ed
    const doc = await User.findOne({username: 'eds'});
    const update = {smHandle: 'edis2coolforthis'};
    await doc.updateOne(update);

    const updatedDoc = await User.findOne({username: 'eds'});
    console.log(updatedDoc);
;
}

// nuke database
async function clearDatabase() {
    await User.deleteMany({});
    await Group.deleteMany({});

}

async function populateDatabase() {

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

    // GROUPS
    var newGroup1 = new Group({
        groupName: "CSBuddies",
        assocActivit: "Programming",
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
        assocActiit: "Basketaball",
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


