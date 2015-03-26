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

    var mainProcess = []; // store properties of diagram and elements
    var viewForName = []; // names of chevron views 
    var mainProcessName; // name of diagram view
    var iconId; //holds the current id of chevron
    var nameForThisElement; //to map the text box value to property field value
    var elementsOnCanvas = []; // holds all ids of dropped elements on canvas
    var count = 0; // element id incrementer
    var specializations = []; // to store pre/succssor relationships for an element
    var chevrons = []; // save chevron elements on canvas
    var formatting = []; //save chevron postioning on canvas
    var stateDragged = false; //check for postion change 
    var validElementIds = []; // to hold already saved element ids (to check for edits)
    var editName = false; //if the element name is edited
    var existingElement = false; // add edit functionality if element is already created
    var chevronPositions = []; // store position updates of chevrons
    var positionChanged = false; // user changed order of element positions
    var previousName; // when editing chevron name show properties depending on the previous chevron
    var saveClicked = false; // check if properties are saved for previous chevron
    var processTokens = []; // save process model name and id for linking purpose
    var occupiedGridPositions = []; //Keep track of the taken grid positions
    var connections = []; // drawn connection related data
    var chevronNames = []; // holds related name for element id
    var descriptionsForChevrons = []; //keep track of the element ids and descriptions
    var clickedDefaultId = 0; //Keep track of currently clicked element default ids given by jsplumb
    //on form submit save xml content of the drawn canvas
    $('#btn-create-asset').click(function(e) {
        checkForMandatoryFields(); //check if main properties of the diagram are added
        saveDiagram();
    });
    // Save  table properties of elements
    $('#saveElms').click(function(e) {
        createElementProperties(mainProcess);
        var chevronName = $("#properties_ename").val(); //latest value 
        var chevronDescription = $("#properties_eDescription").val(); //get latest value
        updateProcessModelOfState(chevronName); // save the new process model
        updateDescriptionOfState(chevronName, chevronDescription); //save new description 
        saveClicked = true;
        $("#elementProps").hide();
    });
    //Save properties of main process
    $('#save').click(function() {
        createMainProperties(mainProcess);
        $("#fullProps").hide();
    });
    // On Canvas doubleclick show create/view page
    $('#dropArea').dblclick(function(e) {
        if (mainProcess.length !== 0 && mainProcessName) // propererties for main diagram added
        {
            $("#fullProps").show();
            viewPropertyList(mainProcess);
            $("#elementProps").hide();
        } else {
            $("#fullProps").show();
            $("#elementProps").hide();
        }
    });
    // Storing drawn connection arrays
    jsPlumb.bind('connection', function(info) {
        var endpoints = info.connection.endpoints; //get endpoints that are used for the connection
        var endPointSourceX = endpoints[0].anchor.anchors[0].x;
        var sourceOrientationX = endpoints[0].anchor.anchors[0].orientation[0];
        var sourceOrientationY = endpoints[0].anchor.anchors[0].orientation[1];
        var endPointSourceY = endpoints[0].anchor.anchors[0].y;
        var endPointTargetX = endpoints[1].anchor.anchors[0].x;
        var targetOrientationX = endpoints[1].anchor.anchors[0].orientation[0];
        var targetOrientationY = endpoints[1].anchor.anchors[0].orientation[1];
        var endPointTargetY = endpoints[1].anchor.anchors[0].y;
        connections.push({
            sourceId: info.sourceId,
            targetId: info.targetId,
            sourceAnchor1: endPointSourceX,
            sourceAnchor2: endPointSourceY,
            orientation1: sourceOrientationX,
            orientation2: sourceOrientationY,
            targetAnchor1: endPointTargetX,
            targetAnchor2: endPointTargetY,
            orientation3: targetOrientationX,
            orientation4: targetOrientationY
        })
    });
    //update process model value of the chevron state
    function updateProcessModelOfState(chevronName) {
        for (var i = 0; i < chevrons.length; i++) {
            if (chevrons[i].chevronName == chevronName) // if its an existing element
            {
                for (var j = 0; j < mainProcess.length; j++) // get process model for chevron
                {
                    if (mainProcess[j].name == chevronName) {
                        var processModel = mainProcess[j].models;
                    }
                }
                if (chevrons[i].processModel) { //if already added
                    chevrons[i].processModel = processModel;
                }
            }
        }
    }
    //Before creating a chevron diagram check if diagram details are added.
    function checkForMandatoryFields() {
        var recordAdded = false;
        for (var i = 0; i < mainProcess.length; i++) {
            if (mainProcess[i].description) { // main chevron diagram values are added
                recordAdded = true;
            }
        }
        if (!recordAdded) {
            alert('Please fill all properties of chevron diagram');
            return false;
        } else {
            return true;
        }
    }
    //Get matching id for the associated process model name
    function getProcessIdForProcessModel(processName) {
        if (processName) {
            for (var i = 0; i < processTokens.length; i++) {
                if (processName == processTokens[i].processName) {
                    return processTokens[i].processId;
                }
            }
        }
    }
    //Display added properties for a view
    function viewElementProperties(propertyList, specializationList, name) {
        for (var i = 0; i < propertyList.length; i++) {
            if (propertyList[i].name == name) { // if name of chevron found
                var checkId = propertyList[i].id;
                var processId = getProcessIdForProcessModel(propertyList[i].models);
                $("#properties_ename").val(propertyList[i].name);
                $("#properties_eDescription").val(propertyList[i].information);
                $("#properties_Associated_process_emodels").tokenInput("clear").tokenInput("add", {
                    id: processId,
                    name: propertyList[i].models
                });
                if (specializationList.length > 0) { // if predecessor/successor exists for chevron
                    var predecessors = getPredecessorsForElement(checkId); //get predecessors
                    var successors = getSuccessorsForElement(checkId); //get successors
                    $("#properties_epredecessors").val(predecessors);
                    $("#properties_esuccessors").val(successors);
                }
            }
        }
    }
    //retrieve predecessors for given element 
    function getPredecessorsForElement(id) {
        var predecessorList = '';
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].id == id) { //element found
                predecessorList += specializations[i].predecessor + ',';
            }
        }
        if ((predecessorList.match(/,/g) || []).length > 1) { //more than 1 predecessor ,comma separated values
            if ((predecessorList.match(/,/g) || []).length !== (predecessorList.match(/not declared/g) || []).length) {
                while ((predecessorList.match(/not declared/g) || []).length) { //if it has a not declared
                    predecessorList = predecessorList.replace('not declared,', '');
                }
            } else {
                //all values of predecessor is not declared
                predecessorList = "not declared";
            }
        }
        var lastChar = predecessorList.slice(-1);
        if (lastChar == ',') {
            predecessorList = predecessorList.slice(0, -1); //remove trailing comma
        }
        return predecessorList;
    }

    function getSuccessorsForElement(id) {
        var successorList = '';
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].id == id) { //element found
                successorList += specializations[i].successor + ',';
            }
        }
        if ((successorList.match(/,/g) || []).length > 1) { //more than 1 successor
            if ((successorList.match(/,/g) || []).length !== (successorList.match(/not declared/g) || []).length) {
                while ((successorList.match(/not declared/g) || []).length) { //if it has a not declared
                    successorList = successorList.replace('not declared,', '');
                }
            } else { //all values of successor is not declared
                successorList = "not declared";
            }
        }
        var lastChar = successorList.slice(-1); //get the last character
        if (lastChar == ',') {
            successorList = successorList.slice(0, -1); //remove trailing comma
        }
        return successorList;
    }
    //Add properties for elements
    function createElementProperties(propertyList) {
        var id = iconId; // get the current id of the element
        //for testing use
          id = "e1";
          var name = "step1";
          var information = "This is step 1";
          var models = "process1";
        //
        /*var name = $("#properties_ename").val();
        var information = $("#properties_eDescription").val();
        var models = $("#properties_Associated_process_emodels").val();*/
        for (var i = 0; i < propertyList.length; i++) {
            if (id == propertyList[i].id) { // its an edit of a record
                propertyList[i].name = name;
                propertyList[i].models = models;
                propertyList[i].information = information;
                existingElement = true;
            }
        }
        // its a new record
        if (!existingElement) {
            propertyList.push({
                id: id,
                name: name,
                models: models,
                information: information
            });
        }
        //for testing use 
        return propertyList;
        //
        var descriptionEdited = false;
        // To display  descriptions at element tooltip
        if (descriptionsForChevrons.length != 0) {
            for (var i = 0; i < descriptionsForChevrons.length; i++) {
                if (clickedDefaultId == descriptionsForChevrons[i].id) {
                    descriptionsForChevrons[i].description = information;
                    descriptionEdited = true;
                    return;
                }
            }
        }
        //if no description value was added for this id 
        if (!descriptionEdited) {
            descriptionsForChevrons.push({
                id: clickedDefaultId,
                description: information
            });
        }
        var viewName = name;
        viewForName.push(viewName); // keep track of the elements that has view properties added
    }
    //validating main table fields of chevron diagram
    function validateTableFields(description, name, owner, predecessor, successor) {
        var provider = $("#overview_provider").val();
        var version = $("#overview_version").val();
        if (!description || !name || !owner || !predecessor || !successor || !provider || !version) {
            alert('Please fill all the fields');
            return false;
        }
    }
    // Add properties for main process
    function createMainProperties(propertyList) {
      var mainId = "mainId";
      
       /* var description = $("textarea#overview_description").val();
        var name = $("#overview_name").val();
        var owner = $("#properties_owner").val();
        var predecessor = $("#properties_predecessors").val();
        var successor = $("#properties_successors").val();*/
       //for testing use
        var name = "product payment";
        var owner = "user";
        var predecessor = "product order";
        var successor = "product delivery";
        var description = "product description";
       // validateTableFields(description, name, owner, predecessor, successor); //validate empty/null fields
        if (mainProcessName) //its an edit of record
        {
            for (var i = 0; i < propertyList.length; i++) {
                if (propertyList[i].name == mainProcessName) {
                    propertyList[i].name = name;
                    propertyList[i].owner = owner;
                    propertyList[i].predecessor = predecessor;
                    propertyList[i].successor = successor;
                    propertyList[i].description = description;
                }
            }
        } else {
            propertyList.push({
                id: mainId,
                name: name,
                owner: owner,
                predecessor: predecessor,
                successor: successor,
                description: description
            });
            mainProcessName = name;
        }
        //testing use
        return propertyList;
    }
    // Display added properties for main process
    function viewPropertyList(propertyList) {
        for (var i = 0; i < propertyList.length; i++) {
            if (propertyList[i].description) {
                $("#overview_name").val(propertyList[i].name);
                $("#properties_owner").val(propertyList[i].owner);
                $("#properties_predecessors").val(propertyList[i].predecessor);
                $("#properties_successors").val(propertyList[i].successor);
                $("textarea#overview_description").val(propertyList[i].description);
            }
        }
    }
    // Get the relevant description value for the given id 
    function getDescriptionToSaveState(id) {
        var description = "not available"; //default value
        if (descriptionsForChevrons.length != 0) {
            for (var i = 0; i < descriptionsForChevrons.length; i++) {
                if (id == descriptionsForChevrons[i].id) {
                    description = descriptionsForChevrons[i].description;
                    return description;
                }
            }
        }
        return description;
    }
    // update description field to new value
    function updateDescriptionOfState(name, description) {
        if (chevrons.length != 0) {
            for (var i = 0; i < chevrons.length; i++) {
                if (chevrons[i].chevronName == name) { //match found
                    chevrons[i].description = description;
                    return;
                }
            }
        }
    }
    // Save canvas elements and positions
    var saveState = function(chevron) {
        var processModel = "not declared"; //state is first saved when element is dropped to canvas
        var chevronId1 = chevron.find('.text-edit').attr('name');
        var elementId = chevron.attr('id'); // To identify entire element in connections
        var chevronName = chevron.find(".text-edit").val();
        var positionX = parseInt(chevron.css("left"), 10);
        var positionY = parseInt(chevron.css("top"), 10);
        //get description value for the given chevron 
        var description = getDescriptionToSaveState(elementId);
        if (stateDragged || editName) // if position changed/name changed
        {
            for (var i = 0; i < chevrons.length; i++) {
                if (chevrons[i].chevronId == chevronId1) // if its an existing element
                {
                    for (var j = 0; j < mainProcess.length; j++) // get process model for chevron
                    {
                        if (mainProcess[j].id == chevronId1) {
                            processModel = mainProcess[j].models;
                        }
                    }
                    chevrons[i].processModel = processModel;
                    if (stateDragged) {
                        formatting[i].positionX = positionX;
                        formatting[i].positionY = positionY;
                    } else {
                        chevrons[i].chevronName = chevronName;
                    }
                }
            }
            stateDragged = false;
        } else {
            chevrons.push({
                diagramName: mainProcessName,
                elementId: elementId,
                chevronId: chevronId1,
                chevronName: chevronName,
                description: description,
                processModel: processModel
            });
            formatting.push({
                chevronId: chevronId1,
                positionX: positionX,
                positionY: positionY
            });
        }
    }
    //store coordinates of each chevron position
        function setChevronPositions(element) {
            var name = "empty"; //default value
            var id = element.find('.text-edit').attr('name');
            var xValue = parseInt(element.css("left"), 10);
            var yValue = parseInt(element.css("top"), 10);
            chevronPositions.push({
                chevId: id,
                chevName: name,
                xVal: xValue,
                yVal: yValue
            });
        }
        // update stored coordinates of each chevron position
        function updateChevronPositions(element) {
            var id = element.find('.text-edit').attr('name');
            var xValue = parseInt(element.css("left"), 10);
            var yValue = parseInt(element.css("top"), 10);
            for (var i = 0; i < chevronPositions.length; i++) {
                if (id == chevronPositions[i].chevId) {
                    if (chevronPositions[i].xVal != xValue) {
                        chevronPositions[i].xVal = xValue;
                        chevronPositions[i].yVal = yValue;
                        chevronPositions[i].chevName = element.find('.text-edit').val();
                        positionChanged = true;
                    }
                }
            }
        }
        // check if the previous properties were saved 
        function validateUnsavedProperties() {
            if ($("#properties_ename").val() !== '' && !saveClicked) {
                alert("Please save entered properties first");
                return false;
            } else {
                saveClicked = false;
                return true;
            }
        }
        //display properties for chevron if it was not displayed before
        function viewPropertyForNewChevron(chevronText) {
            if (chevronText == $("#properties_ename").val()) {
                if (previousName !== chevronText) {
                    previousName = chevronText;
                    viewElementProperties(mainProcess, specializations, chevronText);
                }
            }
            if (chevronText !== $("#properties_ename").val()) {
                if (previousName !== chevronText) {
                    previousName = chevronText;
                    viewElementProperties(mainProcess, specializations, chevronText);
                }
            }
        }
        // Set incremental ids for elements that are added to the canvas
        function setIdsForElements(defaultId, element) {
            if (defaultId == 0 && elementsOnCanvas.length == 0) //first element
            {
                isFirstElement = true;
                defaultId = ++count;
                elementsOnCanvas.push(defaultId);
                element.find('.text-edit').attr('name', defaultId);
                setChevronPositions(element); //save position details for chevron
            }
            if (defaultId == 0 && elementsOnCanvas.length !== 0) //not first element 
            {
                var lastLocation = elementsOnCanvas.length;
                var temp = elementsOnCanvas[lastLocation - 1];
                defaultId = temp + 1;
                elementsOnCanvas.push(defaultId);
                element.find('.text-edit').attr('name', defaultId);
                setChevronPositions(element);
            }
        }
        // get the row of the element 
        function getAlignedGridRow(yPosition) {
            var rowId;
            // Canvas y axis ranges for each row definition
            var range1 = 0;
            var range2 = 97;
            var range3 = 98;
            var range4 = 217;
            var range5 = 218;
            var range6 = 321;
            var range7 = 322;
            if (range1 < yPosition && yPosition <= range2) {
                rowId = 0;
            } else if (range3 < yPosition && yPosition <= range4) {
                rowId = 1;
            } else if (range5 < yPosition && yPosition <= range6) {
                rowId = 2;
            } else if (range7 < yPosition) {
                rowId = 3;
            }
            return rowId;
        }
        // add taken grid position (row/column)
        function addNewGridPositionToList(updatedRow, updatedCell, element) {
            var id = element.attr('id');
            var elementExists = false;
            for (var i = 0; i < occupiedGridPositions.length; i++) {
                if (occupiedGridPositions[i].id == id) {
                    elementExists = true;
                }
            }
            if (!elementExists) {
                occupiedGridPositions.push({ //save the taken cell position of the grid
                    id: id,
                    row: updatedRow,
                    cell: updatedCell
                });
            }
        }
        //remove previous grid position(row/column)
        function removeOldGridPositionFromList(originalRow, originalCell, element) {
            var id = element.attr('id');
            for (var i = 0; i < occupiedGridPositions.length; i++) {
                if (occupiedGridPositions[i].id == id) {
                    occupiedGridPositions.splice(i, 1);
                }
            }
        }
        // replace the element to previous position
        function revertToOldPosition(clickedElement, originalXPosition, originalYPosition) {
            clickedElement.css("left", originalXPosition);
            clickedElement.css("top", originalYPosition);
        }
        // add the element to given row and cell
        function addElementToGrid(row, cell, element) {
            var proceed = false;
            var available = checkPositionAvailability(cell, row); //check if position is not taken
            if (!available) // if original spot is taken
            {
                var newCell = ++cell;
                proceed = checkPositionAvailability(newCell, row); //check for next spot
                if (!proceed) //if new spot is also taken
                {
                    alert('Please add element to available location');
                    return false;
                } else {
                    proceed == true;
                }
            }
            if (available || proceed) { // if the suggested position is not occupied
                setPositionForElement(row, cell, element); //set css top/left values 
                storeLocationOfElement(element, row, cell);
                element.appendTo('#dropArea'); //add element to canvas
                return true;
            }
        }
        //store newly accessed row and cell of the grid
        function storeLocationOfElement(element, row, cell) {
            var elementId = element.attr('id');
            var elementAdded = false;
            if (occupiedGridPositions.length !== 0) { //if not first element
                for (var i = 0; i < occupiedGridPositions.length; i++) {
                    if (occupiedGridPositions[i].id == elementId) {
                        elementAdded = true;
                    }
                }
                if (!elementAdded) {
                    occupiedGridPositions.push({ //save the taken cell position of the grid
                        id: elementId,
                        row: row,
                        cell: cell
                    });
                }
            } else {
                occupiedGridPositions.push({ //save the taken cell position of the grid
                    id: elementId,
                    row: row,
                    cell: cell
                });
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
        //return row number of given element id 
        function getGridRowForElementId(id) {
            for (var i = 0; i < occupiedGridPositions.length; i++) {
                if (id == occupiedGridPositions[i].id) {
                    return occupiedGridPositions[i].row;
                }
            }
        }
        //return the chevron name of given element id
        function getChevronNameForId(id) {
            if (chevronNames.length == 0) { //if no values are added for chevrons
                return;
            } else {
                for (var i = 0; i < chevronNames.length; i++) {
                    if (chevronNames[i].id == id) { //if matching element found
                        return chevronNames[i].name;
                    }
                }
            }
        }
        //add found element as a successor for current element
        function addElementAsSuccessor(element, foundElementId) {
            var defaultPredecessor = "not declared";
            var successorAlreadyAdded = false;
            var currentId = element.attr('id'); //get current element's id 
            var successor = getChevronNameForId(foundElementId); //get name text of the found element
            for (var i = 0; i < specializations.length; i++) {
                if (specializations[i].id == currentId) { //this element has previous records
                    if (specializations[i].successorId == foundElementId) { //if this predecessor is already added
                        successorAlreadyAdded = true;
                        return;
                    }
                }
            }
          
                        if (!successorAlreadyAdded) {
                specializations.push({
                    id: currentId,
                    predecessorId: defaultPredecessor,
                    predecessor: defaultPredecessor,
                    successorId: foundElementId,
                    successor: successor
                });
            }
        }
        //add found element as a predecessor for current element
        function addElementAsPredecessor(element, foundElementId) {
            var defaultSuccessor = "not declared";
            var predecessorAlreadyAdded = false;
            var currentId = element.attr('id'); //get current element's id 
            var predecessor = getChevronNameForId(foundElementId); //get name text of the found element
            for (var i = 0; i < specializations.length; i++) {
                if (specializations[i].id == currentId) { //this element has previous records
                    if (specializations[i].predecessorId == foundElementId) { //if this predecessor is already added
                        predecessorAlreadyAdded = true;
                        return;
                    }
                }
            }
           
            if (!predecessorAlreadyAdded) {
                specializations.push({
                    id: currentId,
                    predecessorId: foundElementId,
                    predecessor: predecessor,
                    successorId: defaultSuccessor,
                    successor: defaultSuccessor
                });
            }
        }
        //Find the pre/suc relationships for element
        function addRelationsForElement(element) {
            var elementId = element.attr('id');
            var currentElementCell = getGridCellForElementId(elementId); //get current element cell
            //alert('curr cell' + currentElementCell);
           // alert('connect' + connections.length);
            // for test purposes

            connections.push({
            sourceId: "e1",
            targetId: "e2"
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
        //clear all stored values on canvas reset 
        function clearAllArrays() {
            mainProcess = [];
            viewForName = [];
            elementsOnCanvas = [];
            specializations = [];
            chevrons = [];
            formatting = [];
            chevronPositions = [];
            processTokens = [];
            occupiedGridPositions = [];
            connections = [];
            chevrons = [];
        }
        // Clear canvas grid positions on reset
    $("#removeDiagram").click(function() {
        clearAllFields(); //Empty all table fields for previous chevrons
        clearAllArrays(); // Empty all previously made connections/relations
        jsPlumb.detachEveryConnection();
        jsPlumb.deleteEveryEndpoint();
        $(".chevron").remove();
        while (occupiedGridPositions.length > 0) { //clear gridpositions
            occupiedGridPositions.pop();
        }
    });
    //show/hide connections on toggle
    $("#connectionVisibility").click(function() {
        if ($.trim($(this).text()) === 'Hide Connections') {
            $('.chevron').each(function(index) {
                jsPlumb.hide(this.id, true);
            });
            $(this).text('Show Connections');
        } else {
            $('.chevron').each(function(index) {
                jsPlumb.show(this.id, true);
            });
            $(this).text('Hide Connections');
        }
        return false;
    });
    // check if element was dragged from its original position
    function checkForDragChange(element) {
        var id = element.find('.text-edit').attr('name');
        var xValue = parseInt(element.css("left"), 10);
        var yValue = parseInt(element.css("top"), 10);
        var dragged = true;
        for (var i = 0; i < chevronPositions.length; i++) {
            if (id == chevronPositions[i].chevId) { //element found
                if (chevronPositions[i].xVal == xValue || chevronPositions[i].yVal == yValue) {
                    dragged = false;
                    return;
                }
            }
        }
        return dragged;
    }
    // If chevron element is clicked 
    function divClicked() {
        var isFirstElement = false;
        var clickedElement = $(this);
        clickedDefaultId = clickedElement.attr('id');
       
        //TO BE INCLUDEDv
        // clickedElement.css('border','2px solid black');
        // element values before a drag and drop
        var originalXPosition = parseInt(clickedElement.css("left"), 10);
        var originalYPosition = parseInt(clickedElement.css("top"), 10);
        var originalRow = getAlignedGridRow(originalYPosition);
        var originalCell = getMatchingGridCell(originalRow, clickedElement);
        //store taken positions and ids 
        storeLocationOfElement(clickedElement, originalRow, originalCell); //store grid position that is occupied
        clickedElement.find('.text-edit').position({ // position text box in the center of element
            my: "center",
            at: "center",
            of: clickedElement
        });
        clickedElement.find('.text-edit').css('visibility', 'visible');
        clickedElement.find('.text-edit').focus();
        var defaultId = clickedElement.find('.text-edit').attr('name');
        var testName = clickedElement.find('.text-edit').text();
        iconId = defaultId; //setting current id of the element
        setIdsForElements(defaultId, clickedElement); //set ids for elements that are added to canvas
        // make element draggable  
        jsPlumb.draggable(clickedElement, {
            containment: 'parent',
            stop: function(event) {
                // var element = $(this);
                var dragState = checkForDragChange(clickedElement);
                if (dragState) { //only if the element was moved to a new location
                    var latestPositionY = parseInt(clickedElement.css("top"), 10);
                    var updatedRow = getAlignedGridRow(latestPositionY);
                    var updatedCell = getMatchingGridCell(updatedRow, clickedElement);
                    var availability = checkPositionAvailability(updatedCell, updatedRow);
                    if (!availability) {
                        revertToOldPosition(clickedElement, originalXPosition, originalYPosition); //send the element back to old position
                    } else {
                        removeOldGridPositionFromList(originalRow, originalCell, clickedElement); // remove previous grid row/cell from stored list
                        setPositionForElement(updatedRow, updatedCell, clickedElement); //set the new position coordinates of the element
                        addNewGridPositionToList(updatedRow, updatedCell, clickedElement);
                        stateDragged = true;
                        saveState(clickedElement);
                        updateChevronPositions(clickedElement); // update position changes
                    }
                }
            }
        });
        
        var textValue = clickedElement.find('.text-edit').val();
       
        //
        if ($.inArray(textValue, viewForName) > -1) // if properties already added for element
        //show the view with values
        {
            addRelationsForElement(clickedElement); //Add predecessor/successors for element
            $("#elementProps").show();
            //  viewElementProperties(mainProcess, specializations, textValue);
            viewPropertyForNewChevron(textValue); // display properties for the chevron 
            $("#fullProps").hide();
        } else { // let user add properties for new element
            clearAllFields();
            $("#fullProps").hide();
            $("#elementProps").show();
            clickedElement.find('.text-edit').focus();
            clickedElement.find('.text-edit').css('background-color', 'white');
            // user is finished adding chevron name
            clickedElement.find('.text-edit').userInputAdded(function() {
                var tempId = clickedElement.find('.text-edit').attr('name');
                iconId = tempId;
                if ($.inArray(tempId, validElementIds) > -1) { // editing the name
                    var tempName = ($(this).val());
                    viewForName.push($(this).val());
                    editName = true;
                    saveState(clickedElement);
                } else {
                    validElementIds.push(tempId);
                }
                clickedElement.find('.text-edit').css('background-color', '#FFCC33');
                var nameOfCurrentTextBox = $(this).val();
                if (nameOfCurrentTextBox) //if not empty
                {
                    storeChevronNameForElement(clickedElement, nameOfCurrentTextBox);
                }
                $("#properties_ename").val(nameOfCurrentTextBox); //add chevron name to table property automatically         
                nameForThisElement = $("#properties_ename").val();
                saveState(clickedElement);
            });
        }
    }

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
    jsPlumb.bind("connectionDrag", function(info) { //on dragging thr connections
        jsPlumb.selectEndpoints({
            element: info.sourceId
        }).setPaintStyle({
            fillStyle: "FF0000"
        });
    });
    //Adds 4 endpoints to dropped element
    function addEndPointForElement(element) {
        //create an endpoint
         //create an endpoint
        var endpointOptions = {
            isTarget: true,
            isSource: true,
            maxConnections: -1,
           
            endpoint: ["Dot", {
                radius: 4
            }],
            
            connector: ["Flowchart"],
            connectorStyle: {
                strokeStyle: "#5c96bc",
                lineWidth: 1,
                outlineColor: "transparent",
                outlineWidth: 4
            },
            connectorOverlays: [
                ["Arrow", {
                    width: 10,
                    length: 10,
                    foldback: 1,
                    location: 1,
                    id: "arrow"
                }]
            ],
            paintStyle: {
                fillStyle: "transparent"
            },
            hoverPaintStyle: {
                fillStyle: "FF0000"
            }
        };
        var endpointOptions2 = {
            isTarget: true,
            isSource: true,
            maxConnections: -1,
            anchors: ["Top"],
            endpoint: ["Dot", {
                radius: 4
            }],
            
            connector: ["Flowchart"],
            connectorStyle: {
                strokeStyle: "#5c96bc",
                lineWidth: 1,
                outlineColor: "transparent",
                outlineWidth: 4
            },
            connectorOverlays: [
                ["Arrow", {
                    width: 10,
                    length: 10,
                    foldback: 1,
                    location: 1,
                    id: "arrow"
                }]
            ],
            paintStyle: {
                fillStyle: "transparent"
            },
            hoverPaintStyle: {
                fillStyle: "FF0000"
            }
        };
        var endpointOptions3 = {
            isTarget: true,
            isSource: true,
            maxConnections: -1,
            anchors: ["Bottom"],
            endpoint: ["Dot", {
                radius: 4
            }],
            
            connector: ["Flowchart"],
            connectorStyle: {
                strokeStyle: "#5c96bc",
                lineWidth: 1,
                outlineColor: "transparent",
                outlineWidth: 4
            },
            connectorOverlays: [
                ["Arrow", {
                    width: 10,
                    length: 10,
                    foldback: 1,
                    location: 1,
                    id: "arrow"
                }]
            ],
            paintStyle: {
                fillStyle: "transparent"
            },
            hoverPaintStyle: {
                fillStyle: "FF0000"
            }
        };
        //create endpoint
        var endpointOptions1 = {
            isTarget: true,
            isSource: true,
            maxConnections: -1,
            anchors: [
                [0.98, 0.5], "Right"
            ],
            endpoint: ["Dot", {
                radius: 4
            }],
           
            connector: ["Flowchart"],
            connectorStyle: {
                strokeStyle: "#5c96bc",
                lineWidth: 1,
                outlineColor: "transparent",
                outlineWidth: 4
            },
            connectorOverlays: [
                ["Arrow", {
                    width: 10,
                    length: 10,
                    foldback: 1,
                    location: 1,
                    id: "arrow"
                }]
            ],
            paintStyle: {
                fillStyle: "transparent"
            },
            hoverPaintStyle: {
                fillStyle: "FF0000"
            }
        };
      
       jsPlumb.addEndpoint(element, endpointOptions3);
       jsPlumb.addEndpoint(element, endpointOptions1);
        jsPlumb.addEndpoint(element, endpointOptions2);
        jsPlumb.addEndpoint(element, endpointOptions3);
      
    } // function close
    // On time out/ click on canvas add chevron name to table name property
    (function($) {
        $.fn.extend({
            userInputAdded: function(callback, timeout) {
                timeout = timeout || 100e3; // 10 second default timeout
                var timeoutReference,
                    userInputAdded = function(instance) {
                        if (!timeoutReference) return;
                        timeoutReference = null;
                        callback.call(instance);
                    };
                return this.each(function(i, instance) {
                    var $instance = $(instance);
                    $instance.is(':input') && $instance.on('keyup keypress', function(e) {
                        if (e.type == 'keyup' && e.keyCode != 8) { // if user is still typing/deleting
                            return;
                        }
                        if (timeoutReference) { //if timeout is set reset the time
                            clearTimeout(timeoutReference);
                        }
                        timeoutReference = setTimeout(function() { //timeout passed auto update field name
                            userInputAdded(instance);
                        }, timeout);
                    }).on('blur', function() { // if user is leaves the field 
                        userInputAdded(instance);
                    });
                });
            }
        });
    })(jQuery);
    // clear field values 
    function clearAllFields() {
        $("#properties_ename").val("");
        $("#properties_eowner").val("");
        $("#properties_epredecessors").val("");
        $("#properties_esuccessors").val("");
        $("#properties_Associated_process_emodels").val("");
        $("#properties_eDescription").val("");
    }
    //at page load
    jsPlumb.setContainer($('#dropArea'));
    $("#fullProps").show();
    $("#elementProps").hide();
    //////////REMOVE
   function getProduct(id) {
    $.ajax({
        type: "GET",
        url: "/products/" + id,
        contentType: "application/json; charset=utf-8",
        dataType: "json"
    });
}
    ////////////
    //Save the created diagram in XML format
    function saveDiagram(callback) {
        var root = {};
        var xmlSection1 = [];
        var xmlSection2 = [];
        var diagram = [];
        var elements = [];
        var connects = [];
        var orders = [];

        // for test use
        var left1= 100;
        var top1= 270;
    
         connections.push({
          sourceId : "e1",
          targetId : "e2"
         });
         formatting.push({
           chevronId:"e1",
            positionX:left1,
            positionY:top1
         });
         chevrons.push({
            elementId:"e1",
            chevronId:"e1",
            chevronName:"step1",
            description:"This is step1",
            processModel: "process 1"
         });
         
        //
        for (var i in formatting) { // save the formatting values
            var item = formatting[i];
            connects.push({
                "chevronId": item.chevronId,
                "X": item.positionX,
                "Y": item.positionY
            });
        }
        for (var i in connections) { // save element flow
            var item = connections[i];
            orders.push({
                "sourceId": item.sourceId,
                "targetId": item.targetId,
                "sourceAnchor1": item.sourceAnchor1,
                "sourceAnchor2": item.sourceAnchor2,
                "orientation1": item.orientation1,
                "orientation2": item.orientation2,
                "targetAnchor1": item.targetAnchor1,
                "targetAnchor2": item.targetAnchor2,
                "orientation3": item.orientation3,
                "orientation4": item.orientation4
            });
        }
        for (var i in chevrons) { // save element details
            var item = chevrons[i];
            elements.push({
                "elementId": item.elementId,
                "chevronId": item.chevronId,
                "chevronName": item.chevronName,
                "description": item.description,
                "associatedAsset": item.processModel
            });
        }
        xmlSection1.push({
            mainProcess: mainProcessName,
            element: elements
        });
        xmlSection2.push({
            format: connects,
            flow: orders
        });
        diagram.push({
            chevrons: xmlSection1,
            styles: xmlSection2
        });
        root.diagram = diagram;
        var savedCanvas = JSON.stringify(root);
        var x2js = new X2JS();
        var strAsXml = x2js.json2xml_str(savedCanvas); // convert to xml


        //ajax call to save value in api
        $.ajax({
            type: "POST",
            url: "/publisher/asts/chevron/apis/chevronxml",
            data: {
                content: strAsXml,
                name: mainProcessName,
                type: "POST"
            }
            ,
        success: function() {
            callback();
        }
        });
    }
    //for test use 
    function callback(){

    }
    //Add a new row dynamically after the last available row
    function createDynamicRow() {
        var newLastId;
        var lastId = $('#canvasGrid tr:last').attr('id');
        newLastId = ++lastId;
        $('#canvasGrid tr').last().after('<tr id=' + newLastId + '><td class="canvasCell">1</td><td class="canvasCell">2</td><td class="canvasCell">3</td><td class="canvasCell">4</td></tr>');
        return newLastId;
    }
    // Decide the suitable row id based on element position
    function getMatchingGridRow(element) {
        var rowId; // store row id the element belongs to
        var elementY = parseInt(element.css("top"), 10);
      //  alert(element.css("top"));
        // Canvas y axis ranges for each row definition
        var range1 = 150;
        var range2 = 250;
        var range3 = 350;
        var range4 = 450;
        if (elementY <= range1) {
            rowId = 0;
        } else if (range1 < elementY && elementY <= range2) {
            rowId = 1;
        } else if (range2 < elementY && elementY <= range3) {
            rowId = 2;
        } else if (range3 < elementY && elementY <= range4) {
            rowId = 3;
        } else if (elementY > range4) {
            rowId = createDynamicRow();
        }
        return rowId;
    }
    // Add a new cell dynamically after the last available cell
    function addDynamicCell(elementX, element) {
        // Canvas x axis ranges for each cell definition
        var range1 = 742;
        var range2 = 752;
        var range3 = 762;
        var range4 = 772;
        if (elementX > range1 && elementX <= range2) {
            cellId = 5;
        } else if (elementX > range2 && elementX <= range3) {
            cellId = 6;
        } else if (elementX > range3 && elementX <= range4) {
            cellId = 7;
        } else if (elementX <= range1) {
            cellId = 4;
        }
        return cellId;
    }
    // Decide suitable cell id  based on the element position
    function getMatchingGridCell(row, element) {
        var cellId;
        var elementX = parseInt(element.css("left"), 10);
       // alert(element.css("left"));

        // Canvas x axis ranges for each cell definition
        var range1 = 160;
        var range2 = 335;
        var range3 = 505;
        var range4 = 640;
        if (elementX <= range1) {
            cellId = 0;
        }
        if (elementX > range1 && elementX <= range2) {
            cellId = 1;
        }
        if (elementX > range2 && elementX <= range3) {
            cellId = 2;
        }
        if (elementX > range3 && elementX <= range4) {
            cellId = 3;
        }
        if (elementX > range4) {
            cellId = addDynamicCell(elementX, element);
        }
        //alert('cellid' + cellId);
        return cellId;
    }
    //Check if suggested row/cell is free
    function checkPositionAvailability(cell, row) {
        for (var i = 0; i < occupiedGridPositions.length; i++) {
            if (cell == occupiedGridPositions[i].cell && row == occupiedGridPositions[i].row) {
                return false;
            }
        }
        return true;
    }
    // Set the element on grid with default spacing
    function setPositionForElement(row, cell, element) {
        var defaultXGap = 166;
        var defaultYGap = 117;;
        var defaultX = 15; // starting x position
        var defaultY = 17; // starting y position
        if (row == 0 && cell == 0) { // position(0,0) element
            element.css("left", defaultX);
            element.css("top", defaultY);
        } else { // if not the very first cell location
            var countRow = row;
            for (var i = 0; i < countRow; i++) //setting row position
            {
                defaultY = defaultY + defaultYGap;
            }
            var countCell = cell;
            for (var i = 0; i < countCell; i++) {
                defaultX = defaultX + defaultXGap;
            }
            element.css("left", defaultX);
            element.css("top", defaultY);
        }
    }
    // Remove cell/row combination from the grid
    function removePositionFromGrid(element) {
        var elementId = element.attr('id');
        var elementCell = getGridCellForElementId(elementId); //get current element cell
        var elementRow = getGridRowForElementId(elementId);
        removeOldGridPositionFromList(elementRow, elementCell, element);
    }
    //Remove element from the mainProcess list
    function removeFromMainProcessList(elementId) {
        if (mainProcess.length > 0) { //if it has elements 
            for (var i = 0; i < mainProcess.length; i++) {
                if (mainProcess[i].id == elementId) {
                    mainProcess.splice(i, 1); //remove element from list
                }
            }
        }
    }
    // Remove element from the xml structure storing list
    function removeFromXmlStoreList(elementId) {
        if (chevrons.length > 0) {
            for (var i = 0; i < chevrons.length; i++) {
                if (chevrons[i].chevronId == elementId) {
                    chevrons.splice(i, 1); //remove element from list
                }
            }
        }
    }
    // Remove connections attached to the given element
    function removeRelatedConnectionsFromList(element) {
        var elementId = element.attr('id');
        

        if (connections.length > 0) {
            for (var i = 0; i < connections.length; i++) {
                if (connections[i].sourceId == elementId) {
                    
                    connections.splice(i, 1); //remove element from list
                } else if (connections[i].targetId == elementId) {
                      
                    connections.splice(i, 1);
                }
            }
        }
     
        if (specializations.length > 0) {
            for (var i = 0; i < specializations.length; i++) { //clear the removed element from any relation (predecessor/successor)
                if (specializations[i].id == elementId || specializations[i].predecessorId == elementId || specializations[i].successorId == elementId) {
                    specializations.splice(i, 1);
                }
            }
        }
    }
    // remove selected element 
    function removeElement(element) {
        var id = element.find('.text-edit').attr('name');
        removeFromMainProcessList(id);
        removeFromXmlStoreList(id);
        removePositionFromGrid(element);
        removeRelatedConnectionsFromList(element);
        jsPlumb.removeAllEndpoints(element); // remove endpoints of that element
        jsPlumb.detachAllConnections(element); //remove added connections from element
        element.remove(); //remove element
    }
    // Get description value for the given element id
    function getDescriptionForElement(id) {
        var descriptionValue = "no description added";
        //retrieve the description for that element
        if (descriptionsForChevrons.length != 0) {
            for (var i = 0; i < descriptionsForChevrons.length; i++) {
                if (id == descriptionsForChevrons[i].id) {
                    descriptionValue = descriptionsForChevrons[i].description;
                    return descriptionValue;
                }
            }
        }
        return descriptionValue;
    }
    // at initial page load
    $(function() {
        $("#fullProps").tabs(); //show main process table
        $("#elementProps").tabs(); //show element table
        var url = "../chevron/apis/processes?type=process"; // url to get all associated process names
        $("#properties_Associated_process_emodels").tokenInput(url, {
            preventDuplicates: true,
            theme: "facebook",
            onResult: function(results) {
                var assets = {
                    data: []
                }
                $.each(results, function() {
                    for (var i in results) {
                        var item = results[i];
                        assets.data.push({
                            "path": item.path,
                            "id": item.id,
                            "name": item.attributes.overview_name
                        });
                    };
                });
                return assets.data;
                console.log('' + JSON.stringify(arguments));
            },
            onAdd: function(item) {
                var name = item.name;
                $("#properties_Associated_process_emodels").val(name);
                processTokens.push({
                    processId: item.id,
                    processName: item.name
                });
            },
            tokenFormatter: function(item) {
                return "<li><a href =../process/details/" + item.id + ">" + item.name + " </a></li>"
            }
        });
        //Drag icon from toolbox and place on canvas
        $(".chevron-toolbox").draggable({
            helper: 'clone',
            cursor: 'move',
            tolerance: 'fit',
            revert: true
        });
        $("#dropArea").droppable({
            accept: '.chevron-toolbox',
            activeClass: "drop-area",
            containment: 'dropArea',
            drop: function(e, ui) {
                droppedElement = ui.helper.clone();
                ui.helper.remove();
                $(droppedElement).removeAttr("class");
                $(droppedElement).addClass("chevron");
                //Adding description on/off button
                var descriptorSwitch = $('<div>').addClass('descriptor');
                descriptorSwitch.appendTo(droppedElement); //append the div to chevron element
                var gridRow = getMatchingGridRow(droppedElement); //get the suitable row for the element
                var gridCell = getMatchingGridCell(gridRow, droppedElement); //add element to suitable cell
                var addedElement = addElementToGrid(gridRow, gridCell, droppedElement); //add element to destined location
                if (addedElement) {
                    addEndPointForElement(droppedElement); // add connecting endopint to element
                    validateUnsavedProperties(); // check if previous properties for element was saved
                    var elementId = droppedElement.attr('id');
                    descriptorSwitch.attr('id', elementId); // set jsPlumb id to the descriptorswitch Id
                    droppedElement.click(divClicked);
                    // On click of descriptor switch of element give pop up
                    descriptorSwitch.popover({
                        html: true,
                        content: function() {
                            var element = $(this);
                            var currentId = element.attr('id');
                            return getDescriptionForElement(currentId);
                        }
                    }).click(function(e) {
                        e.stopPropagation();
                    });
                    droppedElement.bind("dblclick", function(e) { // remove element on double click
                        var selectedElement = $(this);
                        removeElement(selectedElement);
                    });
                }
            }
        });
    });
