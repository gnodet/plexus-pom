package org.codehaus.plexus.builder;

/*
 * Copyright (c) 2004, Codehaus.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.MavenMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;

import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * @author <a href="mailto:jason@maven.org">Jason van Zyl</a>
 * @version $Id$
 */
public abstract class AbstractBuilder
    extends AbstractLogEnabled
{
    protected final static Set BOOT_ARTIFACTS = new HashSet( Arrays.asList( new Artifact[]{
        new DefaultArtifact( "classworlds", "classworlds", "1.1-alpha-2-SNAPSHOT", "jar" ),
    } ) );

    protected final static Set CORE_ARTIFACTS = new HashSet( Arrays.asList( new Artifact[]{
        new DefaultArtifact( "plexus", "plexus-container-default", "1.0-alpha-2-SNAPSHOT", "jar" ),
        new DefaultArtifact( "plexus", "plexus-container-artifact", "1.0-alpha-1-SNAPSHOT", "jar" ),
        new DefaultArtifact( "plexus", "plexus-appserver", "1.0-alpha-1-SNAPSHOT", "jar" ),
        new DefaultArtifact( "plexus", "plexus-utils", "1.0-alpha-1-SNAPSHOT", "jar" ),
    } ) );

    // ----------------------------------------------------------------------
    // Components
    // ----------------------------------------------------------------------

    protected ArtifactResolver artifactResolver;

    // ----------------------------------------------------------------------
    // Utility methods
    // ----------------------------------------------------------------------

    protected void executable( String file )
        throws CommandLineException, IOException
    {
        if ( Os.isFamily( "unix" ) )
        {
            Commandline cli = new Commandline();

            cli.setExecutable( "chmod" );

            cli.createArgument().setValue( "+x" );

            cli.createArgument().setValue( file );

            cli.execute();
        }
    }

    protected File mkdir( File directory )
    {
        if ( !directory.exists() )
        {
            directory.mkdirs();
        }

        return directory;
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    protected void filterCopy( File in, File out, Map map )
        throws IOException
    {
        filterCopy( new FileReader( in ), out, map );
    }

    protected void filterCopy( InputStream in, File out, Map map )
        throws IOException
    {
        filterCopy( new InputStreamReader( in ), out, map );
    }

    protected void filterCopy( Reader in, File out, Map map )
        throws IOException
    {
        InterpolationFilterReader reader = new InterpolationFilterReader( in, map, "@", "@" );

        Writer writer = new FileWriter( out );

        IOUtil.copy( reader, writer );

        writer.close();
    }

    // ----------------------------------------------------------------------
    // Artifact methods
    // ----------------------------------------------------------------------

    protected void copyArtifact( Artifact artifact, File outputDir, File destination )
        throws IOException
    {
        String dest = destination.getAbsolutePath().substring( outputDir.getAbsolutePath().length() + 1 );

        getLogger().info( "Adding " + artifact.getId() + " to " + dest );

        FileUtils.copyFileToDirectory( artifact.getFile(), destination );
    }

    protected void copyArtifacts( File outputDir, File dir, Set artifacts )
        throws IOException
    {
        for ( Iterator it = artifacts.iterator(); it.hasNext(); )
        {
            Artifact artifact = (Artifact) it.next();

            copyArtifact( artifact, outputDir, dir );
        }
    }

    protected Set findArtifacts( Set remoteRepositories, ArtifactRepository localRepository, Set sourceArtifacts, boolean resolveTransitively )
        throws ArtifactResolutionException
    {
        ArtifactResolutionResult result;

        MavenMetadataSource metadata = new MavenMetadataSource( remoteRepositories, localRepository, artifactResolver );

        Set artifacts;

        if( resolveTransitively )
        {
            result = artifactResolver.resolveTransitively( sourceArtifacts, remoteRepositories, localRepository, metadata );

            // TODO: Assert that there wasn't any conflicts.

            artifacts = new HashSet( result.getArtifacts().values() );
        }
        else
        {
            artifacts = artifactResolver.resolve( sourceArtifacts, remoteRepositories, localRepository );
        }

        return artifacts;
    }
}
