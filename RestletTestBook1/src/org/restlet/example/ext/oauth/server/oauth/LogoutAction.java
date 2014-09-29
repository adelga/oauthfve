/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.restlet.example.ext.oauth.server.oauth;

import org.restlet.data.CookieSetting;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

/**
 *
 * @author fve
 */
public class LogoutAction extends ServerResource {

    private static final String ClientCookieID = "_cid";

    @Get("html")
    @Post("html")
    public String representation() throws OAuthException {
        closeSession();
        return "mycallback({\"process\": \"OK\"});";
    }

    public void closeSession() {
        String sessionId = getCookies().getFirstValue(ClientCookieID);
        getLogger().fine("sessionId = " + sessionId);
        // cleanup cookie.
        CookieSetting cs = new CookieSetting(ClientCookieID, "");
        getCookieSettings().add(cs);
    }
}
