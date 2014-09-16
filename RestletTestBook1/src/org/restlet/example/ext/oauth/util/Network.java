/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.restlet.example.ext.oauth.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fve
 */
public class Network {
public static String getLocalIP(){
    String localhost = "";
        try {
            localhost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
        }
    return localhost;
}
}
