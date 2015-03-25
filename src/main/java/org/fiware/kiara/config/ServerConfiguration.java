/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */

package org.fiware.kiara.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConfiguration {
    public String info = null;
    public String idlURL = null;
    public String idlContents = null;
    public List<ServerInfo> servers = new ArrayList<>();

    public final void clear() {
        info = null;
        idlURL = null;
        idlContents = null;
        if (servers != null)
            servers.clear();
    }

    private final static ObjectReader jsonReader;
    private final static ObjectWriter jsonWriter;

    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        //mapper.configure(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED, true);
        jsonWriter = mapper.writer();
        jsonReader = mapper.reader(ServerConfiguration.class);
    }

    public static ServerConfiguration fromJson(String json) throws IOException {
        return jsonReader.<ServerConfiguration>readValue(json);
    }

    public String toJson() throws IOException {
        return jsonWriter.writeValueAsString(this);
    }

}
