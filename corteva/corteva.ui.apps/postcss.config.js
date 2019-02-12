module.exports = {
    parser: 'sugarss',
     plugins: {
        'postcss-assets': {},
        'postcss-functions': {functions: {
        darken: function (value, frac) {
           var darken = 1 - parseFloat(frac);
           var rgba = color(value).toRgbaArray();
           var r = rgba[0] * darken;
           var g = rgba[1] * darken;
           var b = rgba[2] * darken;
           return color([r,g,b]).toHexString();
         },
         rem: function(val){
           return (parseInt(val, 10)/16)+'rem';
         }
        }
        },
        'postcss-mixins': {},
        'postcss-for': {},
        'postcss-simple-vars': {},
        'postcss-apply': {},
        'postcss-custom-media': {},
        'postcss-color-function': {},
        'postcss-map': {},
        'postcss-media-minmax': {},
        'postcss-nested': {},
        'postcss-percentage': {},
        'postcss-responsive-type': {},
        'postcss-cssnext': {},
        'postcss-easy-import': {},
        'precss': {},
        'postcss-calc': {},
        'postcss-discard-comments': ({ removeAll: true }),
        'cssnano': {}
    }
}
