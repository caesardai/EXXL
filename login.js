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

    // getting the value of the username submitted by the user
    const inputUsername = document.getElementById("usernameValue").value;

    // get notSubmitted HTML element
    const notSubmitted = document.getElementById("userNotSubmittedText");

    // if user has not typed a username when they submit
    if (inputUsername === "" || inputUsername === undefined || inputUsername === null) {
        notSubmitted.style.visibility = "visible";
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
                        const notFound = document.getElementById("userNotFoundText");
                        notSubmitted.style.visibility = "hidden";
                        notFound.style.visibility = "visible";
                    }
                })
            }
        });
    }
}