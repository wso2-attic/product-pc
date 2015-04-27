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
chevronProperties = []; //store properties of each chevron
var processId = 0; // store current process id of the chevron clicked  
var specializations = []; //store successor/predecessor relations for a chevron
var chevrons = []; //save element props for later
var formatting = []; //save layout of elements for later
var stateDragged = false; // if the chevron was moved in canvas
var editName = false; //check if chevron name got edited
var positionChanged = false; //check for position updates
var isNewElement; // adding new element to existing diagram
var previousName; // To store the previous chevron name to display the view if needed
var connections = []; // connections made between elements
var chevronList = []; // holds all chevron elements on canvas
var occupiedGridPositions = []; // all grid positions that holds an element
var elementsOnCanvas = []; //keep track of textbox ids 
var count = 0; // element id incrementer
var chevronPositions = []; // keep track of element positions for updates
var iconId; //holds the current id of chevron
var viewForName = []; // names of chevron views
var chevronNames = []; // keep track of chevron name value of each element
var nameForThisElement; //to map the text box value to property field value
var mainProcessOfDiagram; // store the diagram name 
var processTokens = []; // save process model name and id for linking purpose
var validElementIds = []; // to hold already saved element ids (to check for edits)
var currentElement; //temp store of element
var updatedConnections = []; // store new diagram connections
var processModelChanged = false; //flag to check process model update
var descriptionsForChevrons = []; //Keep track of the description value for each chevron 
var loadingDiagram = false; // flag to unbind connections function at page load
// associated process token store
var assets = {
    data: []
};
//check if requested cell and row are available
function checkPositionAvailability(cell, row) {
    for (var i = 0; i < occupiedGridPositions.length; i++) {
        if (cell == occupiedGridPositions[i].cell && row == occupiedGridPositions[i].row) {
            return false;
        }
    }
    return true;
}
// retrn the cell number for given element id
function getGridCellForElementId(id) {
    for (var i = 0; i < occupiedGridPositions.length; i++) {
        if (id == occupiedGridPositions[i].id) {
            return occupiedGridPositions[i].cell;
        }
    }
}
//return row number of given element id 
function getGridRowForElementId(id) {
    for (var i = 0; i < occupiedGridPositions.length; i++) {
        if (id == occupiedGridPositions[i].id) {
            return occupiedGridPositions[i].row;
        }
    }
}
//remove old element position from list
function removeOldGridPositionFromList(originalRow, originalCell, element) {
    var id = element.attr('id');
    for (var i = 0; i < occupiedGridPositions.length; i++) {
        if (occupiedGridPositions[i].id == id) {
            occupiedGridPositions.splice(i, 1);
        }
    }
}
//store the chevron name ,id pair for the given element
function storeChevronNameForElement(element, name) {
    var id = element.attr('id');
    if (chevronNames.length == 0) {
        chevronNames.push({
            id: id,
            name: name
        })
    } else {
        for (var i = 0; i < chevronNames.length; i++) {
            if (chevronNames[i].id == id) { //name edition for a stored element
                chevronNames[i].name = name;
            } else {
                chevronNames.push({
                    id: id,
                    name: name
                })
            }
        }
    }
}
//When element name is edited ,update 
function updateRecordAsSuccessor(elementId, name, prevName) {
    for (var i = 0; i < specializations.length; i++) {
        if (specializations[i].id == elementId && specializations[i].successor == prevName) {
            specializations[i].successor = name;
            return;
        }
    }
}
//When element name is edited, update 
function updateRecordAsPredecessor(elementId, name, prevName) {
    for (var i = 0; i < specializations.length; i++) {
        
        if (specializations[i].id == elementId && specializations[i].predecessor == prevName) {
            
            specializations[i].predecessor = name;
            return;
        }
    }
}
// retrn the cell number for given element id
function getGridCellForElementId(id) {
    for (var i = 0; i < occupiedGridPositions.length; i++) {
        if (id == occupiedGridPositions[i].id) {
            return occupiedGridPositions[i].cell;
        }
    }
}
// Remove cell/row combination from the grid
function removePositionFromGrid(element) {
    var elementId = element.attr('id');
    var elementCell = getGridCellForElementId(elementId); //get current element cell
    var elementRow = getGridRowForElementId(elementId);
    removeOldGridPositionFromList(elementRow, elementCell, element);
}
//Find the pre/suc relationships for element
function updateRelationsForElement(element) {
    var elementId = element.attr('id');
    var currentElementCell = getGridCellForElementId(elementId); //get current element cell
    connections.push({
        sourceId: "e3",
        targetId: "e2"
    });
    connections.push({
        sourceId: "e1",
        targetId: "e4"
    });
   
    if (connections.length == 0) { //no connections added
        return;
    } else { //if connections are found
        var connectedElementId = 0;
        for (var i = 0; i < connections.length; i++) { //while connections
            if (connections[i].targetId == elementId) { //if element id is found in target
                connectedElementId = connections[i].sourceId;
                
            }
            if (connections[i].sourceId == elementId) { //if element id is found in source
                connectedElementId = connections[i].targetId;
               
            }
            var connectedElementCell = getGridCellForElementId(connectedElementId); //get cell of connected element
              
            if (currentElementCell > connectedElementCell && connectedElementId != 0) { //current found a preceeding
                addElementAsPredecessor(element, connectedElementId); //add found element as a predecessor
            }
            if (currentElementCell < connectedElementCell && connectedElementId != 0) { //current found a succeeding
                addElementAsSuccessor(element, connectedElementId); //add found element as a successor
                   
            }
        } //end of for loop
    }
}