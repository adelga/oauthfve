/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.restlet.example.ext.oauth.server.services;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 *
 * @author fve
 */
public class SessionLogout extends ServerResource {
     @Post
  public String getRep(){
  //Unregister client
         System.out.println("Dentro de sessionlogout");
         System.out.println("Unregistering client:"+getCookieSettings().getFirst("_cid"));
    getCookieSettings().getFirst("_cid").setValue("null");
    return null;
     }

}
