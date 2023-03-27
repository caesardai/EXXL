var mongoose = require('mongoose');

// hotspot can be 0.001 degrees, or 111 m. reference:
// https://www.usna.edu/Users/oceano/pguth/md_help/html/approx_equivalents.htm#:~:text=At%20the%20equator%20for%20longitude,(2%20decimals%2C%20km%20accuracy)
var hotspotSchema = new mongoose.Schema({
    longitude: {type : String, required : true},
    latitude: {type : String, required : true},
});

// export hotspotSchema as a class called Hotspot
module.exports = mongoose.model('Hotspot', hotspotSchema);