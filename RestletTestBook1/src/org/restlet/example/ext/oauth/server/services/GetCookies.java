/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.restlet.example.ext.oauth.server.services;

import org.restlet.data.Cookie;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * @author fve
 */
public class GetCookies extends ServerResource {
    @Get
public String representation(){
  /*  System.out.println("Getting cookies...");
     Cookie cookie = request.getCookies().getFirst("Credentials");
if (cookie != null) {
String[] credentials = cookie.getValue().split("=", 2);
if (credentials.length == 2) {
String identifier = credentials[0];
String secret = credentials[1];*/


return "callback({\"username\": \"Usuario001\",\"password\":\"inredis\"});";
}

}
