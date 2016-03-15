/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
$("#save-image").click(function(){
    
    var canvass = document.getElementById('hiddenCanvas');

    var svgDiv = $("#mainSvg")[0];

    var svg = svgDiv.outerHTML;     
   /* var canvas = document.createElement('canvas');*/
    canvg(canvass, svg);
    
    download(canvass, 'sample.png');
});


function download(canvas, filename) {
    download_in_ie(canvas, filename) || download_with_link(canvas, filename);
}

// Works in IE10 and newer
function download_in_ie(canvas, filename) {
    return(navigator.msSaveOrOpenBlob && navigator.msSaveOrOpenBlob(canvas.msToBlob(), filename));
}

// Works in Chrome and FF. Safari just opens image in current window, since .download attribute is not supported
function download_with_link(canvas, filename) {
    var a = document.createElement('a')
    a.download = filename
    a.href = canvas.toDataURL("image/png")
    document.body.appendChild(a);
    a.click();
    a.remove();
}