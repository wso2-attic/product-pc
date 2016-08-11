app.dependencies = ['store-common'];
app.server = function(ctx) {
    return {
        endpoints: {
            pages: [{
                title: 'Publisher | Servicex Page',
                url: 'servicex_global',
                path: 'servicex_global.jag'
            }, {
                title: 'Publisher | Servicex Splash page',
                url: 'splash',
                path: 'servicex_splash.jag'
            }]
        },
        configs:{
            title : "WSO2 Process Center - Explorer",
            landingPage:'/assets/process/list',
            disabledAssets:['package','gadget','site','ebook', 'api', 'wsdl', 'service','policy','proxy','schema','sequence','servicex','uri','wadl','endpoint','swagger','restservice','comments','soapservice']
        }
    }
};