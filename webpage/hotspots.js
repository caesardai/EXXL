var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();

// using current directory
app.use(express.static(__dirname));

// using jsdom to get HTML information from hotspots.html
var html = fs.readFileSync(path.join(__dirname, '/hotspots.html'))
var window = new jsdom.JSDOM(html).window;
var document = new jsdom.JSDOM(html).window.document;

/**
 * function to find the hotspots from database and display them on the hotspots page
 * hotspots will be displayed as text buttons
 * this function is currently called when hotspots.html loads (using onload function)
 */
async function findHotspotsAndDisplay() {
    var hotspotListCard = document.getElementById("hotspotListCard");

    await fetch('http://localhost:3000/findHotspots').then(async (response) => {
        console.log("here1");

        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        } else {
            var newListGroupHTML = "";
            var empty = true;
            var verified = false;
            console.log("here");
            await response.json().then((data) => {
                Object.entries(data).forEach(([zipCodeSubstring, count]) => {
                    var checkmark = " x";
                    if (verified === true) {
                        checkmark = " ✓";
                    }
                    empty = false;

                    console.log(zipCodeSubstring);
                    newListGroupHTML += '<div class="shadow-none p-3 mb-2 bg-light rounded">';
                    newListGroupHTML += "<b>Hotspot zipcode: " + count +  "</b><br>";
                    newListGroupHTML += '<a class="btn btn-outline-danger btn-sm" href="#" role="button" onclick="verifyZipEvent(event, '
                    + count + ')">Verify</a>';
                    newListGroupHTML += '</div>';  // Close the <div> tag opened earlier
                });
            });

            // Add the generated HTML to the hotspotListCard element
            var newElement = document.createElement("div");
            newElement.innerHTML = newListGroupHTML;
            hotspotListCard.appendChild(newElement);
        }
    });
}

async function verifyZipEvent(event, zipCode) {
    event.preventDefault();
    try {
        const response = await fetch('/verifyZipEvent?zipcode=' + zipCode);
        if (response.ok) {
            const targetElement = event.target;
            const parentDiv = targetElement.parentNode;
            const textNode = parentDiv.childNodes[0];
            textNode.nodeValue = textNode.nodeValue.replace(" x", " ✓");
        } else {
            console.error("Error verifying the zipcode:", response.status);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}


// // creating HTML to contain hotspot items
// var newListGroupHTML = 
// "<div class='list-group'>";

// // adding hotspot items and remove button to newListGroupHTML 
// response.json().then((data) => {
//     // var numHotspots = 0;
//     data.forEach((event) => {
//         empty = false;
//         // numHotspots += 1
//         newListGroupHTML += "<button type='button' class='list-group-item' id='" + event.zipcode + "'>Location: " +
//         event.location + 
//         "<a class='btn btn-outline-danger btn-sm' onclick='onDeleteClick(this.id)' id='delete" + "'>Verify</a>";
//         newListGroupHTML += "</div>";

//     });
    
//     // adding HTML we've made here to hotspots.html
//     var newElement = document.createElement("div");
//     newElement.innerHTML = newListGroupHTML;
//     hotspotListCard.appendChild(newElement);  
// });

/**
 * function to delete a hotspot from the hotspots page and database
 * @param {string} idClicked id of the remove button that was clicked
 */
async function onDeleteClick(idClicked) {
    
    // get information of the delete button that was clicked
    var deleteClicked = document.getElementById(idClicked);

    // get the hotspot that the clicked delete button is associated with 
    var hotspotRemoved = deleteClicked.previousSibling;

    // create url to make api call
    var url = 'http://localhost:3000/deleteHotspot?id=' + hotspotRemoved.id;

    // deleting hotspot from database
    await fetch(url).then((response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            response.json().then((data) => {

                // check if hotspot was deleted
                if (data["response"] === "Data deleted"){

                    // if so, remove html elements (hotspot and associated button)
                    deleteClicked.remove();
                    hotspotRemoved.remove();
                }
                else {
                    throw new Error(data["response"]);
                }
            })
        }
    });



}