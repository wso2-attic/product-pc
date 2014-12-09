 var processContent = null;	
 var processName = null;	

 $('#btn-create-asset').click(function() {
	 processContent = $('#processContent').val();	
	 processName = $('#processName').val();
  $.post( "/publisher/asts/process/apis/content", {content: processContent, name: processName, reqType:"post"} );
 
});


$('#editAssetButton').click(function() {
	 processContent = $('#processContent').val();	
	 processName = $('#processName').val();
  $.post( "/publisher/asts/process/apis/content", {content: processContent, name: processName, reqType:"post"} );
 
});