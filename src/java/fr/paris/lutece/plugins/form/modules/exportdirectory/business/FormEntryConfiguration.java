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

/**
 * Class which contains the EntryConfiguration of an entry and the information of its parent
 */
public class FormEntryConfiguration
{

    private EntryConfiguration _entryConfiguration;
    private int _nIdEntryParent;
    private int _nIterationMaximumNumber;

    // Constructor
    public FormEntryConfiguration( EntryConfiguration entryConfiguration, int nIdEntryParent, int nIterationMaximumNumber )
    {
        _entryConfiguration = entryConfiguration;
        _nIdEntryParent = nIdEntryParent;
        _nIterationMaximumNumber = nIterationMaximumNumber;
    }

    /**
     * Return the EntryConfiguration of the FormEntry
     * 
     * @return the entryConfiguration of the FormEntry
     */
    public EntryConfiguration getEntryConfiguration( )
    {
        return _entryConfiguration;
    }

    /**
     * The EntryConfiguration to set to the FormEntry
     * 
     * @param entryConfiguration
     *            the entryConfiguration to set
     */
    public void setEntryConfiguration( EntryConfiguration entryConfiguration )
    {
        _entryConfiguration = entryConfiguration;
    }

    /**
     * The identifier of the parent of the FormEntry
     * 
     * @return the identifier of the parent of the FormEntry
     */
    public int getIdEntryParent( )
    {
        return _nIdEntryParent;
    }

    /**
     * Set the identifier of the parent of the FormEntry
     * 
     * @param nIdEntryParent
     *            the nIdEntryParent to set
     */
    public void setIdEntryParent( int nIdEntryParent )
    {
        _nIdEntryParent = nIdEntryParent;
    }

    /**
     * Return the maximum number of iteration of the parent of the formEntry
     * 
     * @return the nIterationMaximumNumber of the parent of the FormEntry
     */
    public int getIterationMaximumNumber( )
    {
        return _nIterationMaximumNumber;
    }

    /**
     * Set the maximum number of iteration of the parent of the Formentry
     * 
     * @param nIterationMaximumNumber
     *            the nIterationMaximumNumber to set
     */
    public void setIterationMaximumNumber( int nIterationMaximumNumber )
    {
        _nIterationMaximumNumber = nIterationMaximumNumber;
    }

}
