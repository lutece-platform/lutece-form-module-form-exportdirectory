/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class represent the {@link EntryConfiguration} DAO
 *
 */
public class EntryConfigurationDAO implements IEntryConfigurationDAO
{
    private static final String SQL_QUERY_SELECT_BY_PRIMARY_KEY = " SELECT id_directory_entry " +
        "FROM form_exportdirectory_entryconfiguration WHERE id_form = ? AND id_form_entry = ? ";
    private static final String SQL_QUERY_INSERT = " INSERT INTO form_exportdirectory_entryconfiguration " +
        "( id_form, id_form_entry, id_directory_entry ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = " UPDATE form_exportdirectory_entryconfiguration " +
        "SET id_directory_entry = ? WHERE id_form = ? AND id_form_entry = ? ";
    private static final String SQL_QUERY_DELETE = " DELETE FROM form_exportdirectory_entryconfiguration " +
        "WHERE id_form = ? AND id_form_entry = ? ";
    private static final String SQL_QUERY_DELETE_BY_FORM = " DELETE FROM form_exportdirectory_entryconfiguration " +
        "WHERE id_form = ? ";
    private static final String SQL_QUERY_SELECT_ENTRY_CONFIGURATION_BY_ID_FORM = " SELECT id_form_entry, id_directory_entry " +
        "FROM form_exportdirectory_entryconfiguration WHERE id_form = ? ";

    /**
     * {@inheritDoc}
     */
    public Collection<EntryConfiguration> findEntryConfigurationListByIdForm( int nIdForm, Plugin plugin )
    {
        Collection<EntryConfiguration> entryConfigurationList = new ArrayList<EntryConfiguration>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ENTRY_CONFIGURATION_BY_ID_FORM, plugin );
        daoUtil.setInt( 1, nIdForm );

        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            EntryConfiguration entryConfiguration = new EntryConfiguration(  );

            entryConfiguration.setIdFormEntry( DirectoryUtils.convertStringToInt( daoUtil.getString( 1 ) ) );
            entryConfiguration.setIdDirectoryEntry( DirectoryUtils.convertStringToInt( daoUtil.getString( 2 ) ) );
            entryConfigurationList.add( entryConfiguration );
        }

        daoUtil.free(  );

        return entryConfigurationList;
    }

    /**
     * {@inheritDoc}
     */
    public EntryConfiguration findByPrimaryKey( int nIdForm, int nIdEntry, Plugin plugin )
    {
        EntryConfiguration entryConfiguration = null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_PRIMARY_KEY, plugin );
        daoUtil.setInt( 1, nIdForm );
        daoUtil.setString( 2, Integer.toString( nIdEntry ) );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            entryConfiguration = new EntryConfiguration(  );
            entryConfiguration.setIdForm( nIdForm );
            entryConfiguration.setIdFormEntry( nIdEntry );
            entryConfiguration.setIdDirectoryEntry( daoUtil.getInt( 1 ) );
        }

        daoUtil.free(  );

        return entryConfiguration;
    }

    /**
     * {@inheritDoc}
     */
    public void delete( int nIdForm, int nIdEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdForm );
        daoUtil.setString( 2, Integer.toString( nIdEntry ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void deleteByForm( int nIdForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_FORM, plugin );
        daoUtil.setInt( 1, nIdForm );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void insert( EntryConfiguration entryConfiguration, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setInt( 1, entryConfiguration.getIdForm(  ) );
        daoUtil.setString( 2, Integer.toString( entryConfiguration.getIdFormEntry(  ) ) );
        daoUtil.setString( 3, Integer.toString( entryConfiguration.getIdDirectoryEntry(  ) ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * {@inheritDoc}
     */
    public void store( EntryConfiguration entryConfiguration, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setString( 1, Integer.toString( entryConfiguration.getIdDirectoryEntry(  ) ) );
        daoUtil.setInt( 2, entryConfiguration.getIdForm(  ) );
        daoUtil.setString( 3, Integer.toString( entryConfiguration.getIdFormEntry(  ) ) );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }
}
