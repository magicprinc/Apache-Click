/*
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
package net.sf.click.jquery.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Options extends HashMap {

    private JSONWriter jsonWriter;

    public Options() {
    }

    public Options(String key, Object value) {
        put(key, value);
    }

    public Options(String key, JSONLiteral value) {
        put(key, value);
    }

    public Options put(String key, boolean value) {
        super.put(key, value);
        return this;
    }

    public Options put(String key, int value) {
        super.put(key, value);
        return this;
    }

    public Options put(String key, long value) {
        super.put(key, value);
        return this;
    }

    public Options put(String key, double value) {
        super.put(key, value);
        return this;
    }

    public Options put(String key, Map value) {
        super.put(key, value);
        return this;
    }

    public Options put(String key, List value) {
        super.put(key, value);
        return this;
    }

    public Options putList(String key, Object... options) {
        super.put(key, Arrays.asList(options));
        return this;
    }

    public Options put(String key, Object bean) {
        super.put(key, bean);
        return this;
    }

    public Options put(String key, JSONLiteral literal) {
        super.put(key, literal);
        return this;
    }

    public Options putLiteral(String key, String literal) {
        super.put(key, new JSONLiteral(literal));
        return this;
    }

    public Options putFunction(String key, String function) {
        super.put(key, new JSONLiteral(function));
        return this;
    }

    public String toJson() {
        return getJSONWriter().write(this);
    }

    public String toFormattedJson() {
        return getJSONWriter().writeFormatted(this);
    }

    @Override
    public String toString() {
        return toJson();
    }

    // Private Methods --------------------------------------------------------

    private JSONWriter getJSONWriter() {
        if (jsonWriter == null) {
            jsonWriter = new JSONWriter();
        }
        return jsonWriter;
    }
}
