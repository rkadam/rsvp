var fs = require('fs');
var path = require('path');

var extend = require('util-extend');
var config = require('../../config');

var configuration = config.email;

try {
    var local = path.resolve(__dirname, 'configuration.local.json');
    var stats = fs.lstatSync(local);

    if (stats.isFile()) {
        var localConfig = JSON.parse(fs.readFileSync(local, 'utf8'));
        configuration = extend(configuration, localConfig);
    }
}
catch (e) {
    /* swallow */
}

module.exports = {
    getConfig : function(){
        return configuration;
    }
};
