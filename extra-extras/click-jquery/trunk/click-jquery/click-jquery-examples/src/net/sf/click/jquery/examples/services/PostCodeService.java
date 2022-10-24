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
package net.sf.click.jquery.examples.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.click.jquery.examples.domain.PostCode;
import net.sf.click.jquery.examples.util.StartupListener;
import org.apache.commons.lang.StringUtils;

/**
 * PostCode related Services used by the Pages.
 */
public class PostCodeService {

    public List<PostCode> getPostCodes() {
        return StartupListener.POST_CODES;
    }

    public List<String> getPostCodeLocations(String location) {
        List<String> list = new ArrayList<String>();

        for(PostCode postCode : getPostCodes()) {
            if (StringUtils.startsWithIgnoreCase(postCode.getLocality(), location)) {
                String value = postCode.getLocality() + ", " + postCode.getState() +
                    " " + postCode.getPostCode();
                list.add(value);
            }
        }

        Collections.sort(list);
        return list;
    }
}
