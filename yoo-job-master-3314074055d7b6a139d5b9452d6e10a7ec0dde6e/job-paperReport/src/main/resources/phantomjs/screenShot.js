var page = require('webpage').create(), system = require('system'), url, st, domain;
page.viewportSize = { width: 794, height: 1100 };
var url = system.args[1];
var st = system.args[2];
var domain = system.args[3];

var c = '{"name":"S_T","value":"'+st+'","domain":"'+domain+'"}';
phantom.addCookie(JSON.parse(c));

page.open(encodeURI(url), function (status) {
    if (status === "success") {
		setTimeout(function(){
			console.log(page.renderBase64('PNG'));
			phantom.exit();}, 1000);
    } else {
		phantom.exit();
	}
    
});