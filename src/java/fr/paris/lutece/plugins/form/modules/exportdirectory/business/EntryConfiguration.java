/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.form.modules.exportdirectory.business;

import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.form.business.EntryHome;
import fr.paris.lutece.plugins.form.business.IEntry;
import fr.paris.lutece.plugins.form.service.EntryRemovalListenerService;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.portal.service.plugin.PluginService;


/**
 *
 * @author ELY
 *
 */
public class EntryConfiguration
{
    private static EntryConfigurationEntryFormRemovalListener _listenerForm;
    private static EntryConfigurationEntryDirectoryRemovalListener _listenerDirectory;
    private int _nIdForm;
    private int _nIdFormEntry;
    private int _nIdDirectoryEntry;

    /**
    * Initialize the EntryConfiguration
    */
    public static void init(  )
    {
        // Create removal listeners and register them
        if ( _listenerForm == null )
        {
            _listenerForm = new EntryConfigurationEntryFormRemovalListener(  );
            EntryRemovalListenerService.getService(  ).registerListener( _listenerForm );
            _listenerDirectory = new EntryConfigurationEntryDirectoryRemovalListener(  );
            fr.paris.lutece.plugins.directory.business.EntryRemovalListenerService.getService(  )
                                                                                  .registerListener( _listenerDirectory );
        }
    }

    /**
     * @return the idForm
     */
    public int getIdForm(  )
    {
        return _nIdForm;
    }

    /**
     * @param nIdForm the idForm to set
     */
    public void setIdForm( int nIdForm )
    {
        this._nIdForm = nIdForm;
    }

    /**
     * @return the idFormEntry
     */
    public int getIdFormEntry(  )
    {
        return _nIdFormEntry;
    }

    /**
     * @param idFormEntry the idFormEntry to set
     */
    public void setIdFormEntry( int idFormEntry )
    {
        this._nIdFormEntry = idFormEntry;
    }

    /**
     * @return the idDirectoryEntry
     */
    public int getIdDirectoryEntry(  )
    {
        return _nIdDirectoryEntry;
    }

    /**
     * @param idDirectoryEntry the idDirectoryEntry to set
     */
    public void setIdDirectoryEntry( int idDirectoryEntry )
    {
        this._nIdDirectoryEntry = idDirectoryEntry;
    }

    /**
     * Get the Entry title
     * @return The entry title
     */
    public String getFormEntryTitle(  )
    {
        IEntry entry = EntryHome.findByPrimaryKey( getIdFormEntry(  ), PluginService.getPlugin( FormPlugin.PLUGIN_NAME ) );

        if ( entry != null )
        {
            return entry.getTitle(  );
        }
        else
        {
            return null;
        }
    }

    /**
     * Get the Entry title
     * @return The entry title
     */
    public String getDirectoryEntryTitle(  )
    {
        fr.paris.lutece.plugins.directory.business.IEntry entry = fr.paris.lutece.plugins.directory.business.EntryHome.findByPrimaryKey( getIdDirectoryEntry(  ),
                PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ) );

        if ( entry != null )
        {
            return entry.getTitle(  );
        }
        else
        {
            return null;
        }
    }
}
