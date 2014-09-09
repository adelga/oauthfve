/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.restlet.example.ext.oauth.server.external;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * @author fve
 */
public class HelloServerResource extends ServerResource {
@Get
public String representation(){
    System.out.println("Se ha llamado a ServerResource");
return "callback({\"username\": \"Usuario001\",\"password\":\"inredis\"});";
}
}
