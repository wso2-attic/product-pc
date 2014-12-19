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
    var relationsReset = false; // predecessor/successor relationships were updated
    var numOfCanvasElements = 0; //canvas dropped element count
    var canvasElementList = []; // property list of elements on canvas
    var previousName; // when editing chevron name show properties depending on the previous chevron
    var saveClicked = false; // check if properties are saved for previous chevron
    var processTokens = []; // save process model name and id for linking purpose
    //on form submit save xml content of the drawn canvas
    $('#btn-create-asset').click(function(e) {
        saveDiagram();
    });
    // Save properties of elements
    $('#saveElms').click(function(e) {
        createElementProperties(mainProcess);
        var chevronName = $("#properties_ename").val();
        updateProcessModelOfState(chevronName); // save the new process model
        saveClicked = true;
        $("#elementProps").hide();
    });
    //Save properties of main process
    $('#save').click(function() {
        createMainProperties(mainProcess);
        $("#fullProps").hide();
    });

    function updateProcessModelOfState(chevronName) {
        for (var i = 0; i < chevrons.length; i++) {
            if (chevrons[i].chevronName == chevronName){ // if its an existing element
                for (var j = 0; j < mainProcess.length; j++){ // get process model for chevron
                
                    if (mainProcess[j].name == chevronName) {
                        processModel = mainProcess[j].models;
                    }
                }
                chevrons[i].processModel = processModel;
            }
        }
    }
    // On Canvas doubleclick show create/view page
    $('#dropArea').dblclick(function(e) {
        if (mainProcess.length !== 0 && mainProcessName) { // propererties for main diagram added
            $("#fullProps").show();
            viewPropertyList(mainProcess);
            $("#elementProps").hide();
        } else {
            $("#fullProps").show();
            $("#elementProps").hide();
        }
    });
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
                $("#properties_Associated_process_emodels").tokenInput("clear").tokenInput("add", {
                    id: processId,
                    name: propertyList[i].models
                });
                if (specializationList.length > 0) { // if predecessor/successor exists for chevron
                    if (propertyList[0].description) { // if a main process is added
                        $("#properties_epredecessors").val(specializationList[i - 1].predecessor);
                        $("#properties_esuccessors").val(specializationList[i - 1].successor);
                    } else if (!propertyList[0].description) {
                        $("#properties_epredecessors").val(specializationList[i].predecessor);
                        $("#properties_esuccessors").val(specializationList[i].successor);
                    }
                }
            }
        }
    }
    //Add properties for elements
    function createElementProperties(propertyList) {
        var id = iconId; // get the current id of the element
        var name = $("#properties_ename").val();
        var models = $("#properties_Associated_process_emodels").val();
        for (var i = 0; i < propertyList.length; i++) {
            if (id == propertyList[i].id) { // its an edit of a record
                propertyList[i].name = name;
                propertyList[i].models = models;
                existingElement = true;
            }
        }
        if (($.inArray(name, viewForName) == -1) && !existingElement) { // its a new record
            propertyList.push({
                id: id,
                name: name,
                models: models
            });
            var viewName = name;
            viewForName.push(viewName);
        }
    }
    // Add properties for main chevron diagram
    function createMainProperties(propertyList) {
        var mainId = "mainId";
        var description = $("textarea#overview_description").val();
        var name = $("#overview_name").val();
        var owner = $("#properties_owner").val();
        var predecessor = $("#properties_predecessors").val();
        var successor = $("#properties_successors").val();
        if (mainProcessName) { //its an edit of record
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
    }
    // Display added properties for main chevron diagram
    function viewPropertyList(propertyList) {
        for (var i = 0; i < propertyList.length; i++) {
            if (propertyList[i].description) { //it is the main chevron diagram
                $("#overview_name").val(propertyList[i].name);
                $("#properties_owner").val(propertyList[i].owner);
                $("#properties_predecessors").val(propertyList[i].predecessor);
                $("#properties_successors").val(propertyList[i].successor);
                $("textarea#overview_description").val(propertyList[i].description);
            }
        }
    }
    // Save states of canvas elements and  their positions
    var saveState = function(chevron) {
        var processModel = "not declared"; //state is first saved when element is dropped to canvas
        var chevronId1 = chevron.find('.text-edit').attr('name');
        var chevronName = chevron.find(".text-edit").val();
        var positionX = parseInt(chevron.css("left"), 10);
        var positionY = parseInt(chevron.css("top"), 10);
        if (stateDragged || editName) { // if position changed/ or chevron name got updated
            for (var i = 0; i < chevrons.length; i++) {
                if (chevrons[i].chevronId == chevronId1) { // if its an existing element
                    for (var j = 0; j < mainProcess.length; j++) { // get process model for chevron
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
                chevronId: chevronId1,
                chevronName: chevronName,
                processModel: processModel
            });
            formatting.push({
                chevronId: chevronId1,
                positionX: positionX,
                positionY: positionY
            });
        }
    }

        function setChevronPositions(element) { // When element is dropped to canvas keep track of their original positions
            var name = "empty";
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

        function updateChevronPositions(element) { //When moved, update position information for each  chevron
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
        // check if previous properties were saved ,if not give error
        function validateUnsavedProperties() {
            if ($("#properties_ename").val() && !saveClicked) {
                alert("Please save entered properties first");
                return false;
            } else {
                saveClicked = false;
                return true;
            }
        }
        //display properties for chevron  only if it was not displayed before
        function viewPropertyForNewChevron(chevronText) {
            if (chevronText == $("#properties_ename").val()) { // current chevron name is similar to table property name field
                if (previousName !== chevronText) { // previously opened view name is not equal to current chevron name
                    previousName = chevronText;
                    viewElementProperties(mainProcess, specializations, chevronText); //show properties
                }
            }
            if (chevronText !== $("#properties_ename").val()) { // current chevron name is not similar to table property name field
                if (previousName !== chevronText) { //previously opened chevron name is not similar to current chevron name
                    previousName = chevronText;
                    viewElementProperties(mainProcess, specializations, chevronText);
                }
            }
        }
        // Set incremental ids for elements that are added to the canvas
        function setIdsForElements(defaultId, element) {
            if (defaultId == 0 && elementsOnCanvas.length == 0) { //first element
                isFirstElement = true;
                defaultId = ++count;
                elementsOnCanvas.push(defaultId);
                element.find('.text-edit').attr('name', defaultId);
                setChevronPositions(element); //save position details for chevron
            }
            if (defaultId == 0 && elementsOnCanvas.length !== 0) { //not first element 
                var lastLocation = elementsOnCanvas.length;
                var temp = elementsOnCanvas[lastLocation - 1];
                defaultId = temp + 1;
                elementsOnCanvas.push(defaultId);
                element.find('.text-edit').attr('name', defaultId);
                setChevronPositions(element);
            }
        }
        // If chevron element is clicked 
        function divClicked() {
            var isFirstElement = false;
            var clickedElement = $(this);
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
                    stateDragged = true;
                    saveState(clickedElement);
                    updateChevronPositions(clickedElement); // update position changes
                    updateRelations(clickedElement);
                }
            });
            var textValue = clickedElement.find('.text-edit').val();
            if ($.inArray(textValue, viewForName) > -1) { // if properties already added for element
                //show the view with values
                if (!relationsReset) { // if original positions didn't change
                    setSuccessor(defaultId);
                }
                $("#elementProps").show();
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
                        updateRelations(clickedElement); //update predecessor/successor values
                        saveState(clickedElement);
                    } else {
                        validElementIds.push(tempId);
                    }
                    clickedElement.find('.text-edit').css('background-color', '#FFCC33');
                    var nameOfCurrentTextBox = $(this).val();
                    $("#properties_ename").val(nameOfCurrentTextBox);
                    setPredecessor(tempId);
                    nameForThisElement = $("#properties_ename").val();
                    saveState(clickedElement);
                });
            }
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
    // clear field values 
    function clearAllFields() {
        $("#properties_ename").val("");
        $("#properties_eowner").val("");
        $("#properties_epredecessors").val("");
        $("#properties_esuccessors").val("");
        $("#properties_Associated_process_emodels").val("");
    }
    //set successor for the element
    function setSuccessor(id) {
        var tempId = id;
        var nextId = ++id;
        if (id !== "mainId") { // not the main process
            for (var i = 0; i < mainProcess.length; i++) {
                var next;
                if (mainProcess[i].id == nextId) {
                    next = mainProcess[i].name;
                    for (var k = 0; k < specializations.length; k++) {
                        if (specializations[k].id == tempId) {
                            specializations[k].successor = next;
                            specializations[k].successorId = nextId;
                        }
                    }
                }
            }
        }
    }
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
    //get new values for current element's successor
    function getNewSuccessorValues(oldSucId, currentId, currentName, oldPreId, oldPredecessor) {
        for (var j = 0; j < specializations.length; j++) { //set values for old successor element
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
    // set old predecessor values
    function setCurrentPredecessorAsNewSuccessor(currentId, oldPreId) {
        for (var i = 0; i < specializations.length; i++) {
            if (currentId == specializations[i].id) {
                var newPreId = specializations[i].predecessorId;
                var newPredecessor = specializations[i].predecessor;
                getCurrentElementAsNewSuccessor(oldPreId, newPreId, newPredecessor);
            }
        }
    }
    // set new successor of old predecessor
    function getCurrentElementAsNewSuccessor(oldPreId, newPreId, newPredecessor) {
        for (var r = 0; r < specializations.length; r++) { //set values for old predecessor element
            if (oldPreId == specializations[r].id) {
                specializations[r].successorId = newPreId;
                specializations[r].successor = newPredecessor; //set new predecessor for previous predecessor
            }
        }
    }
    // set new predecessor for old predecessor
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
    // set new  successor for current element
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
        for (var k = 0; k < chevronPositions.length; k++) {
            if (oldSucId == chevronPositions[k].chevId) {
                oldX = chevronPositions[k].xVal;
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
        currentId = element.find('.text-edit').attr('name'); //current element id
        var currentName = element.find('.text-edit').val(); //current element's edited name
        if (editName) { // if the name of an element changed
            for (var count = 0; count < specializations.length; count++) {
                if (currentId == specializations[count].predecessorId) { //find current element in specializations predecessor
                    specializations[count].predecessor = currentName;
                }
                if (currentId == specializations[count].successorId) { // find cutrrent element in specializations successor
                    specializations[count].successor = currentName;
                }
            }
        }
        if (positionChanged) { // position changed
            var currentX = parseInt(element.css("left"), 10);
            getCurrentElementOldRelations(currentId, currentName, currentX);
        }
    }
    // add predecessor for element
    function setPredecessor(id) {
        var empty = "not declared";
        if (!editName) { //chevron name was not changed
            if (id !== '1') { // not first element on canvas
                var previous;
                for (var i = 0; i < mainProcess.length; i++) {
                    if (mainProcess[i].id == id - 1) {
                        previousId = mainProcess[i].id;
                        previous = mainProcess[i].name;
                        specializations.push({
                            id: id,
                            predecessor: previous,
                            predecessorId: previousId,
                            successor: empty,
                            successorId: empty
                        });
                    }
                }
            } else {
                specializations.push({
                    id: id,
                    predecessor: empty,
                    predecessorId: empty,
                    successor: empty,
                    successorId: empty
                });
            }
        }
    }
    //at page load
    jsPlumb.setContainer($('#dropArea'));
    $("#fullProps").show();
    $("#elementProps").hide();
    //Save the created diagram in XML format
    function saveDiagram() {
        var root = {};
        var xmlSection1 = [];
        var xmlSection2 = [];
        var diagram = [];
        var elements = [];
        var connects = [];
        var orders = [];
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
        //ajax call to save value in api
        $.ajax({
            type: "POST",
            url: "/publisher/asts/chevron/apis/chevronxml",
            data: {
                content: strAsXml,
                name: mainProcessName,
                type: "POST"
            }
        }).done(function(result) {});
    }
    // align elements that are added to canvas
    function alignDroppedElement(element) {
        var defaultSpace = 169; // space between two chevron elements
        var elementX = parseInt(element.css("left"), 10);
        var elementY = parseInt(element.css("top"), 10);
        if (numOfCanvasElements > 0) { // not the first element on canvas
            var lastId = canvasElementList.length - 1;
            elementX = parseInt(canvasElementList[lastId].elementX) + defaultSpace; // new left x value
            elementY = parseInt(canvasElementList[lastId].elementY) - 1;
            element.css("left", elementX);
            element.css("top", elementY);
            canvasElementList.push({
                canvasId: numOfCanvasElements + 1,
                elementX: elementX,
                elementY: elementY
            });
        } else {
            canvasElementList.push({
                canvasId: numOfCanvasElements + 1,
                elementX: elementX,
                elementY: elementY
            });
        }
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
                alignDroppedElement(droppedElement); //auto align with the previous element
                ++numOfCanvasElements;
                droppedElement.appendTo('#dropArea');
                validateUnsavedProperties(); // check if previous properties for element was saved
                // main container
                droppedElement.click(divClicked);
            }
        });
    });
});