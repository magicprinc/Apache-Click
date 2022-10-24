/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

jQuery('#$imageId').live("click", function() {
    jQuery('#$fieldId').click();
})

jQuery('#$fieldId').ColorPicker({
    onSubmit: function(hsb, hex, rgb, el) {
        jQuery('#$fieldId').val('#' + hex);
        jQuery('#$imageId div div').css('backgroundColor', '#' + hex);
        jQuery(el).ColorPickerHide();
    },
    onBeforeShow: function () {
        jQuery(this).ColorPickerSetColor(this.value);
    }
})
.bind('keyup', function(){
    jQuery(this).ColorPickerSetColor(this.value);
});
