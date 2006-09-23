package org.codehaus.plexus.security.authorization.rbac.web.action.admin;

/*
 * Copyright 2001-2006 The Codehaus.
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

import com.opensymphony.xwork.Preparable;

import org.codehaus.plexus.security.authorization.rbac.web.action.RbacActionException;
import org.codehaus.plexus.security.rbac.RBACManager;
import org.codehaus.plexus.security.rbac.RbacObjectNotFoundException;
import org.codehaus.plexus.security.rbac.Resource;
import org.codehaus.plexus.xwork.action.PlexusActionSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * OperationsAction:
 *
 * @author Jesse McConnell <jmcconnell@apache.org>
 * @version $Id:$
 * @plexus.component role="com.opensymphony.xwork.Action"
 *                   role-hint="pss-resources"
 */
public class ResourcesAction
    extends PlexusActionSupport
    implements Preparable
{
    private static final String LIST = "list";
    
    /**
     * @plexus.requirement
     */
    private RBACManager manager;

    private String resourceIdentifier;
    
    private boolean isPattern;

    private List allResources;

    public void prepare()
        throws Exception
    {
        if ( allResources == null )
        {
            allResources = new ArrayList();
        }
    }
    
    public String list()
    {
        allResources = manager.getAllResources();
        
        if ( allResources == null )
        {
            allResources = new ArrayList();
        }
        
        return LIST;
    }

    public String save()
        throws RbacActionException
    {
        // todo figure out if there is anyway to actually have this model driven action work with jdo objects
        Resource temp = manager.createResource( resourceIdentifier );

        temp.setIdentifier( resourceIdentifier );
        temp.setPattern( isPattern );

        manager.saveResource( temp );

        return LIST;
    }

    public String remove()
        throws RbacActionException
    {
        try
        {
            manager.removeResource( manager.getResource( resourceIdentifier ) );
        }
        catch ( RbacObjectNotFoundException ne )
        {
            throw new RbacActionException( "unable to locate resource to remove " + resourceIdentifier, ne );
        }
        return LIST;
    }

    public List getAllResources()
    {
        return allResources;
    }

    public void setAllResources( List allResources )
    {
        this.allResources = allResources;
    }

    public String getResourceIdentifier()
    {
        return resourceIdentifier;
    }

    public void setResourceIdentifier( String resourceIdentifier )
    {
        this.resourceIdentifier = resourceIdentifier;
    }

    public boolean isPattern()
    {
        return isPattern;
    }

    public void setPattern( boolean isPattern )
    {
        this.isPattern = isPattern;
    }
}