/*
 * Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
describe('view chevron diagram - store', function() {
    afterEach(function() {
        chevronProperties = [];
    });
    it('Confirm ajax call success of retrieving requested digram', function() {
        spyOn($, "ajax").and.callFake(function(options) {
            options.success();
        });
        var getXmlForProcess = jasmine.createSpy();
        getRelatedDiagram(getXmlForProcess);
        expect(getXmlForProcess).toHaveBeenCalled();
    });
    it('Confirm registry location to retrieve diagram data', function() {
        spyOn($, "ajax");
        var diagramName = 'product delivery';
        getXmlForProcess(diagramName);
        expect($.ajax.calls.mostRecent().args[0]["url"]).toEqual("/store/asts/chevron/apis/chevronxml");
    });
    it('Confirm ajax call success of retrieving diagram data', function() {
        spyOn($, "ajax").and.callFake(function(options) {
            options.success();
        });
        var drawDiagramOnCanvas = jasmine.createSpy();
        var process;
        getXmlForProcess(drawDiagramOnCanvas, process);
        expect(drawDiagramOnCanvas).toHaveBeenCalled();
    });
    it('Display properties on element selection when a process model is not passed', function() {
        var element = returnElement();
        var id = element.attr('id');
        var process;
        var name = "process1";
        var description = "description";
        storePropertiesOfChevron(id, name, description, process);
        spyOn(element, 'click');
        element.click(chevronClicked); //invoke click on the element
        expect(element.click).toHaveBeenCalled();
        expect(chevronProperties.length).toEqual(1);
        expect(chevronProperties[0].name).toEqual("process1");
        expect(chevronProperties[0].description).toEqual("description");
        expect(chevronProperties[0].process).toEqual("None");
    });
    it('Display properties on element selection when a process model is passed', function() {
        var element = returnAnotherElement();
        var id = element.attr('id');
        var process = "process1";
        var name = "process1";
        var description = "description";
        storePropertiesOfChevron(id, name, description, process);
        spyOn(element, 'click');
        element.click(chevronClicked); //invoke click on the element
        expect(element.click).toHaveBeenCalled();
        expect(chevronProperties.length).toEqual(1);
        expect(chevronProperties[0].name).toEqual("process1");
        expect(chevronProperties[0].description).toEqual("description");
        expect(chevronProperties[0].process).toEqual("process1");
    });
    it('Hide connections on button click', function() {
        var button = createHideButton();
        var elm = button.click();
        expect(elm.length).toBeGreaterThan(0);
    });
    it('Show connections on button click', function() {
        var button = createShowButton();
        var result = button.click();
        expect(result.length).toEqual(1);
    });
});