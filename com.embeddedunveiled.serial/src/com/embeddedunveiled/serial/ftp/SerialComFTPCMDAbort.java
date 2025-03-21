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

package com.embeddedunveiled.serial.ftp;

/** 
 * <p>Acts as a messenger between application and this library to specify 
 * whether sending/receiving file should continue or be aborted.</p>
 * 
 * @author Rishi Gupta
 */
public final class SerialComFTPCMDAbort {

    private volatile boolean abortTransferNow;

    /**
     * <p>Allocates a new SerialComFTPCMDAbort object with initial state as continue to 
     * transfer.</p>
     */
    public SerialComFTPCMDAbort() {
        abortTransferNow = false; // initial state.
    }

    /** 
     * <p>Instructs this library to stop sending file if called by file sender,
     *  or to stop receiving file if called by file receiver using Xmodem or 
     *  its variant protocols.</p>
     */
    public void abortTransfer() {
        abortTransferNow = true;
    }

    /** 
     * <p>Checks whether file transfer or reception should be aborted or not.</p>
     * 
     * @return true if it should be aborted otherwise false if file transfer 
     *          should continue.
     */
    public boolean isTransferToBeAborted() {
        return abortTransferNow;
    }
}
