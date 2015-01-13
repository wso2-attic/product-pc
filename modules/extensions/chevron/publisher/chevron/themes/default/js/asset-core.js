var asset = {};

(function (asset) {
    asset.process = function (type, path, destination) {
        if(!store.user) {
            $('#modal-login').modal('show');
            return;
        }
        location.href = caramel.context + '/extensions/assets/' + type + '/chevron?asset=' + path + '&destination=' + encodeURIComponent(location.href);
    };
}(asset));