package org.codehaus.plexus.security.authentication;

import org.codehaus.plexus.security.authentication.AuthenticationException;

import java.util.Map;
/*
 * Copyright 2005 The Codehaus.
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

/**
 * AuthenticationStore: interface that the implementation of an authentication system must provide so it can be wired
 * into the PlexusSecurityRealm
 *
 *
 *
 * @author: Jesse McConnell <jesse@codehaus.org>
 * @version: $ID:$
 *
 */
public interface AuthenticationStore
{
    public static String ROLE = AuthenticationStore.class.getName();

    /**
     * Authenticate the map of tokens and return an authentication object or throw an
     * AuthenticationException.  
     *
     * @param tokens
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationResult authenticate( Map tokens ) throws AuthenticationException;

}