
$("#save-image").click(function(){
    
    var canvass = document.getElementById('hiddenCanvas');

    var svgDiv = $("#mainSvg")[0];

    var svg = svgDiv.outerHTML;     
   /* var canvas = document.createElement('canvas');*/
    canvg(canvass, svg);
    
    download(canvass, 'sample.png');
});


function download(canvas, filename) {
    download_in_ie(canvas, filename) || download_with_link(canvas, filename);
}

// Works in IE10 and newer
function download_in_ie(canvas, filename) {
    return(navigator.msSaveOrOpenBlob && navigator.msSaveOrOpenBlob(canvas.msToBlob(), filename));
}

// Works in Chrome and FF. Safari just opens image in current window, since .download attribute is not supported
function download_with_link(canvas, filename) {
    var a = document.createElement('a')
    a.download = filename
    a.href = canvas.toDataURL("image/png")
    document.body.appendChild(a);
    a.click();
    a.remove();
}