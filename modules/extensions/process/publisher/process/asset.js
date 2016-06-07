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

asset.server = function (ctx) {
    var type = ctx.type;
    return {
        onUserLoggedIn: function () {
        },
        endpoints: {
            apis: [{
                url: 'assets',
                path: 'assets.jag'
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
            }]
        }
    }
};

asset.renderer = function (ctx) {
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
            navList.push('Add ' + type, 'btn-add-new', util.buildUrl('create'));
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

        navList.push('Overview', 'btn-overview', util.buildUrl('details') + '/' + id);
        if ((isLCViewEnabled) && (isAssetWithLifecycle(page.assets))) {
            if (permissionAPI.hasAssetPermission(permissionAPI.ASSET_LIFECYCLE, ctx.assetType, ctx.session)) {
                navList.push('Life Cycle', 'btn-lifecycle', util.buildUrl('lifecycle') + '/' + id);
            }
        }
        if (permissionAPI.hasActionPermissionforPath(path, 'delete', ctx.session)) {
            navList.push('Delete', 'btn-delete', util.buildUrl('delete') + '/' + id);
        }
        return navList.list();
    };

    var buildAddLeftNav = function(page, util) {
        return [];
    };

    return {
        details: function (page) {
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

            var flowchartPath = page.assets.tables[7].fields.path.value;
            if(flowchartPath != "NA"){
                page.flowchartAvailable = true;
                page.flowchartPath = flowchartPath;
            }else{
                page.flowchartAvailable = false;
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
            }
        }
    };
};

asset.manager = function(ctx) {
    return {
        remove: function (options) {
            var log=new Log("rxt.asset");
            var processUUID = options;
            var processObj= this.get(processUUID);
            var processName = this.getName(processObj);
            var processVersion = this.getVersion(processObj);

            try {
                importPackage(org.wso2.carbon.pc.core);
                var ps = new ProcessStore();
                ps.deleteProcessRelatedArtifacts(processName, processVersion);
                this._super.remove.call(this, options);
            }catch (e){
                log.error(e.message);
            }
        }
    };
};

