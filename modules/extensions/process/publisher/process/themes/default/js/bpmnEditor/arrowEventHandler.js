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
 // Mouse down on edge/link
processEditor.prototype.mouseDownOnArrowLink = function(arrow, d) {
    var bpmnProcessDiagram = this;
    var stateOfProcessDiagram = bpmnProcessDiagram.stateOfProcessDiagram;
    d3.event.stopPropagation();

    stateOfProcessDiagram.mouseDownOnArrow = d;

    if (stateOfProcessDiagram.selectedActivity) {
        bpmnProcessDiagram.removeSelectedFromActivity();
    }

    var previousArrow = stateOfProcessDiagram.selectedArrowLink;

    if (!previousArrow || previousArrow !== d) {
        bpmnProcessDiagram.replaceSelectedArrowLink(arrow, d);
    } else {
        bpmnProcessDiagram.removeSelectedFromArrowLink();
    }
};