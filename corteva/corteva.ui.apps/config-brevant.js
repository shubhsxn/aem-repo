/* eslint-disable */
/**
 * 'path' library
 * @type {posix}
 */
const path = require('path');
const common = require('./config-common');

/**
 * Sets the app directory to resolve to the content components
 */
const APP_DIR = path.resolve(__dirname, 'src/main/content/jcr_root/apps/corteva/components/content/');


let updatedCommon = {};
Object.keys(common).forEach(key => {
    if (key.startsWith('../../../')) {
        updatedCommon[key.replace('../../../', '../../../../../corteva/components/')] = common[key];
    }
});

/**
 * Extending the Corteva components with the Brevant specific components
 * @type {*}
 */
const config = Object.assign({}, updatedCommon, {
    'style': path.resolve(__dirname, 'src/main/content/jcr_root/apps/brevant/components/clientlib/toplib/styles/style.js'),
    '../../../clientlib/toplib/js/dist/app': APP_DIR + '../../../../brevant/components/clientlib/toplib/js/app.js',

    // Brevant specific components
});

module.exports = config;
