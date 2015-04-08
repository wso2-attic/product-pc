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
describe('list chevron diagram items - publisher', function() {
    it('Confirm ajax call success of retrieving requested digram name', function() {
        var element = "diagram 1";
        spyOn($, "ajax").and.callFake(function(options) {
            options.success();
        });
        var callback = jasmine.createSpy();
        getRelatedChevronDiagram(callback, element);
        expect(callback).toHaveBeenCalled();
    });
    it('Get diagram name on link click', function() {
        var link = $('<a/>', {
            text: 'diagram1',
            click: function() {
                var link1 = $(this);
                var linkName = link1.text();
                return linkName;
            }
        });
        var linkText = link.click();
        expect(linkText.length).toEqual(1);
    });
});