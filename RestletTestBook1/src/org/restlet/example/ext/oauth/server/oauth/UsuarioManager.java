/**
 * Copyright 2005-2013 Restlet S.A.S.
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
 * Restlet is a registered trademark of Restlet S.A.S.
 */
package org.restlet.example.ext.oauth.server.oauth;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import org.reslet.example.oauth.dbconnection.dao.GenericDAO;
import org.reslet.example.oauth.dbconnection.dao.UsuarioDAO;
import org.reslet.example.oauth.dbconnection.dto.UsuarioDTO;

/**
 *
 * @author Shotaro Uchida <fantom@xmaker.mx>
 */
public class UsuarioManager {
    
    private HashSet<UsuarioDTO> userSet;
    
    public UsuarioManager() {
        userSet = new HashSet<UsuarioDTO>();
    }
    
    public UsuarioDTO addUser(String id) {
        UsuarioDTO user = new UsuarioDTO(id);
        if (!userSet.contains(user)) {
            userSet.add(user);
            return user;
        }
        return null;
    }
    
    public UsuarioDTO findUserById(String id) throws SQLException {
      String respuesta = "";
     //   bbdd bd = new bbdd();
        Connection connection = GenericDAO.getConnection();
        try {
         //   Map<String, String> map = bbdd.consultaBBDD(connection, GET_USER, id);
            UsuarioDTO dto = UsuarioDAO.getUserByUserName(connection, id);
            System.out.println("findUser con id=" + id);

            if (!dto.isEmpty()) {
              //  respuesta += "user=" + map.get("username") + " password=" + map.get("password");
                respuesta += "user=" + dto.getId() + " password=" + dto.getPassword();
             //   printResponse(respuesta);
                UsuarioDTO user = new UsuarioDTO(dto.getId(), dto.getPassword(), "OK");
                if (user.getId().equals(id)) {
                    return user;
                }
            }
            System.out.println("respuesta:" + respuesta);
        } catch (Exception e) {
            System.out.println("Exception in findUserById");
            e.printStackTrace();
        }
        return null;
    }
}
