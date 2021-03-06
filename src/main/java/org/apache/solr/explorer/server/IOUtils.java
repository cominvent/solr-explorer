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

package org.apache.solr.explorer.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Uri Boness
 */
public final class IOUtils {

    private final static int DEFAULT_BUFFER = 2048;

    private IOUtils() {
    }

    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, DEFAULT_BUFFER);
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        try {
            byte[] buffer = new byte[bufferSize];
            int i = 0;
            while ((i = input.read(buffer)) > 0) {
                output.write(buffer, 0, i);
            }
            output.flush();
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

}
