   /*
 * Copyright (c) WSO2 Inc, 2015. (http://wso2.com) All Rights Reserved.
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
             //Adding association for Predecessors
            if (asset.attributes.properties_predecessors != null) {
                var listOfPredecessors = asset.attributes.properties_predecessors.split("\,");
                for (var i = 0; i < listOfPredecessors.length; i++) {
                    var preAsset = this.get(listOfPredecessors[i]);
                    this.registry.associate(asset.path, preAsset.path, "Predecessors");
                }
            }
            //Adding association for properties_specializations
            if (asset.attributes.properties_specializations != null) {
                var listOfSpecializations = asset.attributes.properties_specializations.split("\,");
                for (var i = 0; i < listOfSpecializations.length; i++) {
                    var specAsset = this.get(listOfSpecializations[i]);
                    this.registry.associate(asset.path, specAsset.path, "Specializations");
                }
            }
            //Adding association for properties_generalizations
            if (asset.attributes.properties_generalizations != null) {
                var listOfGeneralizations = asset.attributes.properties_generalizations.split("\,");
                for (var i = 0; i < listOfGeneralizations.length; i++) {
                    var genAsset = this.get(listOfGeneralizations[i]);
                    this.registry.associate(asset.path, genAsset.path, "Generalizations");
                }
            }
            //Adding association for properties_successor
            if (asset.attributes.properties_sucessors != null) {
                var listOfSucessors = asset.attributes.properties_sucessors.split("\,");
                for (var i = 0; i < listOfSucessors.length; i++) {
                    var sucAsset = this.get(listOfSucessors[i]);
                    this.registry.associate(asset.path, sucAsset.path, "Successors");
                }
            }
        }
    };
};

asset.configure = function () {
    return {
        table: {
            overview: {
                fields: {
                    provider: {
                        readonly: true
                    },
                    name: {
                        name: {
                            name: 'name',
                            label: 'Name'
                        },
                        validation: function () {
                        }
                    },
                    version: {
                        name: {
                            label: 'Version'
                        }
                    }
                }
            },
            images: {
                fields: {
                	processArchive: {
                        type: 'file'
                    },
                    thumbnail: {
                        type: 'file'
                    },
                    banner: {
                        type: 'file'
                    }
                }
            }
        },
        meta: {
            lifecycle: {
                name: 'DefaultProcessLifeCycle',
                commentRequired: false,
                defaultAction: 'Promote',
                deletableStates: [],
                publishedStates: ['Published']
            },
            ui: {
                icon: 'icon-cog'
            },
            thumbnail: 'images_thumbnail',
            banner: 'images_banner',
            processArchive:'images_processArchive'
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