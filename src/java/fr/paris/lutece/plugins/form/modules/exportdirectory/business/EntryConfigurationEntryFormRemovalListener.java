/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.plugins.form.modules.exportdirectory.service.ExportdirectoryPlugin;
import fr.paris.lutece.plugins.form.utils.EntryTypeGroupUtils;
import fr.paris.lutece.plugins.form.utils.FormConstants;
import fr.paris.lutece.plugins.genericattributes.business.EntryHome;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.RemovalListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

/**
 * {@link FormConfiguration} Removal Listener
 */
public class EntryConfigurationEntryFormRemovalListener implements RemovalListener
{
    private static final String PROPERTY_FORM_CANNOT_BE_REMOVED = "module.form.exportdirectory.message.entryFormCannotBeRemoved";

    /**
     * Check if the object can be safely removed
     * 
     * @param strId
     *            The object id
     * @return true if the pbject can be removed otherwise false
     */
    public boolean canBeRemoved( String strId )
    {
        if ( strId == null )
        {
            return true;
        }

        Plugin pluginExportdirectory = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        Entry entry = EntryHome.findByPrimaryKey( Integer.parseInt( strId ) );

        List<EntryConfiguration> listEntryConfiguration = new ArrayList<>( );
        int nEntryIterationNumber = EntryTypeGroupUtils.getEntryMaxIterationAllowed( entry.getIdEntry( ) );
        if ( nEntryIterationNumber != FormConstants.DEFAULT_ITERATION_NUMBER )
        {
            int nCurrentIteration = NumberUtils.INTEGER_ONE;
            while ( nCurrentIteration <= nEntryIterationNumber )
            {
                EntryConfiguration entryConfiguration = EntryConfigurationHome.findByPrimaryKey( entry.getIdResource( ), entry.getIdEntry( ),
                        nCurrentIteration, pluginExportdirectory );
                if ( entryConfiguration != null )
                {
                    listEntryConfiguration.add( entryConfiguration );
                }

                nCurrentIteration++;
            }
        }
        else
        {
            EntryConfiguration entryConfiguration = EntryConfigurationHome.findByPrimaryKey( entry.getIdResource( ), entry.getIdEntry( ),
                    FormConstants.DEFAULT_ITERATION_NUMBER, pluginExportdirectory );
            if ( entryConfiguration != null )
            {
                listEntryConfiguration.add( entryConfiguration );
            }
        }

        if ( listEntryConfiguration != null && !listEntryConfiguration.isEmpty( ) )
        {
            return false;
        }

        return true;
    }

    /**
     * Gives a message explaining why the object can't be removed
     * 
     * @param strId
     *            The object id
     * @param locale
     *            The current locale
     * @return The message
     */
    public String getRemovalRefusedMessage( String strId, Locale locale )
    {
        // Build a message mailing list for using this form
        return I18nService.getLocalizedString( PROPERTY_FORM_CANNOT_BE_REMOVED, locale );
    }
}
