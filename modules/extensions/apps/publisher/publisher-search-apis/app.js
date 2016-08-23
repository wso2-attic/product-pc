app.server = function(ctx) {
    return {
        endpoints: {
            apis: [{
                url:'search',
                path:'search.jag',
                secured:true
            }]
        }
    }
};

app.apiHandlers = function(ctx) {
    return {
        onApiLoad: function() {
            if ((ctx.isAnonContext) && (ctx.endpoint.secured)) {
                //ctx.res.status='401';//sendRedirect(ctx.appContext+'/login');
                print('{ error:"Authentication error" }'); //TODO: Fix this to return a proper status code
                return false;
            }
            return true;
        }
    };
};