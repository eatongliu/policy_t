var path = require('path');
var webpack = require('webpack');
var ExtractTextPlugin = require('extract-text-webpack-plugin'); //样式提取到单独的css文件
var HtmlWebpackPlugin = require('html-webpack-plugin'); //生成html

var pages = [
    'index',
    'search',
    'article',
    'chart',
    'sub',
    'user',
    'help',
    'about'
]

function getEntry(src) {
    return ['webpack-dev-server/client?http://localhost:9090', 'webpack/hot/only-dev-server', src]
}
var entrys = {};
pages.map(function(one) {
    entrys[one] = getEntry('./src/js/page/' + one + '.js');
})

function HtmlWebpackPluginCreator(name) {
    return new HtmlWebpackPlugin({
        favicon: './src/img/favicon.ico', //favicon路径，通过webpack引入同时可以生成hash值
        filename: './' + name + '.html', //生成的html存放路径，相对于path
        template: './src/' + name + '.html', //html模板路径
        inject: 'body', //js插入的位置，true/'head'/'body'/false
        hash: true,
        chunks: [
            'vendors', name
        ], //需要引入的chunk，不配置就会引入所有页面的资源
        minify: { //压缩HTML文件
            removeComments: true, //移除HTML中的注释
            collapseWhitespace: false //删除空白符与换行符
        }
    })
}
var htmls = pages.map(function(one) {
    return HtmlWebpackPluginCreator(one)
})
module.exports = {
    entry: entrys,
    output: {
        path: path.join(__dirname, 'dist'), //输出目录的配置，模板、样式、脚本、图片等资源的路径配置都相对于它
        publicPath: './', //模板、样式、脚本、图片等资源对应的server上的路径
        filename: './js/[name].js', //每个页面对应的主js的生成配置
        chunkFilename: './js/[id].chunk.js' //chunk生成的配置
    }, catch: true,
    devtool: "cheap-module-source-map",
    module: {
        preLoaders: [
            {
                test: /\.(js|jsx)$/,
                include: path.join(__dirname, 'src'),
                loader: 'eslint-loader'
            }
        ],
        loaders: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                loader: "babel-loader"
            },
            {
                test: /\.css$/,
                //配置css的抽取器、加载器。'-loader'可以省去
                loader: ExtractTextPlugin.extract('style-loader', 'css-loader')
            }, {
                //html模板加载器，可以处理引用的静态资源，默认配置参数attrs=img:src，处理图片的src引用的资源
                //比如你配置，attrs=img:src img:data-src就可以一并处理data-src引用的资源了，就像下面这样
                test: /\.html$/,
                loader: "html?attrs=img:src img:data-src"
            }, {
                //文件加载器，处理文件静态资源
                test: /\.(woff|woff2|ttf|eot|svg|otf)(\?v=[0-9]\.[0-9]\.[0-9])?$/,
                loader: 'file-loader?name=./fonts/[name].[ext]'
            }, {
                //图片加载器，雷同file-loader，更适合图片，可以将较小的图片转成base64，减少http请求
                //如下配置，将小于8192byte的图片转成base64码
                test: /\.(png|jpg|gif)$/,
                loader: 'url-loader?root=./&limit=8192&name=./img/[hash].[ext]'
            }
            ]
    },
    plugins: [
        new webpack.ProvidePlugin({ //加载jq
            $: 'jquery'
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name: 'vendors', // 将公共模块提取，生成名为`vendors`的chunk
            chunks: pages, //提取哪些模块共有的部分
            minChunks: 2 // 提取至少2个模块共有的部分
        }),
        new ExtractTextPlugin('css/[name].css'), //单独使用link标签加载css并设置路径，相对于output配置中的publickPath
        new webpack.HotModuleReplacementPlugin() //热加载
    ].concat(htmls),
    //使用webpack-dev-server，提高开发效率
    devServer: {
        host: '0.0.0.0',
        port: 9090,
        inline: true,
        hot: true,
        historyApiFallback: true
    }
};
