var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
var app = express();

// using current directory
app.use(express.static(__dirname));

// using jsdom to get HTML information from index.html
var html = fs.readFileSync(path.join(__dirname, '/login.html'))
var document = new jsdom.JSDOM(html).window.document;

/**
 * function to check if username is a valid admin username
 * currently, we don't have admin/non-admin in the User schema, so everyone is an admin
 * this function is called when the user presses the "Login" button in login.html
 */
async function onSubmitUsername() {

    // getting HTML element for the username input
    const usernameInputLocation = document.getElementById("usernameInputLocation");

    // getting the value of the username submitted by the user
    const inputUsername = document.getElementById("usernameValue").value;

    // if user has not typed a username when they submit
    if (inputUsername === "" || inputUsername === undefined || inputUsername === null) {

        // call helper function
        displayUsernameNotSubmittedText(usernameInputLocation);
    }
    else {

        // getting list of usernames from the database
        await fetch('http://localhost:3000/login').then((response) => {
            if (!response.ok) {
                const message = `Error: ${response.status}`;
                throw new Error(message);
            }
            else {
                response.json().then((data) => {
                    var isAdmin = false;

                    // iterating through users, if user is found, index.html (homepage) is opened 
                    data.forEach((existingUsername) => {
                        if (inputUsername === existingUsername) {
                            isAdmin = true;
                            window.location.assign("/index.html");
                        }
                    })

                    // if user is not found, display user not found text element
                    if (isAdmin === false) {

                        // calling helper function
                        displayUsernameNotFoundText(usernameInputLocation);
                    }
                })
            }
        });
    }
}

function displayUsernameNotSubmittedText(usernameInputLocation) {

    if (usernameInputLocation === null) {
        return;
    }

    // creating new HTML element for "Please submit a username" message
    var newNotSubmittedTextHTML = 
        "<p class='text-center' style='color: crimson;'>Please submit a username</p>";
    var newElement = document.createElement("div");
    newElement.id = "usernameNotSubmittedText";
    newElement.innerHTML = newNotSubmittedTextHTML;

    // if "Username not found" message is already displayed, remove it
    if (usernameInputLocation.nextSibling.id === "usernameNotFoundText") {
        usernameInputLocation.nextSibling.remove();
    }

    // if "Please submit a username" message is not already displayed, we add the HTML we've made to hotspots.html
    if (usernameInputLocation.nextSibling.id !== "usernameNotSubmittedText") {
        usernameInputLocation.insertAdjacentElement("afterend", newElement); 
    }

}

function displayUsernameNotFoundText(usernameInputLocation) {

    if (usernameInputLocation === null) {
        return;
    }

    // if "Please submit a username" message is already displayed, remove it
    if (usernameInputLocation.nextSibling.id === "usernameNotSubmittedText") {
        usernameInputLocation.nextSibling.remove();
    }

    // if "Username not found" message is not already displayed, we add the HTML we've made to hotspots.html
    if (usernameInputLocation.nextSibling.id !== "usernameNotFoundText") {

        // creating new HTML element for "Username not found" message
        var newUsernameNotFoundHTML =
            "<p class='text-center' style='color: crimson;'>Username not found</p>"
            var newElement = document.createElement("div");
            newElement.id = "usernameNotFoundText";
            newElement.innerHTML = newUsernameNotFoundHTML;
        usernameInputLocation.insertAdjacentElement("afterend", newElement); 
    }
}