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
    await fetch('http://localhost:3000/findGroups').then(async (response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            // creating HTML to contain hotspot items
            var newListGroupHTML = "<div class='list-group'>";
            var empty = true;

            // adding hotspot items to newListGroupHTML 
            await response.json().then((data) => {
                data.forEach((group) => {
                    empty = false;

                    // Display group information
                    newListGroupHTML += '<div class="shadow-none p-3 mb-2 bg-light rounded">';
                    newListGroupHTML += "<b>" + group.groupName + "</b> <br>";
                    newListGroupHTML += "Activity: " + group.assocActivity + "<br>";
                    usersObj = group.users;
                    usersArr = new Array();
                    usersObj.forEach((user) => {
                        usersArr.push(" " + user.username); 
                    })
                    newListGroupHTML += "Users: " + usersArr + "<br>";

                    // Delete Button
                    newListGroupHTML += '<input type="button" class="btn btn-outline-danger btn-sm" value=Delete onclick="deleteGroup(' + "'" + group._id + "'" + ')">'
                    newListGroupHTML += '<script type="text/javascript" src="./index.js"></script>'
                    
                    newListGroupHTML += '</div>';

                });

                var newElement = document.createElement("div");
                newElement.innerHTML = newListGroupHTML;
                groupListCard.appendChild(newElement);  
            });

            if (empty){
                newListGroupHTML += "No groups in database.";
            }
        }
    });
}

async function deleteGroup(groupid){
    var url = 'http://localhost:3000/deleteGroup?id=' + groupid;

    await fetch(url).then((response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            response.json().then((data) => {

                // check if hotspot was deleted
                if (data["response"] === "Data deleted"){

                    window.location.reload();
                }
                else {
                    throw new Error(data["response"]);
                }
            })
        }
    });
}

