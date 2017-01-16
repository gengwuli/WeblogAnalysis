var mysql = require('mysql');
var logger = require('morgan');
var express = require('express');
var bodyParser = require('body-parser');

var app = express();
app.use(bodyParser.json());
app.use(logger('combined'));
app.use(corsEnable);
console.log("directory:", __dirname);
app.get('/', hello);

function corsEnable(req, res, next) {
	res.set({
		'Access-Control-Allow-Origin': req.get('origin'),
		'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE',
		'Access-Control-Allow-Credentials': 'true',
		'Access-Control-Allow-Headers': 'Authorization, Content-Type, X-Requested-With'
	});
	if (req.method == 'OPTIONS') {
		return res.sendStatus(200);
	}
	next();
}

var con = mysql.createConnection({
	host: '172.16.249.139',
	user: 'root',
	password: 'root',
	database: 'db_weblog'
});

con.connect(function(err) {
	if (err) {
		console.log('Error connecting to DB');
		return;
	}
	console.log('Connection established');
});

if (process.env.NODE_ENV !== 'production') {
	require('dot-env')
}

var port = process.env.PORT || 3001

var server = app.listen(port, function() {
	console.log('Server listening at http://%s:%s', 
		server.address().address,
		server.address().port)
});

function hello(req, res) {
	con.query('select * from dw_pvs_refhost_topn_h', function(err, rows) {
		res.send({'topn':rows});
	})
}
