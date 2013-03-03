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

package org.apache.solr.explorer.client.plugin.spellcheck.model.result;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;

/**
 * Result of spellcheck, containing suggestions for replacements of a particular searchterm.
 *
 * @author Rob van Maris
 */
public class SpellCheckResult {

    private String collatedSuggestion;

    private final Map<String, List<Suggestion>> suggestionsByOriginal = new HashMap<String, List<Suggestion>>();

    private Set<String> suggestedWords = new HashSet<String>();

    public SpellCheckResult() {
    }

    public String getCollatedSuggestion() {
        return collatedSuggestion;
    }

    public void setCollatedSuggestion(String collatedSuggestion) {
        this.collatedSuggestion = collatedSuggestion;
    }

    public void addSuggestion(String original, Suggestion suggestion) {
        addSuggestions(original, Arrays.asList(suggestion));
    }

    public void addSuggestions(String original, List<Suggestion> suggestions) {
        List<Suggestion> currrentSuggestions = suggestionsByOriginal.get(original);
        if (currrentSuggestions == null) {
            currrentSuggestions = new ArrayList<Suggestion>();
            suggestionsByOriginal.put(original, currrentSuggestions);
        }
        currrentSuggestions.addAll(suggestions);
        for (Suggestion suggestion : suggestions) {
            suggestedWords.add(suggestion.getWord());
        }
    }

    public Map<String, List<Suggestion>> getSuggestionsByOriginal() {
        return suggestionsByOriginal;
    }

    @SuppressWarnings("unchecked")
    public List<Suggestion> getSuggestions(String original) {
        List<Suggestion> suggestions = suggestionsByOriginal.get(original);
        if (suggestions == null) {
            suggestions = Collections.EMPTY_LIST;
        }
        return suggestions;
    }

    public boolean hasSuggestions(String original) {
        List<Suggestion> suggestions = suggestionsByOriginal.get(original);
        return suggestions != null && !suggestions.isEmpty();
    }

    public boolean hasSuggestions() {
        return !suggestionsByOriginal.isEmpty();
    }

    public boolean isSuggested(String word) {
        return suggestedWords.contains(word);
    }


    //================================================= Inner Classes ==================================================

    public static class Suggestion {

        private final String word;
        private final int frequency;

        public Suggestion(String word, int frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        public String getWord() {
            return word;
        }

        public int getFrequency() {
            return frequency;
        }
    }
}
