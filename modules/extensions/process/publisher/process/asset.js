/*
 * Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
asset.server = function(ctx) {
    var type = ctx.assetType;
    var typeDetails = ctx.rxtManager.listRxtTypeDetails(type);
    var typeSingularLabel = type; //Assume the type details are not returned
    var pluralLabel = type; //Assume the type details are not returned
    if (typeDetails) {
        typeSingularLabel = typeDetails.singularLabel;
        pluralLabel = typeDetails.pluralLabel;
    }
    return {
        onUserLoggedIn: function() {},
        endpoints: {
            apis: [{
                       url: 'assets',
                       path: 'assets.jag'
                   }, {
                       url: 'asset',
                       path: 'asset.jag'
                   }, {
                       url: 'statistics',
                       path: 'statistics.jag'
                   }, {
                       url: 'create_process',
                       path: 'create_process.jag'
                   }, {
                       url: 'upload_bpmn',
                       path: 'upload_bpmn.jag'
                   }, {
                       url: 'get_process_text',
                       path: 'get_process_text.jag'
                   }, {
                       url: 'get_bpmn_content',
                       path: 'get_bpmn_content.jag'
                   }, {
                       url: 'save_process_text',
                       path: 'save_process_text.jag'
                   }, {
                       url: 'asset',
                       path: 'asset.jag'
                   }, {
                       url: 'statistics',
                       path: 'statistics.jag'
                   }, {
                       url: 'get_process_list',
                       path: 'get_process_list.jag'
                   }, {
                       url: 'update_subprocess',
                       path: 'update_subprocess.jag'
                   }, {
                       url: 'update_successor',
                       path: 'update_successor.jag'
                   }, {
                       url: 'update_predecessor',
                       path: 'update_predecessor.jag'
                   }, {
                       url: 'delete_subprocess',
                       path: 'delete_subprocess.jag'
                   }, {
                       url: 'delete_successor',
                       path: 'delete_successor.jag'
                   }, {
                       url: 'delete_Predecessor',
                       path: 'delete_Predecessor.jag'
                   }, {
                       url: 'update_process',
                       path: 'update_process.jag'
                   }, {
                       url: 'update_owner',
                       path: 'update_owner.jag'
                   }, {
                       url: 'upload_documents',
                       path: 'upload_documents.jag'
                   }, {
                       url: 'get_process_tags',
                       path: 'get_process_tags.jag'
                   }, {
                       url: 'upload_flowchart',
                       path: 'upload_flowchart.jag'
                   }, {
                       url: 'get_process_flowchart',
                       path: 'get_process_flowchart.jag'
                   }, {
                       url: 'get_process_doc',
                       path: 'get_process_doc.jag'
                   }, {
                       url: 'download_document',
                       path: 'download_document.jag'
                   }, {
                       url: 'delete_flowchart',
                       path: 'delete_flowchart.jag'
                   }, {
                       url: 'delete_document',
                       path: 'delete_document.jag'
                   }, {
                       url: 'delete_bpmn',
                       path: 'delete_bpmn.jag'
                   }, {
                       url: 'update_description',
                       path: 'update_description.jag'
                   }, {
                       url: 'audit_log',
                       path: 'audit_log.jag'
                   }, {
                       url: 'get_role_permission',
                       path: 'get_role_permission.jag'
                   }, {
                       url: 'export_process',
                       path: 'export_process.jag'
                   }, {
                       url: 'import_process',
                       path: 'import_process.jag'
                   }, {
                        url: 'update_document_details',
                        path: 'update_document_details.jag'
                   }, {
                        url: 'config_das_analytics',
                        path: 'config_das_analytics.jag'
                   }, {
                        url: 'save_process_variables',
                        path: 'save_process_variables.jag'
                   }, {
                        url: 'get_process_variables',
                        path: 'get_process_variables.jag'
                   }, {
                        url: 'get_process_variables_list',
                        path: 'get_process_variables_list.jag'
                   },{
                        url: 'get_process_deployed_id',
                        path: 'get_process_deployed_id.jag'
                   }
            ],
            pages: [{
                        title: 'Asset: ' + typeSingularLabel,
                        url: 'asset',
                        path: 'asset.jag'
                    }, {
                        title: 'Assets ' + typeSingularLabel,
                        url: 'assets',
                        path: 'assets.jag'
                    }, {
                        title: 'Create ' + typeSingularLabel,
                        url: 'create',
                        path: 'create.jag',
                        permission: 'ASSET_CREATE'
                    }, {
                        title: 'Update ' + typeSingularLabel,
                        url: 'update',
                        path: 'update.jag',
                        permission: 'ASSET_UPDATE'
                    }, {
                        title: 'Details ' + typeSingularLabel,
                        url: 'details',
                        path: 'details.jag'
                    }, {
                        title: 'List ' + pluralLabel,
                        url: 'list',
                        path: 'list.jag',
                        permission: 'ASSET_LIST'
                    }, {
                        title: 'Lifecycle',
                        url: 'lifecycle',
                        path: 'lifecycle.jag',
                        permission: 'ASSET_LIFECYCLE'
                    }, {
                        title: 'Old lifecycle ',
                        url: 'old-lifecycle',
                        path: 'old-lifecycle.jag'
                    }, {
                        title: 'Statistics',
                        url: 'statistics',
                        path: 'statistics.jag'
                    }, {
                        title: 'Copy ' + typeSingularLabel,
                        url: 'copy',
                        path: 'copy.jag',
                        permission: 'ASSET_CREATE'
                    }, {
                        title: 'Delete ' + typeSingularLabel,
                        url: 'delete',
                        path: 'delete.jag'
                    },{
                        title: 'Log: ',
                        url: 'log',
                        path: 'log.jag'
                    },{
                        title: 'Import Process: ',
                        url: 'import_process',
                        path: 'import_process.jag'
                    },{
                        title: 'Configure Analytics: ',
                        url: 'config_analytics',
                        path: 'config_analytics.jag'
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
            navList.push('Import Process', 'btn-import', util.buildUrl('import_process'));
            navList.push('Audit Log', 'btn-auditlog', util.buildUrl('log'));
        }
        //navList.push('Configuration', 'icon-dashboard', util.buildUrl('configuration'));
        return navList.list();
    };
    var importProcessLeftNav = function(page, util) {
        var navList = util.navList();
        if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_CREATE, ctx.assetType, ctx.session)) {
            navList.push('Add ', 'btn-add-new', util.buildUrl('create'));
            navList.push('Import Process', 'btn-import', util.buildUrl('import_process'));
            navList.push('Audit Log', 'btn-overview', util.buildUrl('log'));
        }
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

        //Only render the view if the asset has a
        if ((isLCViewEnabled) && (isAssetWithLifecycle(page.assets))) {
            if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_LIFECYCLE, ctx.assetType, ctx.session)) {
                navList.push('Lifecycle', 'btn-lifecycle', util.buildUrl('lifecycle') + '/' + id);
            }
        }

        if (permissionAPI.hasActionPermissionforPath(path, 'delete', ctx.session)) {
            navList.push('Delete', 'btn-delete', util.buildUrl('delete') + '/' + id);
        }
        navList.push('Config Analytics', 'btn-configAnalytics', util.buildUrl('config_analytics') + '/' + id);
        navList.push('Audit Log', 'btn-auditlog', util.buildUrl('log') + '/' + id);
        //if (permissionAPI.hasActionPermissionforPath(path, 'write', ctx.session) && permissionAPI.hasAssetPagePermission(type,'update',user.tenantId,username)) {
        //navList.push('Version', 'btn-copy', util.buildUrl('copy') + '/' + id);
        //}

        return navList.list();
    };
    var buildLogNav = function (page, util) {
        var navList = util.navList();
        var isLCViewEnabled = ctx.rxtManager.isLifecycleViewEnabled(ctx.assetType);
        var path = page.assets.path;
        if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_CREATE, ctx.assetType, ctx.session)) {
            //    log.info(page.assets);
            if(page.assets.id != null) {
                navList.push('Edit', 'btn-edit', util.buildUrl('update') + '/' + page.assets.id);
                if ((isLCViewEnabled) && (isAssetWithLifecycle(page.assets))) {
                    if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_LIFECYCLE, ctx.assetType, ctx.session)) {
                        navList.push('Life Cycle', 'btn-lifecycle', util.buildUrl('lifecycle') + '/' + page.assets.id);
                    }
                }
                if (permissionAPI.hasActionPermissionforPath(path, 'delete', ctx.session)) {
                    navList.push('Delete', 'btn-delete', util.buildUrl('delete') + '/' + page.assets.id);
                }
                navList.push('Config Analytics', 'btn-configAnalytics', util.buildUrl('config_analytics') + '/' + page.assets.id);
                navList.push('Audit Log', 'btn-auditlog', util.buildUrl('log') + '/' +page.assets.id);

            } else {
                navList.push('Add ', 'btn-add-new', util.buildUrl('create'));
                navList.push('Import Process', 'btn-import', util.buildUrl('import_process'));
                navList.push('Audit Log', 'btn-auditlog', util.buildUrl('log'));
            }
        }
        return navList.list();
    };
    var buildAddLeftNav = function(page, util) {
        var navList = util.navList();
        navList.push('Processes', 'btn-stats', util.buildUrl('list'));
        return navList.list();
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
            var log = new Log();
            var resourcePath = page.assets.path;
            if (log.isDebugEnabled()) {
                log.debug(resourcePath);
            }
            if (page.assets.tables[1].fields.processtextpath.value == "NA") {
                page.processTextAvaliable = false;
            } else {
                page.processTextAvaliable = true;
            }

            if (page.assets.tables[1].fields.bpmnid.value == "NA") {
                page.bpmnAvaliable = false;
            } else {
                page.bpmnAvaliable = true;
            }

            importPackage(org.wso2.carbon.pc.core);
            var ps = new ProcessStore();
            var isDocumentAvailableStr = ps.isDocumentAvailable(resourcePath);
            var isDocumentAvailable = JSON.parse(isDocumentAvailableStr);

            if (isDocumentAvailable == true) {
                page.documentAvailable = true;
            } else {
                page.documentAvailable = false;
            }

            var conData = ps.getSucessorPredecessorSubprocessList(resourcePath);
            var conObject = JSON.parse(conData);
            if (log.isDebugEnabled()) {
                log.debug(conObject);
            }
            page.involveProcessList = conObject;
            if (log.isDebugEnabled()) {
                log.debug(page);
            }

            var flowchartPath = page.assets.tables[8].fields.path.value;
            if(flowchartPath != "NA"){
                page.flowchartAvailable = true;
                page.flowchartPath = flowchartPath;
            }else{
                page.flowchartAvailable = false;
            }

            var permissionAPI = require('rxt').permissions;
            if (permissionAPI.hasActionPermissionforPath(resourcePath , 'write', ctx.session)){
                page.permission=true;
            }
            else{
                page.permission=false;
            }

            var thumbnail = page.assets.tables[7].fields.thumbnail.value;
            if (thumbnail === "images_thumbnail") {
                page.customThumbnailAvailable = true;
            } else {
                page.customThumbnailAvailable = false;
            }

            var processName = page.assets.tables[0].fields.name.value; //tables[0].fields["Name"].value;
            var processVersion = page.assets.tables[0].fields.version.value;
            page.processName = processName;
            page.processVersion = processVersion;

            try {

                var processTags = ps.getProcessTags(processName, processVersion);
                var ps = new ProcessStore();
                page.processTagsArray = processTags.split("###");
            } catch (e) {
                log.error("Error in retrieving process tags. Exception:" + e);
            }

            importPackage(org.wso2.carbon.pc.analytics.core.generic.utils);
            page.DASAnalyticsEnabled = AnalyticsUtils.isDASAnalyticsActivated();
            importPackage(org.wso2.carbon.pc.analytics.core.kpi.utils);
            page.DASAnalyticsConfigured = DASConfigurationUtils.isDASAnalyticsConfigured(processName, processVersion);

            if (page.DASAnalyticsConfigured) {
                var processVariablesJObArrStr = ps.getProcessVariablesList(resourcePath);
                var processVariablesJObArr = JSON.parse(processVariablesJObArrStr);
                page.processVariableList = processVariablesJObArr;
                var streamAndReceiverInfo = JSON.parse(ps.getStreamAndReceiverInfo(resourcePath));

                page.eventStreamName = streamAndReceiverInfo["eventStreamName"];
                page.eventStreamVersion = streamAndReceiverInfo["eventStreamVersion"];
                page.eventStreamDescription = streamAndReceiverInfo["eventStreamDescription"];
                page.eventStreamNickName = streamAndReceiverInfo["eventStreamNickName"];
                page.eventReceiverName = streamAndReceiverInfo["eventReceiverName"];
                page.processDefinitionId = streamAndReceiverInfo["processDefinitionId"];
            }

        },
        create: function(page) {
            var tables = page.assets.tables;
            var providerAttribute = 'provider'; //TODO: Provider should be picked up from the provider attribute
            var processTextPathAttribute = 'processtextpath';
            var bpmnPathAttribute = 'bpmnpath';
            for (var index in tables) {
                var table = tables[index];
                if ((table.name == 'overview') && (table.fields.hasOwnProperty(providerAttribute))) {
                    table.fields[providerAttribute].value = page.cuser.cleanedUsername;
                }

                if ((table.name == 'properties') && (table.fields.hasOwnProperty(processTextPathAttribute))) {
                    var processTextField = table.fields[processTextPathAttribute].value;
                    var bpmnPathField = table.fields[bpmnPathAttribute].value;
                    page.isProcessTextAvailable = false;
                    page.isBpmnAvailable = false;

                    if(processTextField == 'NA') {
                        page.isProcessTextAvailable = false;
                    } else if(processTextField != null) {
                        page.isProcessTextAvailable = true;
                    }

                    if(bpmnPathField == 'NA') {
                        page.isBpmnAvailable = false;
                    } else if(processTextField != null) {
                        page.isBpmnAvailable = true;
                    }
                }

                var resourcePath = page.assets.path;
                var permissionAPI = require('rxt').permissions;
                if (permissionAPI.hasActionPermissionforPath(resourcePath , 'write', ctx.session)){
                    page.permission=true;
                }
                else{
                    page.permission=false;
                }
            }
        },
        update: function(page) {
            var tables = page.assets.tables;
            var timestampAttribute = 'createdtime';
            var processTextPathAttribute = 'processtextpath';
            var bpmnPathAttribute = 'bpmnpath';
            var documentPathNameAttribute = 'name';
            var flowchartPathAttribute = 'path';
            for (var index in tables) {
                var table = tables[index];
                if ((table.name == 'overview') && (table.fields.hasOwnProperty(timestampAttribute))) {
                    var value = table.fields[timestampAttribute].value;
                    var date = new Date();
                    date.setTime(value);
                    table.fields[timestampAttribute].value = date.toUTCString();
                }
                if ((table.name == 'properties') && (table.fields.hasOwnProperty(processTextPathAttribute))) {
                    var processTextField = table.fields[processTextPathAttribute].value;
                    var bpmnPathField = table.fields[bpmnPathAttribute].value;
                    page.isProcessTextAvailable = false;
                    page.isBpmnAvailable = false;

                    if(processTextField == 'NA') {
                        page.isProcessTextAvailable = false;
                    } else if(processTextField != null) {
                        page.isProcessTextAvailable = true;
                    }

                    if(bpmnPathField == 'NA') {
                        page.isBpmnAvailable = false;
                    } else if(processTextField != null) {
                        page.isBpmnAvailable = true;
                    }
                }
                if((table.name == 'document') && (table.fields["path"].hasOwnProperty("value"))) {
                    var documetName = table.fields["path"].value;
                    if(documetName == null) {
                        page.isDocumentAvailable = false;
                    } else {
                        page.isDocumentAvailable = true;
                    }
                }

                if((table.name == 'flowchart') && (table.fields.hasOwnProperty(flowchartPathAttribute))) {
                    var flowchartPath = table.fields[flowchartPathAttribute].value;
                    if(flowchartPath == 'NA') {
                        page.isFlowChartAvailable = false;
                    } else {
                        page.isFlowChartAvailable = true;
                        page.flowchartPath = flowchartPath;
                    }
                }
            }

            var thumbnail = page.assets.tables[7].fields.thumbnail.value;
            if (thumbnail === "images_thumbnail") {
                page.customThumbnailAvailable = true;
            } else {
                page.customThumbnailAvailable = false;
            }

            importPackage(org.wso2.carbon.pc.core);
            var ps = new ProcessStore();
            var resourcePath = page.assets.path;
            var conData = ps.getSucessorPredecessorSubprocessList(resourcePath);
            var conObject = JSON.parse(conData);
            if (log.isDebugEnabled()) {
                log.debug(conObject);
            }
            page.involveProcessList = conObject;
            if (log.isDebugEnabled()) {
                log.debug(page);
            }

            var permissionAPI = require('rxt').permissions;
            if (permissionAPI.hasActionPermissionforPath(resourcePath , 'write', ctx.session)){
                page.permission=true;
            }
            else{
                page.permission=false;
            }
            page.isUpdateView = true;
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
                    case 'log':
                        page.leftNav = buildLogNav(page, this);
                        break;
                    case 'statistics':
                        page.leftNav = buildListLeftNav(page, this);
                        break;
                    case 'import_process':
                        page.leftNav = importProcessLeftNav(page,this);
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
            var processUUID = options;
            var processObj= this.get(processUUID);
            var processName = this.getName(processObj);
            var processVersion = this.getVersion(processObj);

            var server = require('store').server;
            var user = server.current(session);
            var username = user? user.username : null;

            try {
                importPackage(org.wso2.carbon.pc.core);
                var ps = new ProcessStore();
                ps.deleteProcessRelatedArtifacts(processName, processVersion, username);
                this._super.remove.call(this, options);
            }catch (e){
                log.error(e.message);
            }
        }
    };
};
