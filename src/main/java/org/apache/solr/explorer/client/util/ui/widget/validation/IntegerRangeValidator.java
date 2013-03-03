/*
 * Copyright 2011 SearchWorkings.org
 *
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

package org.apache.solr.explorer.client.util.ui.widget.validation;

/**
 * @author Uri Boness
 */
public class IntegerRangeValidator extends AbstractValidator<Integer> {

    private final int min;
    private final int max;

    public IntegerRangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public ValidationResult validate(Integer value) {
        if (value == null) {
            return valid();
        }
        return (value >= min && value <= max) ? valid() : error("Value must be in the range between " + min + " and " + max);
    }
}
