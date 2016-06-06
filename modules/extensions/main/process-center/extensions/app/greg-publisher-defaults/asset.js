/*
 *  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.w   See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
// asset.manager = function(ctx) {
//  return {
//      get:function(id){
//          log.info('overridden get method in the GREG default extension');
//          return this._super.get.call(this,id);
//      }
//  }
// };
asset.renderer = function(ctx) {
    var gregAPI = require('/modules/asset-action-api.js').api;
    var rxt = require('rxt');
    var assetManager = function(session, type) {
        var am = rxt.asset.createUserAssetManager(session, type);
        return am;
    };
    var getAssetNoteManager = function (ctx) {
        var rxt = require('rxt');
        var am = rxt.asset.createUserAssetManager(ctx.session, 'note');
        return am;
    };
    return {
        pageDecorators: {
            permissionMetaDataPopulator: function(page, util) {
                var ptr = page.leftNav || [];
                var am = assetManager(ctx.session,ctx.assetType);
                var entry;
                var allowedPages = ['details','lifecycle','update','associations','permissions'];
                log.debug('Permission populator ' + page.meta.pageName);
                if(allowedPages.indexOf(page.meta.pageName)>-1){
                    var permissionList = gregAPI.permissions.list(am, page.assets.id);
                    if(permissionList){
                        if(permissionList.isAuthorizeAllowed && !permissionList.isVersionView){
                            log.debug('adding link');
                            entry = {};
                            entry.name = 'Permissions';
                            entry.iconClass = 'btn-lifecycle';
                            entry.url = this.buildAppPageUrl('permissions') + '/' + page.assets.type + '/'
                                + page.assets.id;
                            ptr.push(entry);
                        }
                    }
                }
            }
        }
    };
};
asset.configure = function() {
    return {
        meta: {
            lifecycle: {
                commentRequired: false,
                defaultAction: '',
                deletableStates: ['*'],
                defaultLifecycleEnabled: false,
                publishedStates: ['Published']
            },
            ui:{
                icon:'fw fw-resource'
            },
            grouping: {
                groupingEnabled: false,
                groupingAttributes: ['overview_name']
            }
        }
    };
};

