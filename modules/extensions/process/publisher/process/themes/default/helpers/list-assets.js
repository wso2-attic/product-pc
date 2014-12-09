var log=new Log();
log.info('######### Loading the list-assets helper ######## ');
var resources = function (block, page, area, meta) {
    return {
        js: ['asset-core.js', 'asset-helpers.js', 'assets.js','jquery.event.mousestop.js'],
        css: ['es-publisher-commons.css','assets.css','grid.css','styles.css','font-awesome-ie7.min.css','jslider.css','jslider.round.plastic.css','navigation.css','sort-assets.css'],
    };
};

var currentPage = function (assetsx,ssox,userx, paging) {
    var outx  = {
        'assets': assetsx,
        'sso': ssox,
        'user': userx
    };
    return outx;
};

var format = function (fields) {
    fields.searchFields.forEach(function (field) {
        field.field_name = field.field_name.toLocaleLowerCase();
    });
    return fields;
};

