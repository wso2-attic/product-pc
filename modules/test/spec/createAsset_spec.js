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
describe('create chevron diagram - publisher', function() {
    var elements = [];
    beforeEach(function() {
        elements = [];
    });
   
    it('Add element to canvas', function() {
        elements.push(("#element"));
        expect(elements.length).toEqual(1);
    });
    it('Get matching row for element', function() {
        var element = returnElement();
        var rowId = getMatchingGridRow(element);
        expect(rowId).toEqual(2);
    });
    it('Get matching cell for element', function() {
        var element = returnElement();
        var rowId = getMatchingGridRow(element);
        var cellId = getMatchingGridCell(rowId, element);
        expect(cellId).toEqual(0);
    });
    it('Store occupied element positions on grid', function() {
        var element = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element);
        var cellId1 = getMatchingGridCell(rowId1, element);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(element2, element2);
        storeLocationOfElement(element, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        expect(occupiedGridPositions.length).toEqual(2);
    });
    it('Connect two elements', function() {
        var element = returnElement();
        var element2 = returnAnotherElement();
        connections.push({
            sourceId: element.id,
            targetId: element2.id
        })
        expect(connections.length).toEqual(1);
    });
    it('Get successor of element', function() {
        var element = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element);
        var cellId1 = getMatchingGridCell(rowId1, element);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(rowId2, element2);
        storeLocationOfElement(element, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        addRelationsForElement(element);
        addRelationsForElement(element2);
        expect(specializations[0].successorId).toEqual("e2");
    });
    it('Get predecessor of element', function() {
        var element = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element);
        var cellId1 = getMatchingGridCell(rowId1, element);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(element2, element2);
        storeLocationOfElement(element, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        addRelationsForElement(element);
        addRelationsForElement(element2);
        expect(specializations[1].predecessorId).toEqual("e1");
    });
    it('Get property fields of the diagram', function() {
        // setDiagramPropertyFields();//
        var propertyList = [];
        var propertyList = createMainProperties(propertyList);
        expect(propertyList.length).toBeGreaterThan(0);
        expect(propertyList[0].id).toEqual("mainId");
        expect(propertyList[0].name).toEqual("product payment");
        expect(propertyList[0].owner).toEqual("user");
        expect(propertyList[0].predecessor).toEqual("product order");
        expect(propertyList[0].successor).toEqual("product delivery");
        expect(propertyList[0].description).toEqual("product description");
    });
    it('Get property fields of a selected element', function() {
        var propertyList = [];
        var element = returnElement();
        var processName = "step1";
        createElementProperties(propertyList);
        element.find('.text-edit').val(processName);
        spyOn(element, 'click').and.callThrough();
        element.click(divClicked); //invoke click on the element
        expect(element.click).toHaveBeenCalled();
        expect(propertyList.length).toBeGreaterThan(0);
        expect(propertyList[0].id).toEqual("e1");
        expect(propertyList[0].name).toEqual("step1");
        expect(propertyList[0].models).toEqual("process1");
        expect(propertyList[0].information).toEqual("This is step 1");
    });
    it('Remove selected element', function() {
        var element = returnElement();
        occupiedGridPositions.push(("#element"));
        expect(occupiedGridPositions.length).toEqual(3);
        removeElement(element);
        expect(occupiedGridPositions.length).toEqual(2);
    });
    it('Confirm saved registry location', function() {
        spyOn($, "ajax");
        saveDiagram();
        expect($.ajax.calls.mostRecent().args[0]["url"]).toEqual("/publisher/asts/chevron/apis/chevronxml");
    });
    it('Confirm saved diagram ajax call success', function() {
        spyOn($, "ajax").and.callFake(function(options) {
            options.success();
        });
        var callback = jasmine.createSpy();
        saveDiagram(callback);
        expect(callback).toHaveBeenCalled();
    });
});