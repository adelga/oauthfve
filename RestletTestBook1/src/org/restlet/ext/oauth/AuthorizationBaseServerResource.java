/**
 * Copyright 2005-2014 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet
 */

package org.restlet.ext.oauth;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import org.restlet.data.CookieSetting;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.oauth.internal.AuthSession;
import org.restlet.ext.oauth.internal.AuthSessionTimeoutException;
import org.restlet.ext.oauth.internal.RedirectionURI;
import org.restlet.representation.Representation;

import freemarker.template.Configuration;

/**
 * Base Restlet resource class for Authorization service resource. Handle errors
 * according to OAuth2.0 specification, and manage AuthSession. Authorization
 * Endndpoint, Authorization pages, and Login pages should extends this class.
 * 
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class AuthorizationBaseServerResource extends OAuthServerResource {

    private static final String ClientCookieID = "_cid";

    @Override
    protected void doCatch(Throwable t) {
        final OAuthException oex = OAuthException.toOAuthException(t);
        AuthSession session = null;
        try {
            session = getAuthSession();
        } catch (OAuthException ignore) {
            // FIXME
        }
        if (session == null || session.getAuthFlow() == null) {
            // Session was revoked or not established.
            Representation resp = getErrorPage(
                    HttpOAuthHelper.getErrorPageTemplate(getContext()), oex);
            getResponse().setEntity(resp);
        } else {
            /*
             * If the resource owner denies the access request or if the request
             * fails for reasons other than a missing or invalid redirection
             * URI, the authorization server informs the client by adding the
             * following parameters to the query component of the redirection
             * URI using the "application/x-www-form-urlencoded" format, per
             * Appendix B: (4.2.2.1. Error Response)
             */
            // Use fragment component for Implicit Grant (4.2.2.1. Error
            // Response)
            boolean fragment = session.getAuthFlow().equals(ResponseType.token);
            sendError(session.getRedirectionURI().getURI(), oex,
                    session.getState(), fragment);
        }
    }

    /**
     * Sets up a new authorization session.
     * 
     * @param redirectUri
     *            The redirection URI.
     */
    protected AuthSession setupAuthSession(RedirectionURI redirectUri) {
        getLogger().fine("Base ref = " + getReference().getParentRef());

        AuthSession session = AuthSession.newAuthSession();
        session.setRedirectionURI(redirectUri);

        CookieSetting cs = new CookieSetting(ClientCookieID, session.getId());
        // TODO create a secure mode setting, update all cookies
        // cs.setAccessRestricted(true);
        // cs.setSecure(true);
        getCookieSettings().add(cs);
        System.out.println("Setting cookie in SetupSession - " + session.getId());

        getContext().getAttributes().put(session.getId(), session);

        return session;
    }

    /**
     * Returns the current authorization session.
     * 
     * @return The current {@link AuthSession} instance.
     */
    protected AuthSession getAuthSession() throws OAuthException {
        // Get some basic information
        String sessionId = getCookies().getFirstValue(ClientCookieID);
        getLogger().fine("sessionId = " + sessionId);

        AuthSession session = (sessionId == null) ? null
                : (AuthSession) getContext().getAttributes().get(sessionId);

        if (session == null) {
            return null;
        }

        try {
            session.updateActivity();
        } catch (AuthSessionTimeoutException ex) {
            // Remove timeout session4
            //TODO, ir a login no al redirec uri
            System.err.println("Session timeout redirect: " + getQueryValue(REDIR_URI));
           
            getContext().getAttributes().remove(sessionId);
          // redirectTemporary("/oauth/login");
            return null;
        }

        return session;
    }

    /**
     * Unget current authorization session.
     */
    protected void ungetAuthSession() {
        String sessionId = getCookies().getFirstValue(ClientCookieID);
        // cleanup cookie.
        if (sessionId != null && sessionId.length() > 0) {
            ConcurrentMap<String, Object> attribs = getContext()
                    .getAttributes();
            attribs.remove(sessionId);
        }
    }

    /**
     * Helper method to format error responses according to OAuth2 spec.
     * (Redirect)
     * 
     * @param redirectURI
     *            redirection URI to send error
     * @param ex
     *            Any OAuthException with error
     * @param state
     *            state parameter as presented in the initial authorize request
     * @param fragment
     *            true if use URL Fragment.
     */
    protected void sendError(String redirectURI, OAuthException ex,
            String state, boolean fragment) {
        Reference cb = new Reference(redirectURI);
        cb.addQueryParameter(ERROR, ex.getError().name());
        if (state != null && state.length() > 0) {
            cb.addQueryParameter(STATE, state);
        }
        String description = ex.getErrorDescription();
        if (description != null && description.length() > 0) {
            cb.addQueryParameter(ERROR_DESC, description);
        }
        String errorUri = ex.getErrorURI();
        if (errorUri != null && errorUri.length() > 0) {
            cb.addQueryParameter(ERROR_URI, errorUri);
        }
        if (fragment) {
            cb.setFragment(cb.getQuery());
            cb.setQuery("");
        }
        redirectTemporary(cb);
    }

    /**
     * Helper method to format error responses according to OAuth2 spec. (Non
     * Redirect)
     * 
     * @param errPage
     *            errorPage template name
     * @param ex
     *            Any OAuthException with error
     */
    protected Representation getErrorPage(String errPage, OAuthException ex) {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ContextTemplateLoader(getContext(),
                "clap:///"));
        getLogger().fine("loading: " + errPage);
        TemplateRepresentation response = new TemplateRepresentation(errPage,
                config, MediaType.TEXT_HTML);

        // Build the model
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put(ERROR, ex.getError().name());
        data.put(ERROR_DESC, ex.getErrorDescription());
        data.put(ERROR_URI, ex.getErrorURI());

        response.setDataModel(data);

        return response;
    }
}
