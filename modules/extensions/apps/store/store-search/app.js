app.dependencies=['store-common'];

app.server = function(ctx) {
    return {
        endpoints: {
            pages: [{
                title: 'Store | Advanced Search',
                url: 'advanced-search',
                path: 'advanced-search.jag',
                secured:true
            }]
        }
    }
};

app.renderer = function(ctx) {
    var decoratorApi = require('/modules/page-decorators.js').pageDecorators;
    return {
        pageDecorators: {
            navigationBar: function(page) {
                return decoratorApi.navigationBar(ctx, page, this);
            }
        }
    }
};