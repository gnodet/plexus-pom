package org.codehaus.plexus.configuration;

import junit.framework.TestCase;
import org.codehaus.plexus.configuration.builder.XmlPullConfigurationBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 *
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 *
 * @version $Id$
 */
public class ConfigurationMergerTest
    extends TestCase
{
    private XmlPullConfigurationBuilder configurationBuilder;

    private PlexusConfiguration user;

    private PlexusConfiguration system;

    public ConfigurationMergerTest( String s )
    {
        super( s );
    }

    public void setUp()
        throws Exception
    {
        configurationBuilder = new XmlPullConfigurationBuilder();

        InputStream userStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "org/codehaus/plexus/configuration/avalon.xml" );

        assertNotNull( userStream );

        user = configurationBuilder.parse( new InputStreamReader( userStream ) );

        InputStream systemStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "org/codehaus/plexus/plexus.conf" );

        assertNotNull( systemStream );

        system = configurationBuilder.parse( new InputStreamReader( systemStream ) );
    }

    public void testSimpleConfigurationCascading()
        throws Exception
    {
        PlexusConfiguration cc = PlexusConfigurationMerger.merge( user, system );

        assertEquals( "user-conf-dir", cc.getChildren( "configurations-directory" )[0].getValue() );

        assertEquals( "org.codehaus.plexus.personality.avalon.AvalonComponentRepository",
                      cc.getChild( "component-repository" ).getChild( "implementation" ).getValue() );

        assertEquals( "org.codehaus.plexus.classloader.DefaultResourceManager",
                      cc.getChild( "resource-manager" ).getChild( "implementation" ).getValue() );

        assertEquals( "logging-implementation", cc.getChild( "logging" ).getChild( "implementation" ).getValue() );

        PlexusConfiguration lhm = cc.getChild( "lifecycle-handler-manager" );

        assertEquals( "avalon", lhm.getChild( "default-lifecycle-handler-id" ).getValue() );

        PlexusConfiguration lh = lhm.getChild( "lifecycle-handlers" ).getChildren( "lifecycle-handler" )[0];

        assertEquals( "avalon", lh.getChild( "id" ).getValue() );

        PlexusConfiguration[] bs = lh.getChild( "begin-segment" ).getChildren( "phase" );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.LogEnablePhase", bs[0].getAttribute( "implementation" ) );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.ContextualizePhase", bs[1].getAttribute( "implementation" ) );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.ServicePhase", bs[2].getAttribute( "implementation" ) );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.ComposePhase", bs[3].getAttribute( "implementation" ) );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.ConfigurePhase", bs[4].getAttribute( "implementation" ) );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.InitializePhase", bs[5].getAttribute( "implementation" ) );

        assertEquals( "org.codehaus.plexus.personality.avalon.lifecycle.phase.StartPhase", bs[6].getAttribute( "implementation" ) );

        PlexusConfiguration componentMM = cc.getChild( "component-manager-manager" );

        assertEquals( "singleton", componentMM.getChild( "default-component-manager-id" ).getValue() );

        PlexusConfiguration[] components = cc.getChild( "components" ).getChildren( "component" );

        assertEquals( "org.codehaus.plexus.ServiceA", components[0].getChild( "role" ).getValue() );
    }
}
