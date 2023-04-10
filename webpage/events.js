var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();

// using current directory
app.use(express.static(__dirname));

var html = fs.readFileSync(path.join(__dirname, '/events.html'))
var window = new jsdom.JSDOM(html).window;
var document = new jsdom.JSDOM(html).window.document;

/**
 * TODO: fill
 */
async function findEventsAndDisplay() {

    // get HTML content of the card to put events list 
    var eventListCard = document.getElementById("eventsCard");

    // get list of events from database
    await fetch('http://localhost:3000/findEvents').then(async (response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            // creating HTML to contain the events
            var newListGroupHTML = "";
            var empty = true;

            await response.json().then((data) => {
                data.forEach((event) => {
                    // keeps track of whether any events are added to the HTML
                    empty = false;

                    // Create box with one event's information
                    newListGroupHTML += '<div class="shadow-none p-3 mb-2 bg-light rounded">';
                    newListGroupHTML += "<b>" + event.name + "</b><br>" + event.location + 
                        "; Host: " + event.host + ";";

                    if (event.certification)
                        newListGroupHTML += " ✓";
                    else
                        newListGroupHTML += " ✕";

                    newListGroupHTML += "<br>Event description: " + event.description + "<br>";
                    // newListGroupHTML += "<br><a href=\"/deleteEvent?name=" + event.name + "\">[Delete]</a><br><br>";

                    // Add edit button (placeholder)
                    newListGroupHTML += '<a class="btn btn-outline-danger btn-sm" href=\"/editEvent?name='
                    + event.name + '" role="button">Edit</a>';
                    // '<button type="button" class="btn btn-outline-warning btn-sm">Edit</button>\t'

                    // Add delete button (placeholder)
                    newListGroupHTML += '<a class="btn btn-outline-danger btn-sm" href=\"/deleteEvent?name='
                                        + event.name + '" role="button">Delete</a>';

                    newListGroupHTML += "</div>";
                });
            });

            // if not events are added to the HTML (no events in database)...
            if (empty)
                newListGroupHTML += "No events in database.";

            // adding HTML we've made here to hotspots.html
            var newElement = document.createElement("div");
            newElement.innerHTML = newListGroupHTML;
            eventListCard.appendChild(newElement);  
        }
    });
}
