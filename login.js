var express = require('express');
var path = require('path');
var fs = require('fs');
var jsdom = require('jsdom');
const { response } = require('express');
var app = express();

// using current directory
app.use(express.static(__dirname));

// using jsdom to get HTML information from index.html
var html = fs.readFileSync(path.join(__dirname, '/index.html'))
var document = new jsdom.JSDOM(html).window.document;

async function onSubmitUsername() {
    const inputUsername = document.getElementById("usernameValue").value;
    await fetch('http://localhost:3000/login').then((response) => {
        if (!response.ok) {
            const message = `Error: ${response.status}`;
            throw new Error(message);
        }
        else {
            response.json().then((data) => {
                var isAdmin = false;
                data.forEach((existingUsername) => {
                    if (inputUsername === existingUsername) {
                        isAdmin = true;
                        window.location.assign("/index.html");
                    }
                })
                if (isAdmin === false) {
                    const div = document.getElementById("userNotFoundText");
                    div.style.visibility = "visible";
                }
            })
        }
    });
}

