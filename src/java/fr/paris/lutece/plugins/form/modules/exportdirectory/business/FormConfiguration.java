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

import fr.paris.lutece.plugins.directory.business.DirectoryRemovalListenerService;
import fr.paris.lutece.plugins.form.service.FormRemovalListenerService;


/**
 *
 * @author ELY
 *
 */
public class FormConfiguration
{
    private static FormConfigurationFormRemovalListener _listenerForm;
    private static FormConfigurationDirectoryRemovalListener _listenerDirectory;
    private int _nIdForm;
    private int _nIdDirectory;

    /**
     * Initialize the FormConfiguration
     */
    public static void init(  )
    {
        // Create removal listeners and register them
        if ( _listenerForm == null )
        {
            _listenerForm = new FormConfigurationFormRemovalListener(  );
            FormRemovalListenerService.getService(  ).registerListener( _listenerForm );
            _listenerDirectory = new FormConfigurationDirectoryRemovalListener(  );
            DirectoryRemovalListenerService.getService(  ).registerListener( _listenerDirectory );
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
     * @return the id directory
     */
    public int getIdDirectory(  )
    {
        return _nIdDirectory;
    }

    /**
     * @param nIdDirectory the id directory to set
     */
    public void setIdDirectory( int nIdDirectory )
    {
        this._nIdDirectory = nIdDirectory;
    }
}
