/*
 * Copyright 2022 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.org.sevn.mddata;

public class Prop {

    public static final String LIST_SEPARATOR = ",";
    public static final String STR_SEPARATOR = "\n";
    private final String name;
    private StringBuilder value = new StringBuilder ();

    public Prop (String n) {
        String [] strs = n.split ("=");
        this.name = strs [0];
        for (int i = 1; i < strs.length; i++) {
            addValue (strs [i]);
        }
    }

    public String getValue () {
        return value.toString ();
    }

    public void addValue (String value) {
        if (this.value.length () > 0) {
            this.value.append (STR_SEPARATOR);
        }
        this.value.append (value);
    }

    public void addListValue (String value) {
        if (this.value.length () > 0) {
            this.value.append (LIST_SEPARATOR);
        }
        this.value.append (value);
    }

    public String getName () {
        return name;
    }

}
