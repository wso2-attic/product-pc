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

