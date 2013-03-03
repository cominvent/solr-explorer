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

package org.apache.solr.explorer.client.util.collection;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Uri Boness
 */
public class DefaultIndexedMap<K, V> implements IndexedMap<K, V> {

    private final Map<K,V> map;
    private final List<V> list;

    public DefaultIndexedMap() {
        this(new HashMap<K, V>(), new ArrayList<V>());
    }

    public DefaultIndexedMap(Map<K, V> map, List<V> list) {
        this.map = map;
        this.list = list;
    }

    public int indexOf(V v) {
        return list.indexOf(v);
    }

    public V getValue(int index) {
        return list.get(index);
    }

    public Iterator<V> iterator() {
        return list.iterator();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return list.contains(value);
    }

    public V get(Object key) {
        return map.get(key);
    }

    public V put(K key, V value) {
        V replaced = map.put(key, value);
        if (replaced != null) {
            list.remove(replaced);
        }
        list.add(value);
        return replaced;
    }

    public V remove(Object key) {
        V removed = map.remove(key);
        list.remove(removed);
        return removed;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        map.clear();
        list.clear();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return list;
    }

    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
