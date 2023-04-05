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

    // getting the HTML content of the card the hotspots will be displayed on 
    var hotspotListCard = document.getElementById("hotspotListCard");

    // getting hotspots from database
    await fetch('http://localhost:3000/findHotspots').then((response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {

            // creating HTML to contain hotspot items
            var newListGroupHTML = 
            "<div class='list-group'>";

            // adding hotspot items and remove button to newListGroupHTML 
            response.json().then((data) => {
                var numHotspots = 0;
                data.forEach((hotspot) => {
                    numHotspots += 1
                    newListGroupHTML += "<button type='button' class='list-group-item list-group-item-action' id='" + hotspot._id + "'>Longitude: " +
                    hotspot.longitude + ", Latitude: " + hotspot.latitude + "</button>" + 
                    "<a class='btn btn-outline-danger btn-sm' onclick='onDeleteClick(this.id)' id='delete" + numHotspots + "'>Delete</a>";
                });
                newListGroupHTML += "</div>";

                // adding HTML we've made here to hotspots.html
                var newElement = document.createElement("div");
                newElement.innerHTML = newListGroupHTML;
                hotspotListCard.appendChild(newElement);  
            });
        }
    });
}

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