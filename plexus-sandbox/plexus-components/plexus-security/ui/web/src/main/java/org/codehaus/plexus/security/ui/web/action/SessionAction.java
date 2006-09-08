package org.codehaus.plexus.security.ui.web.action;

/*
 * Copyright 2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.codehaus.plexus.security.authentication.AuthenticationDataSource;
import org.codehaus.plexus.security.authentication.AuthenticationException;
import org.codehaus.plexus.security.system.SecuritySession;
import org.codehaus.plexus.security.system.SecuritySystem;
import org.codehaus.plexus.security.user.UserNotFoundException;
import org.codehaus.plexus.security.user.UserManager;
import org.codehaus.plexus.security.user.User;
import org.codehaus.plexus.xwork.action.PlexusActionSupport;

/**
 * SessionAction:
 *
 * @author Jesse McConnell <jmcconnell@apache.org>
 * @version $Id:$
 * @plexus.component role="com.opensymphony.xwork.Action"
 * role-hint="session"
 */
public class SessionAction
    extends PlexusActionSupport
{

    /**
     * @plexus.requirement
     */
    private SecuritySystem securitySystem;

    private String username;

    private String password;


    public String login()
    {
        if ( username != null && password != null )
        {
            //
            // this can be removed once we have a functional user management system
            //
            UserManager um = securitySystem.getUserManager();

            String fullName = "bobs your uncle";
            String emailAddress = "fake@address.com";
            
            User user = um.createUser( username, fullName, emailAddress );
            user.setPassword( password );

            um.addUser( user );

            try
            {
                SecuritySession securitySession =
                    securitySystem.authenticate( new AuthenticationDataSource( username, password ) );

                if ( securitySession.getAuthenticationResult().isAuthenticated() )
                {
                    session.put( SecuritySession.ROLE, securitySession );
                    session.put( "user", securitySession.getUser() );
                    session.put( "authStatus", new Boolean( true ) );
                    return SUCCESS;
                }
                else
                {
                    addActionError( "authentication failed" );
                    return ERROR;
                }
            }
            catch ( AuthenticationException ae )
            {
                addActionError( ae.getMessage() );
                return ERROR;
            }
            catch ( UserNotFoundException ue )
            {
                addActionError( ue.getMessage() );
                return ERROR;
            }
        }
        else
        {
            return INPUT;
        }
    }

    public String logout()
    {
        session.clear();

        return SUCCESS;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }
}