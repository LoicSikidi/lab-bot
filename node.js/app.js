var http = require('http');
var restify = require('restify');
var builder = require('botbuilder');

//=========================================================
// Bot Setup
//=========================================================

// Setup Restify Server
var server = restify.createServer();
server.use(restify.bodyParser());
server.listen(process.env.NODEJSPORT || 12345, function () {
   console.log('%s listening to %s', server.name, server.url); 
});
  
// Create chat bot
var connector = new builder.ChatConnector({
    appId: process.env.APPID,
    appPassword: process.env.APPSECRET
});
var bot = new builder.UniversalBot(connector);
server.post('/api/messages', connector.listen());


//=========================================================
// Listen from MBC
//=========================================================

bot.dialog('/', function (session) {
	sendBot(session);
	//session.send('Hello World');
    console.log("after sendBot");
});

//=========================================================
// Send to Bot
//=========================================================
function sendBot(session){
	var data = JSON.stringify(session.message);
	
	var options = {
        host: 'localhost',
        port: 12345,
        path: '/mbc/',
        method: 'POST'
    };

    var req = http.request(options);
    req.write(data);
    req.end();
}

//=========================================================
// Listen from bot
//=========================================================
server.post('/mbc', function(request, result, obj, next) {
    result.send(200);
    sendToMBC(request.body.text, request.body.address);
    return next();
});

function sendToMBC(bodyText, bodyAddress){
	var msg = new builder.Message()
				.address(bodyAddress)
				.text(bodyText);
	bot.send(msg);
}