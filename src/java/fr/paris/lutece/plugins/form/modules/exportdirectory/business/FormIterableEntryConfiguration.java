package fr.paris.lutece.plugins.form.modules.exportdirectory.business;

import fr.paris.lutece.plugins.form.utils.FormConstants;

/**
 * Object which contains information for the creation of directory entry during the export from Form
 */
public class FormIterableEntryConfiguration
{
    private int _nCurrentIterationNumber = FormConstants.DEFAULT_ITERATION_NUMBER;
    private boolean _bGlobalConfiguration = Boolean.FALSE;
    private boolean _bGlobalTypeConfiguration = Boolean.FALSE;

    // Default constructor
    public FormIterableEntryConfiguration( )
    {

    }

    // Constructor
    public FormIterableEntryConfiguration( int nCurrentIterationNumber, boolean bGlobalConfiguration, boolean bGlobalTypeConfiguration )
    {
        _nCurrentIterationNumber = nCurrentIterationNumber;
        _bGlobalConfiguration = bGlobalConfiguration;
        _bGlobalTypeConfiguration = bGlobalTypeConfiguration;
    }

    /**
     * Return the current number of iteration
     * 
     * @return the current number of iteration
     */
    public int getCurrentIterationNumber( )
    {
        return _nCurrentIterationNumber;
    }

    /**
     * Set the current number of iteration
     * 
     * @param nCurrentIterationNumber
     *            the current number of iteration to set
     */
    public void setCurrentIterationNumber( int nCurrentIterationNumber )
    {
        _nCurrentIterationNumber = nCurrentIterationNumber;
    }

    /**
     * Return the boolean which tell if the configuration is global for all iteration or not
     * 
     * @return the bIsGlobalConfiguration
     */
    public boolean isGlobalConfiguration( )
    {
        return _bGlobalConfiguration;
    }

    /**
     * Set the bGlobalConfiguration
     * 
     * @param bGlobalConfiguration
     *            the bIsGlobalConfiguration to set
     */
    public void setGlobalConfiguration( boolean bGlobalConfiguration )
    {
        _bGlobalConfiguration = bGlobalConfiguration;
    }

    /**
     * Return the boolean which tell if the configuration type is the same for all the iteration or not
     * 
     * @return the bGlobalTypeConfiguration
     */
    public boolean isGlobalTypeConfiguration( )
    {
        return _bGlobalTypeConfiguration;
    }

    /**
     * Set the bGlobalTypeConfiguration
     * 
     * @param bGlobalTypeConfiguration
     *            the bGlobalTypeConfiguration to set
     */
    public void setGlobalTypeConfiguration( boolean bGlobalTypeConfiguration )
    {
        _bGlobalTypeConfiguration = bGlobalTypeConfiguration;
    }
}
