const webpack = require('webpack');
const WebpackDevServer = require('webpack-dev-server');
const devConfig = require('./webpack.config');
const open = require('open');

new WebpackDevServer(webpack(devConfig), devConfig.devServer)
    .listen(9090, '0.0.0.0', (err) => {
        if (err) {
            console.log(err);
        }
        console.log('Opening your system browser...');
        open('http://localhost:9090/webpack-dev-server/');
    });
