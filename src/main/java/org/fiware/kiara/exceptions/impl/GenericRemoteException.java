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

package org.fiware.kiara.exceptions.impl;

import org.fiware.kiara.exceptions.RemoteException;

/**
 *
 * @author {@literal Dmitri Rubinstein <dmitri.rubinstein@dfki.de>}
 */
public class GenericRemoteException extends RemoteException {
    private final int errorCode;
    private final Object errorData;

    // Common error codes (borrowed from JSON RPC spec)
    public static final int METHOD_NOT_FOUND       = -32601;
    public static final int INVALID_METHOD_PARAMS  = -32602;
    public static final int INTERNAL_ERROR         = -32603;

    public GenericRemoteException() {
        super();
        errorCode = 0;
        errorData = null;
    }

    public GenericRemoteException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorData = null;
    }

    public GenericRemoteException(String message, int errorCode, Object errorData) {
        super(message);
        this.errorCode = errorCode;
        this.errorData = errorData;
    }

    public GenericRemoteException(String message, int errorCode, Object errorData, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorData = errorData;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Object getErrorData() {
        return errorData;
    }
}
