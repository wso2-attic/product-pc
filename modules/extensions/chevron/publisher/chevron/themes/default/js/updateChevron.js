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
jsPlumb.ready(function(e) {
    chevronProperties = []; //store properties of each chevron
    var processId = 0; // store current process id of the chevron clicked  
    var specializations = []; //store successor/predecessor relations for a chevron
    var chevrons = []; //save element props for later
    var formatting = []; //save layout of elements for later
    var nameEdit = false; // if the name of chevron edited
    var stateDragged = false; // if the chevron was moved in canvas
    var relationPositions = []; // to hold chevron position changes to derive relationships
    var relationsReset = false; //check if pre/successor relationships got updated
    var editName = false; //check if chevron name got edited
    var positionChanged = false; //check for position updates
    var idList = []; // list of chevron ids on canvas
    var isNewElement; // adding new element to existing diagram
    var newElementPosX;
    var newElementPosY;
    var newElementProcess; // process property for new element
    var lastXCoordinate;
    var lastYCoordinate; // store last element position values for alignment 
    var notNewElement = false;
    var predecessorCount = 0; // To keep track of the very first element's predecessor
    var flowList = []; //store original position order of elements
    var canvasElementList = []; //store alignment properties of all the chevrons
    var numOfCanvasElements = 0; // Keep count of all the elements currently on canvas
    var previousName; // To store the previous chevron name to display the view if needed
    // associated process token store
    var assets = {
        data: []
    };
    var currentId; //current clicked chevron's Id
    $("#editMainProps").show();
    $("#editElementProps").hide();
    $('#save').css('visibility', 'hidden');
    // ajax call to get the main chevron name of the diagram
    $.ajax({
        type: "GET",
        url: "/publisher/asts/chevron/apis/nameStore",
        data: {
            type: "GET"
        },
        success: function(result) {
            getXmlForProcess(result);
        }
    });
    // on form submit re-save the canvas xml content
    $('#editAssetButton').click(function(e) {
        saveDiagram();
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
        for (var i in specializations) { // save element flow
            var item = specializations[i];
            orders.push({
                "sourceId": item.id,
                "targetId": item.successorId
            });
        }
        for (var i in chevrons) { // save element details
            var item = chevrons[i];
            elements.push({
                "chevronId": item.chevronId,
                "chevronName": item.chevronName,
                "associatedAsset": item.processModel
            });
        }
        xmlSection1.push({
            mainProcess: mainProcessName,
            element: elements,
            flow: orders
        });
        xmlSection2.push({
            format: connects
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
            url: "/publisher/asts/chevron/apis/chevronxml",
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
            url: "/publisher/asts/chevron/apis/chevronxml",
            data: {
                type: "GET",
                name: process
            },
            success: function(xmlResult) {
                drawDiagramOnCanvas(xmlResult, process);
            }
        });
    }
    //save the state of each chevron
    var saveState = function(mainProcess, id, name, process, xValue, yValue) {
        var mainProcessName = mainProcess; //state is first saved when element is dropped to canvas
        if (process == "" || process == null) {
            process = "None";
        }
        chevrons.push({
            diagramName: mainProcessName,
            chevronId: id,
            chevronName: name,
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
            var test = xmlResult;
            test = test.replace(/&quot;/g, '"');
            test = test.split(',');
            var jsonobj = eval("(" + test + ")");
            // get length of elements  array
            var numberOfElements = jsonobj.diagram[0].chevrons[0].element.length;
            //draw elements 
            for (var i = 0; i < numberOfElements; i++) {
                var chevronId = jsonobj.diagram[0].chevrons[0].element[i].chevronId;
                var chevronName = jsonobj.diagram[0].chevrons[0].element[i].chevronName;
                var source = jsonobj.diagram[0].chevrons[0].flow[i].sourceId;
                var successor = jsonobj.diagram[0].chevrons[0].flow[i].targetId;
                var positionX = jsonobj.diagram[0].styles[0].format[i].X;
                var positionY = jsonobj.diagram[0].styles[0].format[i].Y;
                var process = jsonobj.diagram[0].chevrons[0].element[i].associatedAsset;
                
                storePropertiesOfChevron(chevronId, chevronName, process); //store each chevron property
                var element1 = $('<div>').attr('id', 'chevronId').addClass('chevron');
                var textField = $('<textArea>').attr({
                    name: chevronId
                }).addClass("chevron-textField");
                textField.val(chevronName);
                element1.append(textField);
                element1.find('.chevron-text').position({ // position text box in the center of element
                    my: "center",
                    at: "center",
                    of: element1
                });
                element1.css({
                    'top': positionY,
                    'left': positionX
                });
                $('#canvasArea').append(element1);
                saveState(mainProcess, chevronId, chevronName, process, positionX, positionY); // save initial state of each chevron
                setPositionsForRelations(element1); // to keep track of pre/suc relationships when position is changed.
                //TO DO 
                flowList.push(source);
                flowList.push(successor);
                idList.push(chevronId);
                element1.click(chevronClicked);
            }
            for (var i = 0; i < flowList.length; i++) {
                first = flowList[i];
                second = flowList[++i];
                setPredecessor(second, first);
            }
            for (var k = 0; k < flowList.length; k++) {
                first = flowList[k];
                second = flowList[++k];
                setSuccessor(first, second);
            }
        }
        //store position coordinates for the chevron
        function setPositionsForRelations(element) {
            var id = element.find('.chevron-textField').attr('name');
            var name = element.find('.chevron-textField').text();
            var xValue = parseInt(element.css("left"), 10);
            var yValue = parseInt(element.css("top"), 10);
            relationPositions.push({
                chevId: id,
                chevName: name,
                xVal: xValue,
                yVal: yValue
            });
        }
        //get relevant process name for the chevron
        function getProcessForChevron(id) {
            var nameOfProcess;
            for (var i = 0; i < chevronProperties.length; i++) {
                if (id == chevronProperties[i].id) {
                    nameOfProcess = chevronProperties[i].process;
                    replaceProcessText(nameOfProcess); // replace process name as a link
                }
            }
        }

        function storePropertiesOfChevron(id, name, process) {
            if (process == "" || process == null) {
                process = "None";
            }
            chevronProperties.push({
                id: id,
                name: name,
                process: process
            });
        }
    $('#canvasArea').dblclick(function(e) {
        $("#editMainProps").show();
        $("#editElementProps").hide();
    });

    function viewElementProperties(id) {
        var processId;
        for (var k = 0; k < specializations.length; k++) {
            if (id == specializations[k].id) {
                predecessor = specializations[k].predecessor;
                successor = specializations[k].successor;
                $("#td_predecessor1").html(predecessor);
                $("#td_successor1").html(successor);
            }
        }
        for (var i = 0; i < chevronProperties.length; i++) {
            if (id == chevronProperties[i].id) {
                name = chevronProperties[i].name;
                $("#td_name1").html(name);
                processModel = chevronProperties[i].process;
                if (assets.length !== 0) { // all stored process token inputs
                    for (var k = 0; k < assets.length; k++) {
                        for (var j in assets.data) {
                            if (processModel == assets.data[j].name) {
                                processId = assets.data[j].id;
                                $("#td_mod").tokenInput("clear").tokenInput("add", {
                                    id: processId,
                                    name: processModel
                                });
                            }
                        }
                    }
                }
            }
        }
    }
    //set predecessor for a chevron
    function setPredecessor(sourceId, predecessorId) {
        var empty = "not declared";
        if (sourceId !== empty) {
            for (var i = 0; i < chevronProperties.length; i++) {
                if (predecessorId == chevronProperties[i].id) {
                    predecessorName = chevronProperties[i].name;
                }
            }
            if (predecessorCount == 0) { // the first most element's predecessor should be empty
                specializations.push({
                    id: predecessorId,
                    predecessor: empty,
                    predecessorId: empty,
                    successor: empty,
                    successorId: empty
                });
                specializations.push({
                    id: sourceId,
                    predecessorId: predecessorId,
                    predecessor: predecessorName,
                    successor: empty,
                    successorId: empty
                });
                ++predecessorCount;
            } else {
                specializations.push({
                    id: sourceId,
                    predecessorId: predecessorId,
                    predecessor: predecessorName,
                    successor: empty,
                    successorId: empty
                });
            }
        }
    }
    //set successor for a chevron
    function setSuccessor(sourceId, successorId) {
        if (successorId == "not declared") {
            for (var i = 0; i < specializations.length; i++) {
                if (sourceId == specializations[i].id) {
                    specializations[i].successorId = successorId;
                    specializations[i].successor = "not declared";
                }
            }
        } else {
            for (var k = 0; k < chevronProperties.length; k++) {
                if (successorId == chevronProperties[k].id) {
                    successorName = chevronProperties[k].name;
                    for (var i = 0; i < specializations.length; i++) {
                        if (sourceId == specializations[i].id) {
                            specializations[i].successorId = successorId;
                            specializations[i].successor = successorName;
                        }
                    }
                }
            }
        }
    }
    //update edited chevron positions
    function updatePositionsForRelations(element) {
        var id = element.find('.chevron-textField').attr('name');
        var xValue = parseInt(element.css("left"), 10);
        var yValue = parseInt(element.css("top"), 10);
        for (var i = 0; i < relationPositions.length; i++) {
            if (id == relationPositions[i].chevId) {
                if (relationPositions[i].xVal != xValue) {
                    relationPositions[i].xVal = xValue;
                    positionChanged = true;
                }
            }
        }
    }
    //when element is moved from its initial position update the state values as well
    function updatePositionInState(chevron) {
        var tempId = chevron.find('.chevron-textField').attr('name');
        for (var i = 0; i < formatting.length; i++) {
            if (tempId == formatting[i].chevronId) {
                formatting[i].positionX = parseInt(chevron.css("left"), 10);
                formatting[i].positionY = parseInt(chevron.css("top"), 10);
            }
        }
    }
    //clear property fields 
    function clearFields() {
        $("#td_predecessor1").html("");
        $("#td_successor1").html("");
        $("#td_name1").html("");
        $("#td_mod").html("");
    }
    //adding a new chevron element to existing diagram
    function newChevronCreation(xPosition, yPosition) {
        newElementPosX = xPosition;
        newElementPosY = yPosition;
        clearFields();
        isNewElement = true; // new element properties to be added 
        $('#save').css('visibility', 'visible');
    }
    //save properties for new element
    $('#save').click(function() {
        id = currentId;
        name = $("#td_name1").html();
        process = newElementProcess;
        storePropertiesOfChevron(id, name, process);
        saveStateForNewElement(id, name, process, newElementPosX, newElementPosY);
        setRelationsForNewElement(id, name); //set pre/suc values for new element
        $("#editElementProps").hide();
        isNewElement = false; //let element properties be viewed
    });

    function saveStateForNewElement(id, name, process, xPosition, yPosition) { //save state for new element
        relationPositions.push({
            chevId: id,
            chevName: name,
            xVal: xPosition,
            yVal: yPosition
        });
        chevrons.push({
            chevronId: id,
            chevronName: name,
            processModel: process
        });
        formatting.push({
            chevronId: id,
            positionX: xPosition,
            positionY: yPosition
        });
    }
    //set predecessor/successor values for newly created element
    function setRelationsForNewElement(id, name) {
        for (var i = 0; i < specializations.length; i++) {
            if (specializations[i].successor == "not declared") { // this would be the last available element 
                specializations[i].successorId = id;
                specializations[i].successor = name;
                newPredecessorId = specializations[i].id;
                for (var k = 0; k < chevronProperties.length; k++) {
                    if (newPredecessorId == chevronProperties[k].id) {
                        newPredecessor = chevronProperties[k].name;
                    }
                }
                newSuccessorId = "not declared";
                newSuccessor = "not declared";
            }
        }
        specializations.push({
            id: id,
            predecessorId: newPredecessorId,
            predecessor: newPredecessor,
            successorId: newSuccessorId,
            successor: newSuccessor
        });
    }
    //on click of a chevron
    function chevronClicked() {
        $("#editMainProps").hide();
        $("#editElementProps").show();
        $("#td_mod").html("");
        var clickedElement = $(this);
        clickedElement.find('.chevron-textField').focus();
        var id = clickedElement.find('.chevron-textField').attr('name');
        currentId = id;
        if (id == 0) { //if a new element is added
            newId = ++idList.length;
            clickedElement.find('.chevron-textField').attr('name', newId); //set an id for new element
            newChevId = clickedElement.find('.chevron-textField').attr('name');
            currentId = newChevId;
            idList.push(newChevId);
            var positionX = parseInt(clickedElement.css("left"), 10);
            var positionY = parseInt(clickedElement.css("top"), 10);
            newChevronCreation(positionX, positionY); // save properties of new chevron
        }
        if (!isNewElement) { //existing element of diagram
            $('#save').css('visibility', 'hidden');
            getProcessForChevron(id); // get associated process for the chevron
            var chevronText = clickedElement.find('.chevron-textField').val();
            viewElementProperties(id); //view properties of that chevron
            //view element details for chevron
        }
        jsPlumb.draggable(clickedElement, { //on element drag
            containment: 'parent',
            stop: function(event) {
                stateDragged = true;
                updatePositionInState(clickedElement); //alter initial saved state
                updatePositionsForRelations(clickedElement); // update position changes for suc/pre relationships
                updateRelations(clickedElement); //update pre/successor relationships from original to new
            }
        });
        clickedElement.find('.chevron-textField').userInputAdded(function(e) { // user is done entering name for chevron
            var nameOfCurrentTextBox = $(this).val();
            $("#td_name1").html(nameOfCurrentTextBox);
            if (!isNewElement) { // If exisiting element
                updateNameInState(id, nameOfCurrentTextBox);
                updateEditedName(id, nameOfCurrentTextBox);
                editName = true;
                updateRelations(clickedElement);
            }
        });
    }
    // On time out/ click on canvas add chevron name to table name property
    (function($) {
        $.fn.extend({
            userInputAdded: function(callback, timeout) {
                timeout = timeout || 10e3; // 10 second default timeout
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
    // update predecessor successor relationships
    function getCurrentElementOldRelations(currenId, currentName, currentX) {
        for (var i = 0; i < specializations.length; i++) {
            if (currentId == specializations[i].id) { //if current element
                // current object's old pre/suc values
                oldPreId = specializations[i].predecessorId; //previous predecessor
                oldPredecessor = specializations[i].predecessor;
                oldSucId = specializations[i].successorId; //previous successor
                oldSuccessor = specializations[i].successor;
                var changed = checkForOrderChange(oldSucId, currentX);
                if (changed) { // chevron order changed
                    setCurrentElementNewPredecessor(currentId, oldSucId, oldSuccessor); // set new predecessor for current element
                    getNewSuccessorValues(oldSucId, currentId, currentName, oldPreId, oldPredecessor); // get new successor for current element
                    positionChanged = false; // this method call is done
                    relationsReset = true; // should not set the successor again
                }
            }
        }
    }

    function getNewSuccessorValues(oldSucId, currentId, currentName, oldPreId, oldPredecessor) {
        for (var j = 0; j < specializations.length; j++) //set values for old successor element
        {
            if (oldSucId == specializations[j].id) {
                var oldSuccessorNextId = specializations[j].successorId;
                var oldSuccessorNext = specializations[j].successor;
                specializations[j].successorId = currentId; //set  new succssor for previous successor
                specializations[j].successor = currentName;
                specializations[j].predecessorId = oldPreId;
                specializations[j].predecessor = oldPredecessor; //set new predecessor for previous successor
                if (oldPreId == "not declared") {
                    setOldPredecessorNewValues(currentId, currentName, oldSuccessorNextId); // set new values for old predecessor 
                } else {
                    setCurrentPredecessorAsNewSuccessor(currentId, oldPreId); // set new values for old successor
                }
                setCurrentElementNewSuccessor(oldSuccessorNextId, oldSuccessorNext, currentId); //set new successor for current element
            }
        }
    }
    //set new successor for old predecessor
    function setCurrentPredecessorAsNewSuccessor(currentId, oldPreId) {
        for (var i = 0; i < specializations.length; i++) {
            if (currentId == specializations[i].id) {
                var newPreId = specializations[i].predecessorId;
                var newPredecessor = specializations[i].predecessor;
                getCurrentElementAsNewSuccessor(oldPreId, newPreId, newPredecessor);
            }
        }
    }

    function getCurrentElementAsNewSuccessor(oldPreId, newPreId, newPredecessor) {
        for (var r = 0; r < specializations.length; r++) { //set values for old predecessor element
            if (oldPreId == specializations[r].id) {
                specializations[r].successorId = newPreId;
                specializations[r].successor = newPredecessor; //set new predecessor for previous predecessor
            }
        }
    }
    // set new values for old predecessor
    function setOldPredecessorNewValues(currentId, currentName, oldSuccessorNextId) {
        if (oldSuccessorNextId == "not declared") {
            return;
        }
        for (var t = 0; specializations.length; t++) {
            if (oldSuccessorNextId == specializations[t].id) {
                specializations[t].predecessorId = currentId;
                specializations[t].predecessor = currentName;
            }
        }
    }
    // set new successor for current element
    function setCurrentElementNewSuccessor(oldSuccessorNextId, oldSuccessorNext, currentId) {
        for (var i = 0; i < specializations.length; i++) {
            if (currentId == specializations[i].id) { //if current element
                specializations[i].successorId = oldSuccessorNextId;
                specializations[i].successor = oldSuccessorNext;
            }
        }
    }
    // check if there chevron position order changed
    function checkForOrderChange(oldSucId, currentX) {
        for (var k = 0; k < relationPositions.length; k++) {
            if (oldSucId == relationPositions[k].chevId) {
                oldX = relationPositions[k].xVal;
                if (oldX < currentX) { // current element is dragged forward
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
    //set new predecessor for current element
    function setCurrentElementNewPredecessor(currentId, oldSucId, oldSuccessor) {
        for (var i = 0; i < specializations.length; i++) {
            if (currentId == specializations[i].id) { //if current element
                specializations[i].predecessorId = oldSucId; //set new predecessor for current element
                specializations[i].predecessor = oldSuccessor;
            }
        }
    }
    //update successor/predecessor for moved chevrons
    function updateRelations(element) {
        currentId = element.find('.chevron-textField').attr('name'); //current element id
        var currentName = element.find('.chevron-textField').val(); //current element's edited name
        if (editName) { // if the name of an element changed
            for (var count = 0; count < specializations.length; count++) {
                if (currentId == specializations[count].predecessorId) { //find current element in specializations predecessor
                    specializations[count].predecessor = currentName;
                }
                if (currentId == specializations[count].successorId) { // find cutrrent element in specializations successor
                    specializations[count].successor = currentName;
                }
            }
            editName = false;
        }
        if (positionChanged) { // position changed
            var currentX = parseInt(element.css("left"), 10);
            getCurrentElementOldRelations(currentId, currentName, currentX);
        }
    }
    // update changed name in state property of that chevron
    function updateNameInState(id, editedName) {
        for (var i = 0; i < chevrons.length; i++) {
            if (id == chevrons[i].chevronId) {
                chevrons[i].chevronName = editedName;
            }
        }
    }
    //if chevron name is edited update new name
    function updateEditedName(id, editedName) {
        for (var i = 0; i < chevronProperties.length; i++) {
            if (id == chevronProperties[i].id) {
                chevronProperties[i].name = editedName;
            }
        }
    }
    // replace process name to a linkable text
    function replaceProcessText(processName) {
        $.ajax({
            type: "GET",
            url: "../apis/processes?type=process",
            data: {
                q: processName
            },
            success: function(Result) {
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
    // if process for chevron is changed, update that value in stored array
    function editProcessProperties(newProcess) {
        for (var i = 0; i < chevronProperties.length; i++) {
            if (currentId == chevronProperties[i].id) {
                chevronProperties[i].process = newProcess;
                chevId = chevronProperties[i].id;
                viewElementProperties(chevId);
            }
        }
    }
    //when process name is changed, update chevron state as well
    function updateProcessInState(newProcess) {
        for (var i = 0; i < chevrons.length; i++) {
            if (currentId == chevrons[i].chevronId) {
                chevrons[i].processModel = newProcess;
            }
        }
    }
    // get the last available elements positions
    function getMatchingLastElementPositions(id) {
        for (var i = 0; i < relationPositions.length; i++) {
            if (relationPositions[i].chevId == id) {
                lastXCoordinate = relationPositions[i].xVal;
                lastYCoordinate = relationPositions[i].yVal;
            }
        }
    }
    //auto align new element with the diagram
    function alignElement(element) {
        var elementX = parseInt(element.css("left"), 10);
        var elementY = parseInt(element.css("top"), 10);
        var defaultSpace = 169;
        if (numOfCanvasElements > 0) { // not first element on canvas
            var lastId = canvasElementList.length - 1;
            elementX = parseInt(canvasElementList[lastId].elementX) + defaultSpace;
            elementY = parseInt(canvasElementList[lastId].elementY) - 1;
            element.css("left", elementX);
            element.css("top", elementY);
            canvasElementList.push({
                canvasId: numOfCanvasElements + 1,
                elementX: elementX,
                elementY: elementY
            });
        } else {
            for (var k = 0; k < specializations.length; k++) {
                if (specializations[k].successor == "not declared") {
                    getMatchingLastElementPositions(specializations[k].id); // get x/y values of last available element
                    elementX = parseInt(lastXCoordinate) + defaultSpace;
                    elementY = parseInt(lastYCoordinate) - 1;
                    element.css("left", elementX);
                    element.css("top", elementY);
                    canvasElementList.push({
                        canvasId: numOfCanvasElements + 1,
                        elementX: elementX,
                        elementY: elementY
                    });
                    return;
                }
            }
        }
    }
    $(function() {
        //Drag icon from toolbox and place on canvas
        $(".chevron-toolbox").draggable({
            helper: 'clone',
            cursor: 'move',
            tolerance: 'fit',
            revert: true
        });
        $("#canvasArea").droppable({
            accept: '.chevron-toolbox',
            activeClass: "canvasArea",
            containment: 'canvasArea',
            drop: function(e, ui) {
                var newId = 0; // starting id for new element
                newElement = ui.helper.clone();
                ui.helper.remove();
                $(newElement).removeAttr("class");
                $(newElement).addClass("chevron");
                var textField = $('<textArea>').attr({
                    name: newId
                }).addClass("chevron-textField");
                newElement.append(textField);
                alignElement(newElement); //align the new element with rest
                numOfCanvasElements++;
                newElement.appendTo('#canvasArea'); // main container
                newElement.click(chevronClicked);
            }
        });
        var url = "../apis/processes?type=process"; // url to derive all existing process models
        $("#td_mod").tokenInput(url, {
            preventDuplicates: true,
            theme: "facebook",
            onResult: function(results) {
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
                if (isNewElement) {
                    newElementProcess = name;
                } else {
                    editProcessProperties(name);
                    updateProcessInState(name);
                }
            },
            tokenFormatter: function(item) {
                return "<li><a href =/publisher/asts/process/details/" + item.id + ">" + item.name + " </a></li>"
            }
        });
    });
});