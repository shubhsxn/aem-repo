/* eslint-disable */
const globImporter = require('node-sass-glob-importer');

const webpack = require('webpack'),
    path = require('path'),
    MiniCssExtractPlugin = require('mini-css-extract-plugin'),
    ExtractTextPlugin = require('extract-text-webpack-plugin'),
    UglifyJsPlugin = require('uglifyjs-webpack-plugin'),
    BUILD_DIR = path.resolve(__dirname, 'src/main/content/jcr_root/apps/brevant/components/clientlib/toplib/css'),
    cortevaConfig = require('./config-brevant'),
    env = process.env.NODE_ENV;

let config = {
    mode: env || 'development',
    entry: cortevaConfig,
    output: {
        path: BUILD_DIR,
        filename: '[name].js',
        publicPath: ''
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
                options: {
                    babelrc: false,
                    presets: ['babel-preset-env'],
                    plugins: ['syntax-dynamic-import']
                },
            },
            {
                test: /\.js$/,
                use: 'eslint-loader',
                enforce: 'pre',
                exclude: /node_modules/
            },
            {
                test: /\.(jpe?g|gif|png)$/,
                loader: 'file-loader?name=[name].[ext]&outputPath=../resources/images/',
            },
            {
                test: /\.(svg)$/,
                loader: 'file-loader?name=[name].[ext]&outputPath=../resources/vectors/',
            },
            {
                test: /\.(woff(2)?|ttf|eot)$/,
                loader: 'file-loader?name=[name].[ext]&outputPath=../resources/fonts/',
            },
            {
                test: /\.(css|sass|scss)$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    {
                        loader: 'sass-loader',
                        options: { importer: globImporter() },
                    }],

            }
        ]
    },
    devServer: {
        //open: true,
        contentBase: BUILD_DIR,
        //hot: true,
        //compress: true
    },
    plugins: [
        //new webpack.HotModuleReplacementPlugin(),
        new MiniCssExtractPlugin({
            filename: 'style.css',
            allChunks: true
        }),
        new UglifyJsPlugin()
    ]
}

module.exports = config;