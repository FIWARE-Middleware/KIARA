/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2015 German Research Center for Artificial Intelligence (DFKI)
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
package org.fiware.kiara.util;

import com.google.common.base.Objects;

/**
 *
 * @author Dmitri Rubinstein {@literal <dmitri.rubinstein@dfki.de>}
 */
public class ReturnParam<T> {

    public T value;

    public ReturnParam() {
        value = null;
    }

    public ReturnParam(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value == null ? "<null>" : this.value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReturnParam) {
            ReturnParam other = (ReturnParam) obj;
            return Objects.equal(value, other.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + java.util.Objects.hashCode(this.value);
        return hash;
    }

}
