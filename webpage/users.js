var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();

// using current directory
app.use(express.static(__dirname));

var html = fs.readFileSync(path.join(__dirname, '/users.html'))
var window = new jsdom.JSDOM(html).window;
var document = new jsdom.JSDOM(html).window.document;

/**
 * TODO: fill
 */
async function findUsersAndDisplay() {

    // get HTML content of the card to put user list 
    var userListCard = document.getElementById("usersCard");

    // get list of users from database
    await fetch('http://localhost:3000/findUsers').then(async (response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            // creating HTML to contain the users
            var newListGroupHTML = "";
            var empty = true;

            await response.json().then((data) => {
                data.forEach((user) => {
                    empty = false;

                    // Create box with name and username
                    newListGroupHTML += '<div class="shadow-none p-3 mb-2 bg-light rounded">';
                    newListGroupHTML += "<b>" + user.firstName + " " + user.lastName + "</b> (@" + user.username + ")<br>";

                    // Add edit button (placeholder)
                    newListGroupHTML += '<button type="button" class="btn btn-outline-warning btn-sm">Edit</button>\t';

                    // Add delete button
                    newListGroupHTML += '<a class="btn btn-outline-danger btn-sm" href="/deleteUser?username='
                                        + user.username + '" role="button">Delete</a>';

                    newListGroupHTML += "</div>";
                });
            });

            if (empty)
                newListGroupHTML += "No users in database.";

            // adding HTML we've made here to hotspots.html
            var newElement = document.createElement("div");
            newElement.innerHTML = newListGroupHTML;
            userListCard.appendChild(newElement);  
        }
    });
}
