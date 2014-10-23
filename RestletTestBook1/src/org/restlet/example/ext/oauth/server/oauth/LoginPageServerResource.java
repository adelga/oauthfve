
/*
 * Copyright (c) 2014 , Fundacion Vodafone Espa�a
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. Neither the name of copyright holders nor the names of its
   contributors may be used to endorse or promote products derived
   from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
''AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDERS OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE. BESIDES THIS, IN NO EVENT FUNDACION VODAFONE ESPA�A
SHALL BE LIABLE FOR THE REDISTRIBUTION AND USE IN SOURCE AND BINARY FORMS, WITH OR 
WITHOUT MODIFICATION, DEVELOPMENTS OR ANY OTHER ACTIONS DONE BY THIRD PARTIES, AND NEITHER 
SHALL BE LIABLE FOR ANY DAMAGES (ANY CATEGORY) DERIVED FROM THESE ACTIONS.
Copyright (c) 2013, Fundaci�n Vodafone Espa�a
Todos los derechos reservados.

La redistribuci�n y el uso en las formas de c�digo fuente y binario, con o sin
modificaciones, est�n permitidos siempre que se cumplan las siguientes condiciones:
1. Las redistribuciones del c�digo fuente deben conservar el aviso de copyright
anterior, esta lista de condiciones y el siguiente descargo de responsabilidad.
2. Las redistribuciones en formato binario deben reproducir el aviso de copyright anterior, esta lista de condiciones y la siguiente renuncia en la documentaci�n y/u otros materiales suministrados con la distribuci�n.
3. Ni el nombre de los titulares de derechos de autor ni los nombres de sus colaboradores pueden usarse para apoyar o promocionar productos derivados de este software sin permiso previo y por escrito.

ESTE SOFTWARE SE SUMINISTRA POR FUNDACI�N VODAFONE ESPA�A ''COMO EST�'' Y 
CUALQUIER GARANT�A EXPRESAS O IMPL�CITAS, INCLUYENDO, CON CAR�CTER ENUNCIATIVO 
PERO NO LIMITATIVO A, LAS GARANT�AS IMPL�CITAS DE COMERCIALIZACI�N Y APTITUD 
PARA UN PROP�SITO DETERMINADO, SON TODAS RECHAZADAS. EN NING�N CASO FUNDACI�N VODAFONE ESPA�A 
SER� RESPONSABLE DE DA�OS DIRECTOS, INDIRECTOS, INCIDENTALES, ESPECIALES, EJEMPLARES O CONSECUENTES 
(INCLUYENDO, CON CAR�CTER ENUNCIATIVO PERO LIMITADO A: LA ADQUISICI�N DE BIENES O SERVICIOS,LA P�RDIDA DE USO, 
DE DATOS O DE BENEFICIOS O, LA INTERRUPCI�N DE LA ACTIVIDAD EMPRESARIAL) O POR CUALQUIER OTRO TIPO DE 
RESPONSABILIDAD LEGALMENTE ESTABLECIDA, YA SEA POR CONTRATO, RESPONSABILIDAD ESTRICTA O AGRAVIO 
(INCLUYENDO NEGLIGENCIA O CUALQUIER OTRA CAUSA) QUE SURJA DE CUALQUIER MANERA DEL USO DE ESTE SOFTWARE, 
INCLUSO SI SE HA ADVERTIDO DE LA POSIBILIDAD DE TALES DA�OS. EN ESTE MISMO SENTIDO,FUNDACION VODAFONE 
ESPA�A NO SER� RESPONSABLE BAJO NINGUNA CIRCUNSTANCIA DE LA REDISTRIBUCI�N Y DEL USO EN LAS FORMAS DE 
C�DIGO FUENTE Y BINARIO QUE TERCEROS HAGAN, DE SUS POSIBLES DESARROLLOS, MODIFICACIONES Y DEM�S ACCIONES, 
NI TAMPOCO DE LOS POSIBLES DA�OS (DE CUALQUIER CATEGOR�A) QUE PUDIERAN GENERARSE A PARTIR DE DICHAS ACCIONES.
 */


package org.restlet.example.ext.oauth.server.oauth;

import freemarker.template.Configuration;
import java.sql.SQLException;
import java.util.HashMap;
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;
import org.restlet.data.MediaType;
import org.restlet.example.ext.oauth.server.OAuth2Sample;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.oauth.AuthorizationBaseServerResource;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.ext.oauth.internal.AuthSession;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.security.SecretVerifier;

/**
 * Login authentication resource.
 * 
 * @author Alberto Delgado
 */
public class LoginPageServerResource extends AuthorizationBaseServerResource {
    
    @Get("html")
    @Post("html")
    public Representation getPage() throws OAuthException, SQLException {
       System.out.println("Get Login");
        String userId = getQueryValue("user_id");
        System.out.println("Get Login 2"+userId);
        HashMap<String, Object> data = new HashMap<String, Object>();
        if (userId != null && !userId.isEmpty()) {
            String password = getQueryValue("password");
            getLogger().info("User=" + userId + ", Pass=" + password);
            
            //TODO Chekear en la base de datos de Users (UssuarioConnection) si es correcto la tupla userid/password
            // Podemos utilizar UsuarioConnection dentro de SampleUser y que se gestione internamente desde esta clase, p.e
            System.out.println("Llamada a findByUserById");
            UsuarioDTO user = OAuth2Sample.getSampleUserManager().findUserById(userId);
            System.out.println("Fin llamada findByUserId");
            if (user == null) {
                data.put("error", "Authentication failed.");
                data.put("error_description", "ID is invalid.");
            } else {
                System.out.println("Obj created");
                System.out.println("password en Login page:"+ user.getPassword());
                boolean result = SecretVerifier.compare(password.toCharArray(), user.getPassword().toCharArray());
                if (result) {
                    AuthSession session = getAuthSession();
                    session.setScopeOwner(userId);
                    String uri = getQueryValue("continue");
                    getLogger().info("URI: " + uri);
                    redirectTemporary(uri);
                    return new EmptyRepresentation();
                } else {
                    data.put("error", "Authentication failed.");
                    data.put("error_description", "Password is invalid.");
                }
            }
        }
        
        String continueURI = getQueryValue("continue");
        TemplateRepresentation response = getLoginPage("login.html");
        data.put("continue", continueURI);
        response.setDataModel(data);
        
        return response;
    }
    
    
    protected TemplateRepresentation getLoginPage(String loginPage) {
        Configuration config = new Configuration();
        config.setTemplateLoader(new ContextTemplateLoader(getContext(), "clap://system/resources"));
        getLogger().fine("loading: " + loginPage);
        return new TemplateRepresentation(loginPage, config, MediaType.TEXT_HTML);
    }
}
