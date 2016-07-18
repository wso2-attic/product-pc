/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

asset.server = function(ctx) {
    var type = ctx.type;
    return {
        endpoints: {
            apis: [{
                        url: 'packages',
                        path: 'packages.jag'
                   },
                   {
                        url: 'deployments',
                        path: 'deploy.jag'
                   }],
            pages: [{
                        title: 'Processes',
                        url: 'processes',
                        path: 'processes.jag'
                    },
                    {
                        title: 'Deploy',
                        url: 'deploy',
                        path: 'deploy.jag'
                    }]
        }
    };
};
asset.renderer = function(ctx) {
    var type = ctx.assetType;
    var permissionAPI = require('rxt').permissions;
    var isAssetWithLifecycle = function(asset) {
        if ((asset.lifecycle) && (asset.lifecycleState)) {
            return true;
        }
        if (log.isDebugEnabled()) {
            log.debug('asset: ' + asset.name + ' does not have a lifecycle or a state.The lifecycle view will not be rendered for this asset');
        }
        return false;
    };
    var buildListLeftNav = function(page, util) {
        var navList = util.navList();
        if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_CREATE, ctx.assetType, ctx.session)) {
            navList.push('Add ', 'btn-add-new', util.buildUrl('create'));
        }
        //navList.push('Configuration', 'icon-dashboard', util.buildUrl('configuration'));
        return navList.list();
    };
    var buildDefaultLeftNav = function(page, util) {
        var id = page.assets.id;
        var path = page.assets.path;
        var navList = util.navList();
        var isLCViewEnabled = ctx.rxtManager.isLifecycleViewEnabled(ctx.assetType);
        var user = require('store').server.current(session);
        var username = user? user.username : null;
        //navList.push('Overview', 'btn-overview', util.buildUrl('details') + '/' + id);
        if (permissionAPI.hasActionPermissionforPath(path, 'write', ctx.session) && permissionAPI.hasAssetPagePermission(type,'update',user.tenantId,username)) {
            navList.push('Edit', 'btn-edit', util.buildUrl('update') + '/' + id);
        }
        if (permissionAPI.hasActionPermissionforPath(path, 'delete', ctx.session)) {
            navList.push('Delete', 'btn-delete', util.buildUrl('delete') + '/' + id);
        }
        //Only render the view if the asset has a
        /** Package does not have any life cycle.
        if ((isLCViewEnabled) && (isAssetWithLifecycle(page.assets))) {
            if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_LIFECYCLE, ctx.assetType, ctx.session)) {
                navList.push('Lifecycle', 'btn-lifecycle', util.buildUrl('lifecycle') + '/' + id);
            }
        }
        **/
        if (permissionAPI.hasActionPermissionforPath(path, 'write', ctx.session) && permissionAPI.hasAssetPagePermission(type,'update',user.tenantId,username)) {
           navList.push('Version', 'btn-copy', util.buildUrl('copy') + '/' + id);
           navList.push('Processes', 'btn-copy', util.buildUrl('processes') + '/' + id);
           navList.push('Deploy', 'btn-delete', util.buildUrl('deploy') + '/' + id);
        }

        return navList.list();
    };
    var buildAddLeftNav = function(page, util) {
        return [];
    };
    var isActivatedAsset = function(assetType) {
        var app = require('rxt').app;
        var activatedAssets = app.getUIActivatedAssets(ctx.tenantId); //ctx.tenantConfigs.assets;
        //return true;
        if (!activatedAssets) {
            throw 'Unable to load all activated assets for current tenant: ' + ctx.tenatId + '.Make sure that the assets property is present in the tenant config';
        }
        for (var index in activatedAssets) {
            if (activatedAssets[index] == assetType) {
                return true;
            }
        }
        return false;
    };
    return {
        list: function(page) {
            var assets = page.assets;
            for (var index in assets) {
                var asset = assets[index];
                var timestampAttribute = ctx.rxtManager.getTimeStampAttribute(ctx.assetType);
                if (asset.attributes.hasOwnProperty(timestampAttribute)) {
                    var value = asset.attributes[timestampAttribute];
                    var date = new Date();
                    date.setTime(value);
                    asset.attributes[timestampAttribute] = date.toUTCString();
                }
            }
            require('/modules/page-decorators.js').pageDecorators.assetCategoryDetails(ctx, page, this);
        },
        details: function(page) {
            var tables = page.assets.tables;
            //TODO:This cannot be hardcoded
            var timestampAttribute = 'createdtime'; //ctx.rxtManager.getTimeStampAttribute(this.assetType);
            for (var index in tables) {
                var table = tables[index];
                if ((table.name == 'overview') && (table.fields.hasOwnProperty(timestampAttribute))) {
                    var value = table.fields[timestampAttribute].value || '';
                    var date = new Date();
                    date.setTime(value);
                    table.fields[timestampAttribute].value = date.toUTCString();
                }
            }
        },
        create: function(page) {
            var tables = page.assets.tables;
            var providerAttribute = 'provider'; //TODO: Provider should be picked up from the provider attribute
            for (var index in tables) {
                var table = tables[index];
                if ((table.name == 'overview') && (table.fields.hasOwnProperty(providerAttribute))) {
                    table.fields[providerAttribute].value = page.cuser.cleanedUsername;
                }
            }
        },
        update: function(page) {
            var tables = page.assets.tables;
            var timestampAttribute = 'createdtime';
            for (var index in tables) {
                var table = tables[index];
                if ((table.name == 'overview') && (table.fields.hasOwnProperty(timestampAttribute))) {
                    var value = table.fields[timestampAttribute].value;
                    var date = new Date();
                    date.setTime(value);
                    table.fields[timestampAttribute].value = date.toUTCString();
                }
            }
        },
        pageDecorators: {
            leftNav: function(page) {
                if (log.isDebugEnabled()) {
                    log.debug('Using default leftNav');
                }
                switch (page.meta.pageName) {
                    case 'list':
                        page.leftNav = buildListLeftNav(page, this);
                        break;
                    case 'create':
                        page.leftNav = buildAddLeftNav(page, this);
                        break;
                    case 'statistics':
                        page.leftNav = buildListLeftNav(page, this);
                        break;
                    default:
                        page.leftNav = buildDefaultLeftNav(page, this);
                        break;
                }
                if (page.leftNav) {
                    for (var navItem in page.leftNav) {
                        if (page.leftNav[navItem].name) {
                            page.leftNav[navItem].id = page.leftNav[navItem].name.replace(/\s/g, "");
                        }
                    }
                }
                return page;
            },
            ribbon: function(page) {
                var ribbon = page.ribbon = {};
                var DEFAULT_ICON = 'fw fw-circle';
                var assetTypes = [];
                var assetType;
                var assetList = ctx.rxtManager.listRxtTypeDetails();
                for (var index in assetList) {
                    assetType = assetList[index];
                    //Only populate the link if the asset type is activated and the logged in user has permission to that asset
                    if ((isActivatedAsset(assetType.shortName)) && (permissionAPI.hasAssetPermission(permissionAPI.ASSET_LIST, assetType.shortName, ctx.session))) {
                        assetTypes.push({
                            url: this.buildBaseUrl(assetType.shortName) + '/list',
                            assetIcon: assetType.ui.icon || DEFAULT_ICON,
                            assetTitle: assetType.pluralLabel
                        });
                    }
                }
                ribbon.currentType = page.rxt.singularLabel;
                ribbon.currentTitle = page.rxt.singularLabel;
                ribbon.currentUrl = this.buildBaseUrl(type) + '/list'; //page.meta.currentPage;
                ribbon.shortName = page.rxt.singularLabel;
                ribbon.query = 'Query';
                ribbon.breadcrumb = assetTypes;
                return page;
            },
            getStoreUrl: function (page) {
                page.storeUrl = require('/config/publisher.js').config().storeUrl;
                return page;
            },
            populateAttachedLifecycles: function(page) {
                if (page.assets.id) {
                    require('/modules/page-decorators.js').pageDecorators.populateAttachedLifecycles(ctx, page, this);
                }
            },
            populateAssetVersionDetails: function(page) {
                if (page.assets.id) {
                    require('/modules/page-decorators.js').pageDecorators.populateAssetVersionDetails(ctx, page, this);
                }
            },
            populateGroupingFeatureDetails: function(page) {
                require('/modules/page-decorators.js').pageDecorators.populateGroupingFeatureDetails(ctx, page);
            },
            populateTags: function(page){
                if(page.assets.id){
                    require('/modules/page-decorators.js').pageDecorators.populateTagDetails(ctx,page);
                }
            },
            sorting: function(page){
                require('/modules/page-decorators.js').pageDecorators.sorting(ctx,page);
            },
            hideEmptyTables:function(page){
                if(page.meta.pageName !=='details'){
                    return;
                }
                require('/modules/page-decorators.js').pageDecorators.hideEmptyTables(ctx,page,this);
            },
            populateBreadcrumb:function(page){
                require('/modules/page-decorators.js').pageDecorators.populateAssetPageBreadcrumb(ctx,page,this);
            }
        }
    };
};

asset.manager = function(ctx) {
    return {
        remove: function (options) {
            var log = new Log("rxt.asset");
            var packageUUID = options;
            var packageObj= this.get(packageUUID);
            var packageName = this.getName(packageObj);
            var packageVersion = this.getVersion(packageObj);

            var server = require('store').server;
            var user = server.current(session);
            var username = user? user.username : null;
            try {
                importPackage(org.wso2.carbon.pc.core.assets);
                var packageInstance = Package();
                packageInstance.delete(packageName, packageVersion, username);
                this._super.remove.call(this, options);
            }catch (e){
                log.error(e.message);
            }
        }
    };
};
