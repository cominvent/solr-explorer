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

package org.apache.solr.explorer.client.core.model.context.query;

/**
 * Represents a sort creteria based on which the search results should be sorted on. The sort criteria can be associated
 * with a field name, in which case the result shold be sorted based on the values of the fields. Alternatively, the
 * sort can be based on the hit score in which case {@link #SCORE_ASC} and {@link #SCORE_DESC} can be used.
 *
 * @author Uri Boness
 */
public class Sort {

    public enum Direction { ASC, DESC }

    /**
     * Represents an ascending score based sort.
     */
    public final static Sort SCORE_ASC = new Sort("score", Direction.ASC);

    /**
     * Represents a descending score based sort.
     */
    public final static Sort SCORE_DESC = new Sort("score", Direction.DESC);

    private final String field;

    private final Direction direction;

    /**
     * Constructs a new Sort with a given filed to sort on and a direction.
     *
     * @param field The field to sort on.
     * @param direction The direction of the sort (asc or desc).
     */
    public Sort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public static Sort asc(String field) {
        return new Sort(field, Direction.ASC);
    }

    public static Sort desc(String field) {
        return new Sort(field, Direction.DESC);
    }

    /**
     * Returns the field to sort on.
     *
     * @return The field to sort on.
     */
    public String getField() {
        return field;
    }

    /**
     * Returns the direction of the sort (asc or desc).
     *
     * @return The direction of the sort.
     */
    public Direction getDirection() {
        return direction;
    }

}
