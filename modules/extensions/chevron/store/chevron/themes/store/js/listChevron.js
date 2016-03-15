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
/*
 Functionality related to list created chevron diagrams in Publisher
 */
var linkName; //store chevron name
var href; // store link of the redirected page
function getRelatedChevronDiagram(element) {
    var checkName = $.trim(linkName);
    $.ajax({
        type: "POST",
        url: "/publisher/assets/chevron/apis/nameStore",
        data: {
            linkName: checkName,
            type: "POST"
        }
    });
}
// when the image is clicked get chevron name
$('.ast-img').click(function (e) {
    var element = $(this);
    href = element.attr('href');
    linkName = element[0].nextElementSibling.attributes[0].ownerElement.childNodes[1].innerText;
    //linkName.split("\\s+");
    var content = linkName.split(/\r\n|\r|\n/g);
    linkName = content[0];
    getRelatedChevronDiagram(element);
    loadPageForLink(href);
});
// when link is clicked get chevron process name 
$('.ast-name').click(function (e) {
    var element = $(this);
    linkName = element.text();
    href = element.attr('href');
    getRelatedChevronDiagram(element);
    loadPageForLink(href);
});

function loadPageForLink(href) {
    window.location = href;
}