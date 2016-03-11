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