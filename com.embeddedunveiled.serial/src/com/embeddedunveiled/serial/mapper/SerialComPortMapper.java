/*
 * Author : Rishi Gupta
 * 
 * This file is part of 'serial communication manager' library.
 * Copyright (C) <2014-2016>  <Rishi Gupta>
 *
 * This 'serial communication manager' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The 'serial communication manager' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with 'serial communication manager'.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.embeddedunveiled.serial.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.embeddedunveiled.serial.SerialComException;
import com.embeddedunveiled.serial.internal.SerialComPortMapperJNIBridge;

/**
 * <p>Provides methods to create and remove alias for serial ports is system.</p>
 * 
 * @author Rishi Gupta
 */
public final class SerialComPortMapper {

    private final Object lock = new Object();
    private HashMap<String, String> localInfo = new HashMap<String, String>();
    private final SerialComPortMapperJNIBridge mSerialComPortMapperJNIBridge;

    /**
     * <p>Construct and allocates a new SerialComPortMapper object with given details.</p>
     * 
     * @param mSerialComPortMapperJNIBridge interface class to native library for calling platform 
     *         specific routines.
     * @throws SerialComException if the object can not be constructed.
     */
    public SerialComPortMapper(SerialComPortMapperJNIBridge mSerialComPortMapperJNIBridge) {
        this.mSerialComPortMapperJNIBridge = mSerialComPortMapperJNIBridge;
    }

    /**
     * <p>Start the service which may run with elevated privileges in system.</p>
     * 
     * @return true if the service was started successfully.
     * @throws SerialComException if an error occurs.
     */
    public boolean startMappingService() throws SerialComException {
        synchronized(lock) {
            int ret = mSerialComPortMapperJNIBridge.startMappingService();
            if(ret < 0) {
                throw new SerialComException("Could not start serial port mapping service. Please retry !");
            }
        }
        return true;
    }

    /**
     * <p>Stops the serial port mapping service when no more required. This increase the security if this 
     * library is running on servers.</p>
     * 
     * @return true if the service was started successfully.
     * @throws SerialComException if an error occurs.
     */
    public boolean stopMappingService() throws SerialComException {
        synchronized(lock) {
            int ret = mSerialComPortMapperJNIBridge.stopMappingService();
            if(ret < 0) {
                throw new SerialComException("Could not stop serial port mapping service. Please retry !");
            }
        }
        return true;
    }

    /**
     * <p>Creates a alias to the given existing serial port.</p>
     * 
     * @param alias created name that maps to existing serial port.
     * @param comPort actual existing serial port which is mapped. 
     * @return true on success.
     * @throws SerialComException if alias can not be created or an I/O error occurs.
     */
    public boolean mapAliasToExistingComPort(String alias, String comPort) throws SerialComException {

        if(alias == null) {
            throw new IllegalArgumentException("Argument alias can not be null !");
        }
        String aliasVal = alias.trim();
        if(aliasVal.length() == 0) {
            throw new IllegalArgumentException("Argument alias can not be empty string !");
        }

        if(comPort == null) {
            throw new IllegalArgumentException("Argument comPort can not be null !");
        }
        String comPortVal = comPort.trim();
        if(comPortVal.length() == 0) {
            throw new IllegalArgumentException("Argument comPort can not be empty string !");
        }

        synchronized(lock) {
            int ret = mSerialComPortMapperJNIBridge.mapAliasToExistingComPort(alias, comPort);
            if(ret < 0) {
                throw new SerialComException("Could not map given alias to serial port. Please retry !");
            }

            localInfo.put(alias, comPort);
        }

        return true;
    }

    /**
     * <p>Removes a previously created alias to the given existing serial port.</p>
     * 
     * @param alias created name that maps to existing serial port. 
     * @return true on success.
     * @throws SerialComException if given alias is unknown to this library, or if given 
     *          alias can not be removed or an I/O error occurs.
     */
    public boolean unmapAliasToExistingComPort(String alias) throws SerialComException {

        if(alias == null) {
            throw new IllegalArgumentException("Argument alias can not be null !");
        }
        String aliasVal = alias.trim();
        if(aliasVal.length() == 0) {
            throw new IllegalArgumentException("Argument alias can not be empty string !");
        }

        synchronized(lock) {
            if(localInfo.containsKey(alias)) {
                int ret = mSerialComPortMapperJNIBridge.unmapAliasToExistingComPort(alias);
                if(ret < 0) {
                    throw new SerialComException("Could not remove given alias to serial port. Please retry !");
                }

                localInfo.remove(alias);
            }else {
                throw new SerialComException("Could not remove given alias to serial port. Please retry !");
            }
        }

        return true;
    }

    /**
     * <p>Provides caller with a read-only Map view of the aliases created using SCM library.</p>
     * 
     * @return Map view of the aliases created using SCM library.
     */
    public Map<String, String> listCreatedAliases() {
        Map<String, String> aliasList;
        synchronized(lock) {
            aliasList = Collections.unmodifiableMap(localInfo);
        }
        return aliasList;
    }
}
