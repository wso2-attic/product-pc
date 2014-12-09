var resources = function (page, meta) {
    return {
        js: ['jquery.MetaData.js', 'jquery.rating.pack.js', 'async.min.js', 'asset-core.js', 'asset.js', 'moment.min.js', 'porthole.min.js','editablegrid-2.0.1.js'],
        css: ['jquery.rating.css', 'asset.css','editablegrid-2.0.1.css'],
        code: ['store.asset.hbs']
    };
};

var assetLinks = function (user) {
    return {
        title: 'Process'
    };
};


asset.server = function(ctx) {
    var type = ctx.type;
    return {
        onUserLoggedIn: function() {},
        endpoints: {
            apis: [{
                url: 'content',
                path: 'content.jag'
            },{
                url: 'processes',
                path: 'processes.jag'
            }]
        }
    };
};
