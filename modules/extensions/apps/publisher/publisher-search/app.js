app.dependencies=['publisher-common'];

app.server = function(ctx) {
    return {
        endpoints: {
            pages: [{
                title: 'Publisher | Advanced Search',
                url: 'advanced-search',
                path: 'advanced-search.jag'
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