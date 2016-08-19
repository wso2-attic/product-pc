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
 Functionality related to edit chevron diagram in Publisher
 */
jsPlumb.ready(function (e) {
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
    var finalSelectedItems = []; // Keep track of selected element Ids
    var addElementResults = [];
    var addElementNewResults = [];

    // associated process token store
    var assets = {
        data: []
    };
    $("#editMainProps").show();
    $("#editElementProps").hide();
    //$('#save').css('visibility', 'hidden');
    // ajax call to get the main chevron name of the diagram
    $.ajax({
        type: "GET",
        url: "/designer/assets/chevron/apis/nameStore",
        data: {
            type: "GET"
        },
        success: function (result) {
            getXmlForProcess(result);
        }
    });
    // on form submit re-save the canvas xml content
    $('#editAssetButton').click(function (e) {
        saveDiagram();
    });
    jsPlumb.bind("click", function(conn) {
        var source = conn.sourceId;
        var target = conn.targetId;
        for(var i =0; i < connections.length; i++){
            if(source == connections[i].sourceId || target == connections[i].targetId){
                connections.splice(i,1);
            }

        }

    jsPlumb.detach(conn);
});
    //show/hide connections on toggle
    $("#connectionVisibility").click(function () {
        if ($.trim($(this).text()) === 'Hide Connections') {
            $('.chevron').each(function (index) {
                jsPlumb.hide(this.id, true);
            });
            $(this).text('Show Connections');
        } else {
            $('.chevron').each(function (index) {
                jsPlumb.show(this.id, true);
            });
            $(this).text('Hide Connections');
        }
        return false;
    });
    //function to save the updated values of the diagram back in server
    function saveDiagram() {
        var root = {};
        var xmlSection1 = [];
        var xmlSection2 = [];
        var diagram = [];
        var elements = [];
        var connects = [];
        var orders = [];
        var mainProcessName = $("#properties_name").val();
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
                "textFieldId": item.chevronId,
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
        $("#xmlArea").val(x2js.json2xml_str($.parseJSON(savedCanvas)));
        //ajax call to save value in api
        $.ajax({
            type: "POST",
            url: "/designer/assets/chevron/apis/chevronxml",
            data: {
                content: strAsXml,
                name: mainProcessName,
                type: "POST"
            }
        })
    }

    //function to get the specific xml content for the given process
    function getXmlForProcess(process) {
        $.ajax({
            type: "GET",
            url: "/designer/assets/chevron/apis/chevronxml",
            data: {
                type: "GET",
                name: process
            },
            success: function (xmlResult) {
                drawDiagramOnCanvas(xmlResult, process);
            }
        });
    }

    //Check if element states are duplicted
    function checkForDuplicateState(id) {
        for (var i = 0; i < chevrons.length; i++) {
            if (chevrons[i].elementId == id) {
                return true;
            }
        }
    }

    // store description value for each element
    function storeDescriptionsForElements(id, description) {
        var edited = false;
        if (descriptionsForChevrons.length != 0) {
            for (var i = 0; i < descriptionsForChevrons.length; i++) {
                if (descriptionsForChevrons[i].id == id) { //edit record
                    descriptionsForChevrons[i].description = description;
                    edited = true;
                    return;
                }
            }
        }
        if (!edited) { // new record
            descriptionsForChevrons.push({
                id: id,
                description: description
            });
        }
    }

    //save the state of each chevron that is already drawn at page load
    saveStateOfExistingElement = function (element, mainProcess, id, name, description, process, xValue, yValue) {
        var mainProcessName = mainProcess; //state is first saved when element is dropped to canvas
        if (process == "" || process == null) {
            process = "None";
        }
        var elementId = element.attr('id'); // To identify entire element in connections
        chevrons.push({
            diagramName: mainProcessName,
            chevronId: id,
            elementId: elementId,
            chevronName: name,
            description: description,
            processModel: process
        });
        formatting.push({
            chevronId: id,
            positionX: xValue,
            positionY: yValue
        });
    }
    //function to draw retrived chevron content to canvas
    function drawDiagramOnCanvas(xmlResult, mainProcess) {
        mainProcessOfDiagram = mainProcess; // store diagram name as global
        var test = xmlResult;
        test = test.replace(/&quot;/g, '"');
        test = test.split(',');
        var jsonObj = eval("(" + test + ")");
        // get length of elements  array
        var numberOfElements = jsonObj.diagram[0].chevrons[0].element.length;
        //store existing connections
        if (jsonObj.diagram[0].styles[0].flow.length != 0) {
            for (var i = 0; i < jsonObj.diagram[0].styles[0].flow.length; i++) {
                connections.push({
                    sourceId: jsonObj.diagram[0].styles[0].flow[i].sourceId,
                    targetId: jsonObj.diagram[0].styles[0].flow[i].targetId,
                    sourceAnchor1: jsonObj.diagram[0].styles[0].flow[i].sourceAnchor1,
                    sourceAnchor2: jsonObj.diagram[0].styles[0].flow[i].sourceAnchor2,
                    orientation1: jsonObj.diagram[0].styles[0].flow[i].orientation1,
                    orientation2: jsonObj.diagram[0].styles[0].flow[i].orientation2,
                    targetAnchor1: jsonObj.diagram[0].styles[0].flow[i].targetAnchor1,
                    targetAnchor2: jsonObj.diagram[0].styles[0].flow[i].targetAnchor2,
                    orientation3: jsonObj.diagram[0].styles[0].flow[i].orientation3,
                    orientation4: jsonObj.diagram[0].styles[0].flow[i].orientation4
                });
            }
        }
        //draw elements 
        for (var i = 0; i < numberOfElements; i++) {
            var chevronId = jsonObj.diagram[0].chevrons[0].element[i].elementId;
            var chevronName = jsonObj.diagram[0].chevrons[0].element[i].chevronName;
            var chevronDescription = jsonObj.diagram[0].chevrons[0].element[i].description;
            var positionX = jsonObj.diagram[0].styles[0].format[i].X;
            var positionY = jsonObj.diagram[0].styles[0].format[i].Y;
            var process = jsonObj.diagram[0].chevrons[0].element[i].associatedAsset;
            storePropertiesOfChevron(chevronId, chevronName, chevronDescription, process); //save properties of each chevron
            var element1 = $('<div>').addClass('chevron');
            var textField = $('<textArea>').attr({
                name: chevronId
            }).addClass("text-edit");
            var descriptorSwitch = $('<div>').addClass('descriptor');
            textField.val(chevronName);
            element1.append(textField);
            element1.append(descriptorSwitch);
            element1.find('.text-edit').position({ // position text box in the center of element
                my: "center",
                at: "center",
                of: element1
            });
            var textFieldTop = 19;
            var textFieldLeft = 40;
            element1.find('.text-edit').css({
                'top': textFieldTop,
                'left': textFieldLeft,
                'visibility': 'visible'
            });
            element1.css({
                'top': positionY,
                'left': positionX
            });
            $('#canvasArea').append(element1);
           addEndPointForElement(element1);
            descriptorSwitch.attr('id', chevronId); //set element id for description button
            storeDescriptionsForElements(chevronId, chevronDescription);
            jsPlumb.setId(element1, chevronId);
            saveStateOfExistingElement(element1, mainProcess, chevronId, chevronName, chevronDescription, process, positionX, positionY); // save initial state of each chevron
            viewForName.push(chevronName);
            var row = getAlignedGridRow(positionY);
            var cell = getMatchingGridCellForDragged(row, element1);
            storeLocationOfElement(chevronId, row, cell);
            storeChevronNameForElement(element1, chevronName); //To be used in deriving predecessor/successors
            element1.click(chevronClicked);
            element1.dblclick(removeElementFromCanvas);
            // Display description for chevron as a popup
            descriptorSwitch.popover({
                html: true,
                content: function () {
                    var element = $(this);
                    var currentId = element.attr('id');
                    return getDescriptionForElement(currentId);
                }
            }).click(function (e) {
                e.stopPropagation();
            });
        }
        drawConnectionsForElements(); //Draw existing connections for elements
        // loadingDiagram = false; // diagram loading is done
    }

    //Draw existing connections for elements
    function drawConnectionsForElements() {
        loadingDiagram = true;
        chevronList = document.querySelectorAll(".chevron");
        if (connections.length != 0) {
            for (var i = 0; i < connections.length; i++) {
                var source = getMatchedElement(connections[i].sourceId);
                var target = getMatchedElement(connections[i].targetId);
                var p0 = connections[i].sourceAnchor1;
                var p1 = connections[i].sourceAnchor2;
                var p2 = connections[i].orientation1;
                var p3 = connections[i].orientation2;
                var p4 = connections[i].targetAnchor1;
                var p5 = connections[i].targetAnchor2;
                var p6 = connections[i].orientation3;
                var p7 = connections[i].orientation4;
                //   var dynamicAnchors = [ [p0,p1,p2,p3],[p4,p5,p6,p7]];
                var common = {
                    isTarget: true,
                    isSource: true,
                    maxConnections: -1,
                    connector: ["Flowchart"],
                    connectorStyle: {
                        strokeStyle: "#5c96bc",
                        lineWidth: 1,
                        outlineColor: "transparent",
                        outlineWidth: 4
                    },
                    anchor: [
                        [p0, p1, p2, p3],
                        [p4, p5, p6, p7]
                    ],
                    endpoint: ["Dot", {
                        radius: 4
                    }]
                };
                jsPlumb.connect({
                    source: source,
                    target: target,
                    paintStyle: {
                        strokeStyle: "#5c96bc",
                        lineWidth: 1
                    },
                    overlays: [
                        ["Arrow", {
                            width: 10,
                            length: 10,
                            foldback: 1,
                            location: 1,
                            id: "arrow"
                        }]
                    ],
                    endpointStyle: {
                        fillStyle: "transparent"
                    }
                }, common);
                addEndPointForElement(source);
                addEndPointForElement(target);


            }
           
        }
         loadingDiagram = false;
    }

    //return element for  given id
    function getMatchedElement(id) {
        for (var i = 0; i < chevronList.length; i++) {
            if (id == chevronList[i].childNodes[0].name) {
                return chevronList[i];
            }
        }
    }

    //store element properties
    function storePropertiesOfChevron(id, name, description, process) {
        var existingElement = false;
        if (process == "" || process == null) {
            process = "None";
        }
        if (chevronProperties.length != 0) {
            for (var i = 0; i < chevronProperties.length; i++) {
                if (id == chevronProperties[i].id) { // its an edit of a record
                    chevronProperties[i].name = name;
                    chevronProperties[i].process = process;
                    chevronProperties[i].description = description;
                    existingElement = true;
                }
            }
        }
        if (!existingElement) {
            chevronProperties.push({
                id: id,
                name: name,
                description: description,
                process: process
            });
        }
    }

    // on double click of the canvas display diagram details
    $('#canvasArea').dblclick(function (e) {
        $("#editMainProps").show();
        $("#editElementProps").hide();
    });
    //Display added properties for a view
    function viewElementProperties(propertyList, specializationList, name) {
        for (var i = 0; i < propertyList.length; i++) {
            if (propertyList[i].name == name) { // if name of chevron found
                var checkId = propertyList[i].id;
                var processId = getProcessIdForProcessModel(propertyList[i].process);
                $("#td_name1").html(propertyList[i].name);
                $("#td_description").val(propertyList[i].description);
                $("#td_mod").tokenInput("clear").tokenInput("add", {
                    id: processId,
                    name: propertyList[i].process
                });
                if (specializationList.length > 0) { // if predecessor/successor exists for chevron
                    var predecessors = getPredecessorsForElement(checkId);
                    var successors = getSuccessorsForElement(checkId);
                    $("#td_predecessor1").html(predecessors);
                    $("#td_successor1").html(successors);
                }
            }
        }
    }

    function updateStateOfElement(name, description, process) {
        for (var i = 0; i < chevrons.length; i++) {
            if (chevrons[i].chevronName == name) { //match found
                chevrons[i].description = description;
                chevrons[i].processModel = process;
                return chevrons[i].elementId;
            }
        }
    }

    //clear property fields
    function clearAllFields() {
        $("#td_predecessor1").html("");
        $("#td_successor1").html("");
        $("#td_name1").html("");
        $("#td_mod").html("");
        $("#td_description").val("");
    }

    //save properties for new element on save button click
    $('#save').click(function () {
        id = iconId; // current element id
        var name = $("#td_name1").html();
        var process = $("#td_mod").val();
        var description = $("#td_description").val();
        storePropertiesOfChevron(id, name, description, process);

        processModelChanged = true;
        var elementId = updateStateOfElement(name, description, process); //update state with new values
        storeDescriptionsForElements(elementId, description);
        viewForName.push(name);
        processModelChanged = false;
        $("#editElementProps").hide();
        isNewElement = false; //let element properties be viewed
    });
    // get suitable row id based on element position
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

    // get suitable cell id based on element position
    function getMatchingGridCell(row, element) {
        var cellId;
        var elementX = parseInt(element.css("left"), 10);
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
        // alert(cellId);
        return cellId;
    }

    // get suitable cell id based on element position
    function getMatchingGridCellForDragged(row, element) {
        var cellId;
        var elementX = parseInt(element.css("left"), 10);
        // Canvas x axis ranges for each cell definition
        var range1 = 15;//15
        var range2 = 181;// 181
        var range3 = 347; //347
        var range4 = 513; //513
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
        // alert(cellId);
        return cellId;
    }

    // Set incremental ids for element textboxes that are added to the canvas
    function setIdsForElements(defaultId, element) {
        if (defaultId == 0 && elementsOnCanvas.length == 0) //first element
        {
            isFirstElement = true;
            defaultId = ++count;
            elementsOnCanvas.push(defaultId);
            element.find('.text-edit').attr('name', defaultId);
        }
        if (defaultId == 0 && elementsOnCanvas.length !== 0) //not first element 
        {
            var lastLocation = elementsOnCanvas.length;
            var temp = elementsOnCanvas[lastLocation - 1];
            defaultId = temp + 1;
            elementsOnCanvas.push(defaultId);
            element.find('.text-edit').attr('name', defaultId);
        }
    }

    //store coordinates of each chevron position
    function setChevronPositions(element) {
        var alreadyAdded = false;
        var name = "empty";
        var id = element.attr('id');
        var xValue = parseInt(element.css("left"), 10);
        var yValue = parseInt(element.css("top"), 10);
        if (chevronPositions.length != 0) {
            for (var i = 0; i < chevronPositions.length; i++) {
                if (chevronPositions[i].chevId == id) { //already added
                    alreadyAdded = true;
                    return;
                }
            }
        }
        if (!alreadyAdded) {
            chevronPositions.push({
                chevId: id,
                chevName: name,
                xVal: xValue,
                yVal: yValue
            });
        }
    }

    //store new position where element was dragged
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

    //remove old element position from list
    function removeOldGridPositionFromList(originalRow, originalCell,id) {
       // var id = element.attr('id');
        for (var i = 0; i < occupiedGridPositions.length; i++) {
            if (occupiedGridPositions[i].id == id) {
                occupiedGridPositions.splice(i, 1);
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

    //When element name is edited update successor/predecssors accordingly
    function updateNameInRelations(id, name, prevName) {
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].id == id) {
                updateRecordAsSuccessor(specializations[i].predecessorId, name, prevName);
                updateRecordAsPredecessor(specializations[i].successorId, name, prevName);
            }
        }
    }

    //If new position is already occupied move element back to original
    function revertToOldPosition(clickedElement, originalXPosition, originalYPosition) {
        clickedElement.css("left", originalXPosition);
        clickedElement.css("top", originalYPosition);
    }

    //check if element was moved from its current position
    function checkForDragChange(element) {
        var id = element.attr('id');
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

    // Save state of the element
    var saveState = function (chevron) {
        var processModel = "not declared"; //state is first saved when element is dropped to canvas
        var chevronId1 = chevron.find('.text-edit').attr('name');
        var elementId = chevron.attr('id'); // To identify entire element in connections
        var chevronName = chevron.find(".text-edit").val();
        var description = $("#td_description").val();
        var positionX = parseInt(chevron.css("left"), 10);
        var positionY = parseInt(chevron.css("top"), 10);
        storeDescriptionsForElements(elementId, description); //save description id pairs for tooltip view
        if (stateDragged || editName || processModelChanged) // if position changed. update
        {
            for (var i = 0; i < chevrons.length; i++) {
                if (chevrons[i].chevronId == chevronId1) // if its an existing element
                {
                    for (var j = 0; j < chevronProperties.length; j++) // get process model for chevron
                    {
                        if (chevronProperties[j].id == chevronId1) {
                            processModel = chevronProperties[j].process;
                        }
                    }
                    chevrons[i].processModel = processModel;
                    chevrons[i].description = description;
                    if (stateDragged) {
                        formatting[i].positionX = positionX;
                        formatting[i].positionY = positionY;
                    } else {
                        updateNameInRelations(elementId, chevronName, chevrons[i].chevronName); // Update the name in specializations as well
                        chevrons[i].chevronName = chevronName;
                        viewForName.push(chevronName);
                    }
                }
            }
            stateDragged = false;
        } else {
            var duplicated = false;
            duplicated = checkForDuplicateState(elementId);
            if (!duplicated) {
                chevrons.push({
                    diagramName: mainProcessOfDiagram,
                    elementId: elementId,
                    chevronId: chevronId1,
                    chevronName: chevronName,
                    description: description,
                    processModel: processModel
                });
                formatting.push({
                    chevronId: elementId, //chevronId1
                    positionX: positionX,
                    positionY: positionY
                });
            }
        }
    }
    // update chevron position with new coordinates
    function updateChevronPositions(element) {
        var id = element.attr('id');
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

    //add ,found element as a successor for current element
    function addElementAsSuccessor(element, foundElementId) {
        var defaultPredecessor = "not declared";
        var successorAlreadyAdded = false;
        var currentId = element.find('.text-edit').attr('name'); //get current element's id
        var successor = getChevronNameForId(foundElementId); //get name text of the found element
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].id == currentId) { //this element has previous records
                if (specializations[i].successor == successor) { //if this predecessor is already added
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

    //return the chevron name of given element
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

    // Storing  newly drawn connection arrays
    jsPlumb.bind('connection', function (info) {
        var endpoints = info.connection.endpoints; //get endpoints of the connection
        if (!loadingDiagram) {
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
            });
        }
    });
    //add found element as a predecessor for current element
    function addElementAsPredecessor(element, foundElementId) {
        var defaultSuccessor = "not declared";
        var predecessorAlreadyAdded = false;
        var currentId = element.find('.text-edit').attr('name'); //get current element's id 
        var predecessor = getChevronNameForId(foundElementId); //get name text of the found element
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].id == currentId) { //this element has previous records
                if (specializations[i].predecessor == predecessor) { //if this predecessor is already added
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

    //Find the pre/suc relationships for element
    function addRelationsForElement(element) {
        var elementId = element.attr('id');
        var currentElementCell = getGridCellForElementId(elementId); //get current element cell
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
                if (connectedElementId !== 0 && currentElementCell > connectedElementCell) { //current found a preceding
                    addElementAsPredecessor(element, connectedElementId); //add found element as a predecessor
                }
                if (connectedElementId !== 0 && currentElementCell < connectedElementCell) { //current found a succeeding
                    addElementAsSuccessor(element, connectedElementId); //add found element as a successor
                }
            } //end of for loop
        }
    }

    //display properties for chevron if it was not displayed before
    function viewPropertyForNewChevron(chevronText) {
        if (chevronText == $("#td_name1").html()) { //if name is same as before
            if (previousName !== chevronText) {
                previousName = chevronText;
                viewElementProperties(chevronProperties, specializations, chevronText);
            }
        }
        if (chevronText !== $("#td_name1").val()) {
            if (previousName !== chevronText) {
                previousName = chevronText;
                viewElementProperties(chevronProperties, specializations, chevronText);
            }
        }
    }

    //update values of chevronProperties list at edits
    function updateChevronPropertyList(element) {
        var elementId = element.attr('id'); // To identify entire element in connections
        var chevronName = element.find(".text-edit").val();
        for (var i = 0; i < chevronProperties.length; i++) {
            if (chevronProperties[i].id == elementId) {
                chevronProperties[i].name = chevronName;
                return;
            }
        }
    }

    //on click of a chevron
    function chevronClicked() {
        $("#editMainProps").hide();
        $("#editElementProps").show();
        $("#td_mod").html("");
        var isFirstElement = false;
        var clickedElement = $(this);
        var selectedId = clickedElement.attr('id');
        finalSelectedItems.push(selectedId); // For delete functionality

        setChevronPositions(clickedElement); //save position details for chevron
        // element values before a drag and drop
        var originalXPosition = parseInt(clickedElement.css("left"), 10);
        var originalYPosition = parseInt(clickedElement.css("top"), 10);
        var originalRow = getAlignedGridRow(originalYPosition);
        var originalCell = getMatchingGridCell(originalRow, clickedElement);
        //store taken positions and ids 
        storeLocationOfElementAtClick(clickedElement, originalRow, originalCell); //store grid position that is occupied
        clickedElement.find('.text-edit ').position({ // position text box in the center of element
            my: "center",
            at: "center",
            of: clickedElement
        });
        clickedElement.find('.text-edit  ').css('visibility', 'visible').css('background-color', 'white').focus();
        var defaultId = clickedElement.find('.text-edit').attr('name');
        var testName = clickedElement.find('.text-edit').text();
        iconId = defaultId; //setting current id of the element
        setIdsForElements(defaultId, clickedElement); //set ids for elements that are added to canvas
        // make element draggable  
        jsPlumb.draggable(clickedElement, {
            containment: 'parent',
            stop: function (event) {
                var dragState = checkForDragChange(clickedElement);
                if (dragState) { //only if the element was moved to a new location
                    var latestPositionY = parseInt(clickedElement.css("top"), 10);
                    var updatedRow = getAlignedGridRow(latestPositionY);
                    var updatedCell = getMatchingGridCellForDragged(updatedRow, clickedElement);
                    var availability = checkPositionAvailability(updatedCell, updatedRow); // check availability of the new position
                    if (!availability) {
                        revertToOldPosition(clickedElement, originalXPosition, originalYPosition); //send the element back to old position
                    } else {
                        var id = clickedElement.attr('id');
                        removeOldGridPositionFromList(originalRow, originalCell,id); // remove previous grid row/cell from stored list
                        setPositionForElement(updatedRow, updatedCell, clickedElement); //set the new position coordinates of the element
                        addNewGridPositionToList(updatedRow, updatedCell, clickedElement);
                        stateDragged = true;
                        saveState(clickedElement);
                        updateChevronPositions(clickedElement); // update position changes
                    }
                }
            }
        });
        var textValue = clickedElement.find('.text-edit ').val();
        if ($.inArray(textValue, viewForName) > -1) // if properties already added for element
        //show the view with values
        {
            clickedElement.find('.text-edit').css('background-color', '#FFCC33');
            addRelationsForElement(clickedElement); //Add predecessor/successors for element
            $("#elementProps").show();
            viewPropertyForNewChevron(textValue); // display properties for the chevron 
            $("#fullProps").hide();
        } else { // let user add properties for new element
            clearAllFields();
            $("#fullProps").hide();
            $("#elementProps").show();
            clickedElement.find('.text-edit').css('background-color', 'white').focus();
        } // user is finished adding chevron name
        clickedElement.find('.text-edit').userInputAdded(function () {
            // alert('in');
            if (!isNewElement) { // If exisiting element
                editName = true;
                saveState(clickedElement);
                updateChevronPropertyList(clickedElement);
            }
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
            var nameOfCurrentTextBox = clickedElement.find('.text-edit').css('background-color', '#FFCC33').val();
            if (nameOfCurrentTextBox) //if not empty
            {
                storeChevronNameForElement(clickedElement, nameOfCurrentTextBox);
            }
            $("#td_name1").html(nameOfCurrentTextBox); //add chevron name to table property automatically         
            nameForThisElement = $("#td_name1").html();
            saveState(clickedElement);
            currentElement = clickedElement; // temp storage of element to retrieve on save properties
        });
    }

    // On time out/ click on canvas add chevron name to table name property
    (function ($) {
        $.fn.extend({
            userInputAdded: function (callback, timeout) {
                timeout = timeout || 100e3; // 10 second default timeout
                var timeoutReference,
                    userInputAdded = function (instance) {
                        if (!timeoutReference) return;
                        timeoutReference = null;
                        callback.call(instance);
                    };
                return this.each(function (i, instance) {
                    var $instance = $(instance);
                    $instance.is(':input') && $instance.on('keyup keypress', function (e) {
                        if (e.type == 'keyup' && e.keyCode != 8) { // if user is still typing/deleting
                            return;
                        }
                        if (timeoutReference) { //if timeout is set reset the time
                            clearTimeout(timeoutReference);
                        }
                        timeoutReference = setTimeout(function () { //timeout passed auto update field name
                            userInputAdded(instance);
                        }, timeout);
                    }).on('blur', function () { // if user is leaves the field
                        userInputAdded(instance);
                    });
                });
            }
        });
    })(jQuery);
    // replace process name to a linkable text
    function replaceProcessText(processName) {
        $.ajax({
            type: "GET",
            url: "../apis/processes?type=process",
            data: {
                q: processName
            },
            success: function (Result) {
                setIdOfProcess(Result, processName);
            }
        });
    }

    //get process link id for the given process
    function setIdOfProcess(results, processName) {
        var obj = eval("(" + results + ")");
        for (var i in obj) {
            var item = obj[i];
            processId = item.id;
        }
        $("#td_mod").tokenInput("clear");
        $("#td_mod").tokenInput("add", {
            id: processId,
            name: processName
        });
    }

    //create new row to position the element as requested
    function createDynamicRow() {
        var newLastId;
        var lastId = $('#canvasGrid tr:last').attr('id');
        newLastId = ++lastId;
        $('#canvasGrid tr').last().after('<tr id=' + newLastId + '><td class="canvasCell">1</td>' +
            '<td class="canvasCell">2</td><td class="canvasCell">3</td><td class="canvasCell">4</td></tr>');
        return newLastId;
    }

    //get matching row id for new element on canvas
    function getMatchingGridRow(element) {
        var rowId; // store row id the element belongs to
        var elementY = parseInt(element.css("top"), 10);
        // Canvas y axis ranges for each row definition
        var range1 = 294; 
        var range2 = 395; 
        var range3 = 495; 
        var range4 = 595; 
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

    //add dynamic cell to position element as requested
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

    //get matching grid row id to position element
    function getMatchingGridCell(row, element) {
        var cellId;
        var elementX = parseInt(element.css("left"), 10);
        // Canvas x axis ranges for each cell definition
        var range1 = 198; //15
        var range2 = 375; // 181
        var range3 = 450;// 347
        var range4 = 560; //513
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
        return cellId;
    }

    //check if requested cell and row are available
    function checkPositionAvailability(cell, row) {
        for (var i = 0; i < occupiedGridPositions.length; i++) {
            if (cell == occupiedGridPositions[i].cell && row == occupiedGridPositions[i].row) {
                return false;
            }
        }
        return true;
    }

    //add new element to required cell location
    function addElementToGrid(row, cell, element) {
        var proceed = false;
        var available = checkPositionAvailability(cell, row);
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
        if (available) { // if the suggested position is not occupied
            element.appendTo('#canvasArea'); //add element to canvas
            setPositionForElement(row, cell, element); //set css top/left values 
            if(addElementResults.length == 0){
                addElementResults.push({
                    result:'true',
                    newCell:'none'
                });
            }
            return addElementResults;
        }
        else if(proceed){
             var result = addElementToGrid(row,newCell,element);
                 if(result){
                    if(addElementNewResults.length == 0){
                   addElementNewResults.push({
            result:'true',
            newCell:newCell
           });
               }
                    
                 }
              return addElementNewResults;
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
        if ((predecessorList.match(/,/g) || []).length > 1) { //more than 1 predecessor
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

    //retrieve successors for given element
    function getSuccessorsForElement(id) {
        var successorList = '';
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].id == id) { //element found
                successorList += specializations[i].successor + ',';
            }
        }
        if ((successorList.match(/,/g) || []).length > 1) { //more than 1 predecessor
            if ((successorList.match(/,/g) || []).length !== (successorList.match(/not declared/g) || []).length) {
                while ((successorList.match(/not declared/g) || []).length) { //if it has a not declared
                    successorList = successorList.replace('not declared,', '');
                }
            } else { //all values of successor is not declared
                successorList = "not declared";
            }
        }
        var lastChar = successorList.slice(-1); //get last character
        if (lastChar == ',') {
            successorList = successorList.slice(0, -1); //remove trailing comma
        }
        return successorList;
    }

    //  Store the grid occupied locations of all  existing elements binded to  click event
    function storeLocationOfElementAtClick(element, row, cell) {
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
        } else { //for very first element
            occupiedGridPositions.push({ //save the taken cell position of the grid
                id: elementId,
                row: row,
                cell: cell
            });
        }
    }

    //Keep track of occupied cell location at element drop on canvas
    function storeLocationOfElement(elementId, row, cell) {
        //var elementId = element.attr('id');
        var elementAdded = false;
        if (occupiedGridPositions.length !== 0) { //if not first element
            for (var i = 0; i < occupiedGridPositions.length; i++) {
                if (occupiedGridPositions[i].row == row && occupiedGridPositions[i].cell == cell) {
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
        } else { //for very first element
            occupiedGridPositions.push({ //save the taken cell position of the grid
                id: elementId,
                row: row,
                cell: cell
            });
        }
    }

    //If cell isn't occupied position new element with default spacing
    function setPositionForElement(row, cell, element) {
        var defaultXGap = 166;
        var defaultYGap = 117;
        ;
        var defaultX = 15;
        var defaultY = 17;
        if (row == 0 && cell == 0) { // position(0,0) element
            element.css("left", defaultX);
            element.css("top", defaultY);
        } else { // if not the very first cell location
            countRow = row;
            for (var i = 0; i < countRow; i++) //setting row position
            {
                defaultY = defaultY + defaultYGap;
            }
            countCell = cell;
            for (var i = 0; i < countCell; i++) {
                defaultX = defaultX + defaultXGap;
            }
            element.css("left", defaultX);
            element.css("top", defaultY);
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

    //Adds four endpoints to dropped element
    function addEndPointForElement(element) {
        //create an endpoint
        var endpointOptions = {
            isTarget: true,
            isSource: true,
            maxConnections: -1,
            anchors: [
                [0.2, 0.5],
                "Left",
            ],
            endpoint: ["Dot", {
                radius: 4
            }],
            cssClass: "chevronEndPoint",
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
            cssClass: "chevronEndPoint",
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
                [0.98, 0.5],
                "Right"
            ],
            endpoint: ["Dot", {
                radius: 4
            }],
            cssClass: "chevronEndPoint",
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
        jsPlumb.addEndpoint(element, endpointOptions);
        jsPlumb.addEndpoint(element, endpointOptions1);
        jsPlumb.addEndpoint(element, endpointOptions2);
        jsPlumb.addEndpoint(element, endpointOptions3);
    } // function close
    // Get the description value for given element id 
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

// Remove cell/row combination from the grid
    function removePositionFromGrid(elementId) {
      //  var elementId = element.attr('id');
        var elementCell = getGridCellForElementId(elementId); //get current element cell
        var elementRow = getGridRowForElementId(elementId);
        removeOldGridPositionFromList(elementRow, elementCell, elementId);
    }

    //Remove element from the mainProcess list
    function removeFromMainProcessList(elementId) {
        if (chevronProperties.length > 0) { //if it has elements 
            for (var i = 0; i < chevronProperties.length; i++) {
                if (chevronProperties[i].id == elementId) {
                    chevronProperties.splice(i, 1); //remove element from list
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
        if(formatting.length > 0){
            for(var i = 0; i < formatting.length; i++){
                if(formatting[i].chevronId == elementId){
                    formatting.splice(i,1);
                }
            }
        }
    }

    // Remove connections attached to the given element
    function removeRelatedConnectionsFromList(elementId) {
      //  var elementId = element.attr('id');
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
                if (specializations[i].id == elementId || specializations[i].predecessorId == elementId
                    || specializations[i].successorId == elementId) {
                    specializations.splice(i, 1);
                }
            }
        }
    }

    //invoke double-click on the element
    function removeElementFromCanvas() {
        var selectedElement = $(this);
        removeElement(selectedElement);

    }

     //remove selected element when button is clicked
       $('#btn-delete').click(function (e) {
         var deleteElementId = getSelectedToDelete();
          var element = document.getElementById(deleteElementId);
         removeElement(element);
         finalSelectedItems.pop();
    });
       function getSelectedToDelete(){
      var selectedElementId = finalSelectedItems[finalSelectedItems.length-1];
        return selectedElementId;
       }
      

    // remove selected element
    function removeElement(element) {
     var id = element.attributes.id.nodeValue;
        removeFromMainProcessList(id);
        removeFromXmlStoreList(id);
        removePositionFromGrid(id);
        removeRelatedConnectionsFromList(id);
        jsPlumb.removeAllEndpoints(element); // remove endpoints of that element
        jsPlumb.detachAllConnections(element); //remove added connections from element
        element.remove(); //remove element
        clearAllFields();
    }


    //At connection drag event show selected endpoints
    jsPlumb.bind("connectionDrag", function (info) {
        jsPlumb.selectEndpoints({
            element: info.sourceId
        }).setPaintStyle({
            fillStyle: "FF0000"
        });
    });
    $(function () {
        //Drag icon from toolbox and place on canvas
        $(".chevron-toolbox").draggable({
            helper: 'clone',
            cursor: 'move',
            tolerance: 'fit',
            revert: true
        });
        $("#canvasArea").droppable({
            accept: '.chevron-toolbox',
            activeClass: "canvas-area",
            containment: 'canvasArea',
            drop: function (e, ui) {
                newElement = ui.helper.clone();
                ui.helper.remove();
                $(newElement).removeAttr("class");
                $(newElement).addClass("chevron");
                //Adding description on/off button
                var descriptorSwitch = $('<div>').addClass('descriptor');
                descriptorSwitch.appendTo(newElement); //Adding the button to the element
                var gridRow = getMatchingGridRow(newElement); //get the suitable row for the element
                var gridCell = getMatchingGridCell(gridRow, newElement); //add element to suitable cell
                var addedElement = addElementToGrid(gridRow, gridCell, newElement); //add element to destined location
                if (addedElement) {
                    addEndPointForElement(newElement); // add connecting endopint to element
                    clearAllFields();
                    isNewElement = true; //new element added to canvas
                    //   $('#save').css('visibility', 'visible'); //show property save button
                    newElement.dblclick(removeElementFromCanvas);
                    newElement.click(chevronClicked);

                    var elementId = newElement.attr('id');
                    if(addedElement[0].newCell == "none"){
                    storeLocationOfElement(elementId,gridRow,gridCell);
                }
                else{
                    storeLocationOfElement(elementId,gridRow,addedElement[0].newCell);
                }
                    descriptorSwitch.attr('id', elementId); //set default jsplumb id to descriptor button
                    // show description value as a popup at click
                    descriptorSwitch.popover({
                        html: true,
                        content: function () {
                            var element = $(this);
                            var currentId = element.attr('id');
                            return getDescriptionForElement(currentId);
                        }
                    }).click(function (e) {
                        e.stopPropagation(); // hold parent click events
                    });

                }
            }
        });
        var url = "../apis/processes?type=process"; // url to derive all existing process models
        $("#td_mod").tokenInput(url, {
            preventDuplicates: true,
            theme: "facebook",
            onResult: function (results) {
                $.each(results, function () {
                    for (var i in results) {
                        var item = results[i];
                        assets.data.push({
                            "path": item.path,
                            "id": item.id,
                            "name": item.attributes.overview_name
                        });
                    }
                    ;
                });
                return assets.data;
                console.log('' + JSON.stringify(arguments));
            },
            onAdd: function (item) {
                var name = item.name;
                $("#td_mod").val(name);
                processTokens.push({
                    processId: item.id,
                    processName: item.name
                });
            },
            tokenFormatter: function (item) {
                return "<li><a href =../../../assets/process/details/" + item.id + ">" + item.name + " </a></li>"
            }
        });
    });
});