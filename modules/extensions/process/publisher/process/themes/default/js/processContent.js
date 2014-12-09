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