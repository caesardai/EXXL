var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();

// using current directory
app.use(express.static(__dirname));

var html = fs.readFileSync(path.join(__dirname, '/groups.html'))
var window = new jsdom.JSDOM(html).window;
var document = new jsdom.JSDOM(html).window.document;

/**
 * function to find the hotspots from database and display them on the hotspots page
 * hotspots will be displayed as text buttons
 * this function is currently called when hotspots.html loads (using onload function)
 */
async function findGroupsAndDisplay() {

    // getting the HTML content of the card the hotspots will be displayed on 
    var groupListCard = document.getElementById("groupListCard");

    // getting hotspots from database
    await fetch('http://localhost:3000/findGroups').then((response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            // creating HTML to contain hotspot items
            var newListGroupHTML = 
            "<div class='list-group'>";

            // adding hotspot items to newListGroupHTML 
            response.json().then((data) => {
                data.forEach((group) => {
                    usersObj = group.users;
                    usersArr = new Array();
                    usersObj.forEach((user) => {
                        usersArr.push(" " + user.username); 
                    })

                    newListGroupHTML += "<button type='button' class='list-group-item list-group-item-action'>Group name: " +
                    group.groupName + ", Associated Activity: " + group.assocActivity + 
                    ", Users:" + usersArr + "</button>";
                });
                newListGroupHTML += "</div>";

                // adding HTML we've made here to hotspots.html
                var newElement = document.createElement("div");
                newElement.innerHTML = newListGroupHTML;
                groupListCard.appendChild(newElement);  
            });
        }
    });
}