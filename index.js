const express = require('express');
const path = require('path');
const fs = require('fs');
const jsdom = require('jsdom');
const app = express();
const port = 3000;

console.log(express);

// using jsdom to get HTML information from index.html
const html = fs.readFileSync(path.join(__dirname, '/index.html'))
const htmlDocument = new jsdom.JSDOM(html).window.document;

// using current directory
app.use(express.static(__dirname));


// sendFile will go here
app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname, '/index.html'));
});

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
});