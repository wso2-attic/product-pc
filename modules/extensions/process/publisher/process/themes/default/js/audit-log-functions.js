/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

var logObj;
var logArr = [];

var timeToDateStr = function(time) {
    var date = new Date(time);
    return date;
};

var LogActivity = function(id,author,action,type,time){
    this.id = id;
    this.author = author;
    this.action = action;
    this.type = type;
    this.time = time;
    this.dateStr = timeToDateStr(time);
};

var compare = function compare(a,b) {
    if (a.time <= b.time)
        return -1;
    else if (a.time > b.time)
        return 1;
    else
        return 0;
};

var logview = function (path) {
        console.log(path);
        $.ajax({
            url: '/publisher/assets/process/apis/audit_log',
            type: 'POST',
            data: {'path': path },
            success: function (data) {
                var response = JSON.parse(data);
                if (response.error === false) {
                    logObj = JSON.parse(response.content);

                    var key;
                    for(key in logObj.log) {
                        var log_activity = new LogActivity(
                            logObj.log[key].asset,logObj.log[key].user,logObj.log[key].action,logObj.log[key].type,logObj.log[key].timestamp);
                        logArr.push(log_activity);
                    }
                    if(logArr.length != 0) {
                        $('#logDiv').show();
                        $('#empty-log-div').hide();
                        logArr.sort(compare);
                        for(key in logArr) {
                            $('#audit-log-table > tbody:last-child').append('<tr class="history-entry"> ' +
                                '<td width="40%" style="border: 0px !important;" class="history-entry"><span class="margin-right-double">'+
                                logArr[key].dateStr+'</span></td> <td width="60%" id="process-version" style="border: 0px !important;" class="history-entry"><span><span class="text-primary"> '+
                                logArr[key].author+'</span> '+ logArr[key].action +'s '+ logArr[key].type +' for <span class="text-success"> '+logArr[key].id+'</span></span></td> </tr>');
                        }
                    } else {
                        $('#logDiv').hide();
                        $('#empty-log-div').show();
                    }
                } else {
                    alert(response.content);
                }
            },
            error: function () {
                alert.error('Process log returning error');
            }
        });
    };