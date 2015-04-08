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
describe('update chevron diagram - publisher', function() {
    var elements = [];
    var id;
    var top;
    var left;
    beforeEach(function() {
        createNewElement(id, top, left);
    });
    afterEach(function() {
        occupiedGridPositions = [];
        specializations = [];
        connections = [];
    });
    it('Add new element to existing diagram', function() {
        elements.push(("#element"));
        expect(elements.length).toEqual(1);
    });
    it('Grid position not available to place element', function() {
        var element = returnElement();
        var rowId1 = getMatchingGridRow(element);
        var cellId1 = getMatchingGridCell(rowId1, element);
        storeLocationOfElement(element, rowId1, cellId1);
        var available = checkPositionAvailability(cellId1, rowId1);
        expect(available).toBe(false);
    });
    it('Grid position  available to place element', function() {
        id = 'e3';
        top = 100;
        left = 100;
        var element3 = createNewElement(id, top, left);
        var rowId1 = getMatchingGridRow(element3);
        var cellId1 = getMatchingGridCell(rowId1, element3);
        var available = checkPositionAvailability(cellId1, rowId1);
        expect(available).toBe(true);
    });
    it('Remove element from grid on delete', function() {
        id = 'e4';
        top = 400;
        left = 400;
        var element3 = createNewElement(id, top, left);
        var rowId1 = getMatchingGridRow(element3);
        var cellId1 = getMatchingGridCell(rowId1, element3);
        storeLocationOfElement(element3, rowId1, cellId1);
        expect(occupiedGridPositions.length).toEqual(1); //before removing
        removePositionFromGrid(element3);
        expect(occupiedGridPositions.length).toEqual(0);
    });
    it('Update predecessor of element', function() {
        id3 = "e3";
        top = 270;
        left = 100;
        var id1 = "e1";
        var id2 = "e2";
        var element1 = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element1);
        var cellId1 = getMatchingGridCell(rowId1, element1);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(rowId2, element2);
        storeLocationOfElement(element1, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        addRelationsForElement(element2);
        expect(specializations[0].predecessorId).toEqual("e1");
        removePositionFromGrid(element1);
        removeRelatedConnectionsFromList(element1);
        var element3 = createNewElement(id3, top, left);
        var rowId3 = getMatchingGridRow(element3);
        var cellId3 = getMatchingGridCell(rowId3, element1);
        storeLocationOfElement(element3, rowId3, cellId3);
        updateRelationsForElement(element2);
        expect(specializations[0].predecessorId).toEqual("e3");
    });
    it('Update successor of element', function() {
        id4 = "e4";
        top = 350;
        left = 200;
        var id1 = "e1";
        var id2 = "e2";
        var element1 = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element1);
        var cellId1 = getMatchingGridCell(rowId1, element1);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(rowId2, element2);
        storeLocationOfElement(element1, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        addRelationsForElement(element1);
        expect(specializations[0].successorId).toEqual("e2");
        removePositionFromGrid(element2);
        removeRelatedConnectionsFromList(element2);
        var element3 = createNewElement(id4, top, left);
        var rowId3 = getMatchingGridRow(element3);
        var cellId3 = getMatchingGridCell(rowId3, element3);
        storeLocationOfElement(element3, rowId3, cellId3);
        updateRelationsForElement(element1);
        expect(specializations[0].successorId).toEqual("e4");
    });
    it('Confirm registry location of saved diagram', function() {
        spyOn($, "ajax");
        saveDiagram();
        expect($.ajax.calls.mostRecent().args[0]["url"]).toEqual("/publisher/asts/chevron/apis/chevronxml");
    });
    it('Confirm reaching save ajax call success', function() {
        spyOn($, "ajax").and.callFake(function(options) {
            options.success();
        });
        var callback = jasmine.createSpy();
        saveDiagram(callback);
        expect(callback).toHaveBeenCalled();
    });
    it('Update multiple successors for element', function() {
        id5 = "e4";
        top = 120;
        left = 360;
        var id1 = "e1";
        var id2 = "e2";
        var element4 = createNewElement(id5, top, left);
        var rowId4 = getMatchingGridRow(element4);
        var cellId4 = getMatchingGridCell(rowId4, element4);
        storeLocationOfElement(element4, rowId4, cellId4);
        var element1 = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element1);
        var cellId1 = getMatchingGridCell(rowId1, element1);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(rowId2, element2);
        storeLocationOfElement(element1, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        addRelationsForElement(element1);
        updateRelationsForElement(element1);
        expect(specializations[0].id).toEqual(specializations[1].id);
        expect(specializations[0].successorId).toEqual("e2");
        expect(specializations[1].successorId).toEqual("e4");
    });
    it('Update multiple predecessors for element', function() {
        id5 = "e3";
        top = 50;
        left = 50;
        var element3 = createNewElement(id5, top, left);
        var rowId3 = getMatchingGridRow(element3);
        var cellId3 = getMatchingGridCell(rowId3, element3);
        storeLocationOfElement(element3, rowId3, cellId3);
        var element1 = returnElement();
        var element2 = returnAnotherElement();
        var rowId1 = getMatchingGridRow(element1);
        var cellId1 = getMatchingGridCell(rowId1, element1);
        var rowId2 = getMatchingGridRow(element2);
        var cellId2 = getMatchingGridCell(rowId2, element2);
        storeLocationOfElement(element1, rowId1, cellId1);
        storeLocationOfElement(element2, rowId2, cellId2);
        addRelationsForElement(element2);
        updateRelationsForElement(element2);
        expect(specializations[0].id).toEqual(specializations[1].id);
        expect(specializations[0].predecessorId).toEqual("e1");
        expect(specializations[1].predecessorId).toEqual("e3");
    });
});