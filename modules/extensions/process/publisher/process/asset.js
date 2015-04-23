/*
*  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

var assetLinks = function(user) {
    return {
        title: 'Process'
    };
};

asset.manager = function(ctx) {
 return {
     create: function(options) {
         var ref = require('utils').time;
         var GovernanceConstants = Packages.org.wso2.carbon.governance.api.util;
         //Check if the options object has a createdtime attribute and populate it
         if ((options.attributes) && (options.attributes.hasOwnProperty('overview_createdtime'))) {
             options.attributes.overview_createdtime = ref.getCurrentTime();
         }
         this._super.create.call(this, options);
         var asset = this.get(options.id);

          //Adding associations for properties_predecessors
         if (asset.attributes.properties_predecessors != null) {
             var tempArray1 = asset.attributes.properties_predecessors.split("\,");

             for (var i = 0; i < tempArray1.length; i++) {

                 var preAsset = this.get(tempArray1[i]);
                 this.registry.associate(asset.path, preAsset.path, "Predecessors");
             }
         }
         //Adding associations for properties_specializations
         if (asset.attributes.properties_specializations != null) {
             var tempArray2 = asset.attributes.properties_specializations.split("\,");
             for (var i = 0; i < tempArray2.length; i++) {
                 var specAsset = this.get(tempArray2[i]);
                 this.registry.associate(asset.path, specAsset.path, "Specializations");
             }
         }
         //Adding associations for properties_generalizations
         if (asset.attributes.properties_generalizations != null) {
             var tempArray3 = asset.attributes.properties_generalizations.split("\,");
             for (var i = 0; i < tempArray3.length; i++) {
                 var genAsset = this.get(tempArray3[i]);
                 this.registry.associate(asset.path, genAsset.path, "Generalizations");
             }
         }
         //Adding associations for properties_successors
         if (asset.attributes.properties_successors != null) {
             var tempArray4 = asset.attributes.properties_successors.split("\,");
             for (var i = 0; i < tempArray4.length; i++) {
                 var sucAsset = this.get(tempArray4[i]);
                 this.registry.associate(asset.path, sucAsset.path, "Successors");
             }
         }
     }
 };
};

asset.server = function(ctx) {
    var type = ctx.type;
    return {
        onUserLoggedIn: function() {},
        endpoints: {
            apis: [{
                url: 'assets',
                path: 'assets.jag'
            }, {
                url: 'content',
                path: 'content.jag'
            },{
                url: 'processes',
                path: 'processes.jag'
            }],
            pages: [{
                title: 'Asset: ' + type,
                url: 'asset',
                path: 'asset.jag'
            }, {}, {
                title: 'Create ' + type,
                url: 'create',
                path: 'create.jag'
            }, {
                title: 'Update ' + type,
                url: 'update',
                path: 'update.jag'
            }, {
                title: 'Details ' + type,
                url: 'details',
                path: 'details.jag'
            }, {
                title: 'List ' + type,
                url: 'list',
                path: 'list.jag'
            }, {
                title: 'Lifecycle',
                url: 'lifecycle',
                path: 'lifecycle.jag'
            }]
        }
    };
};
