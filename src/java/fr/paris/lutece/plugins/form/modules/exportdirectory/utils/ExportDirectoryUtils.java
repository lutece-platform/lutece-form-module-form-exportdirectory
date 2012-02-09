/*
 * Copyright (c) 2002-2011, Mairie de Paris
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
package fr.paris.lutece.plugins.form.modules.exportdirectory.utils;

import fr.paris.lutece.plugins.directory.business.Directory;
import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.EntryType;
import fr.paris.lutece.plugins.directory.business.EntryTypeHome;
import fr.paris.lutece.plugins.directory.business.Field;
import fr.paris.lutece.plugins.directory.business.FieldHome;
import fr.paris.lutece.plugins.directory.business.File;
import fr.paris.lutece.plugins.directory.business.IEntry;
import fr.paris.lutece.plugins.directory.business.MapProviderManager;
import fr.paris.lutece.plugins.directory.business.PhysicalFile;
import fr.paris.lutece.plugins.directory.business.Record;
import fr.paris.lutece.plugins.directory.business.RecordField;
import fr.paris.lutece.plugins.directory.business.RecordFieldHome;
import fr.paris.lutece.plugins.directory.business.RecordHome;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.service.DirectoryService;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.form.business.Form;
import fr.paris.lutece.plugins.form.business.FormHome;
import fr.paris.lutece.plugins.form.business.FormSubmit;
import fr.paris.lutece.plugins.form.business.Response;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.EntryConfiguration;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.EntryConfigurationHome;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.FormConfiguration;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.FormConfigurationHome;
import fr.paris.lutece.plugins.form.modules.exportdirectory.business.ProcessorExportdirectory;
import fr.paris.lutece.plugins.form.modules.exportdirectory.service.ExportdirectoryPlugin;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.plugins.form.service.ResponseService;
import fr.paris.lutece.plugins.form.utils.FormUtils;
import fr.paris.lutece.portal.business.workflow.State;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.image.ImageUtil;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * class FormUtils
 *
 */
public final class ExportDirectoryUtils
{
    //parameter
    public static final String PARAMETER_PREFIX_KEY_GEOLOCATION = "key_geolocation_";
    private static final String PARAMETER_ID_ENTRY_TYPE = "id_entry_type";
    private static final String PARAMETER_ADD_NUMBERING_ENTRY = "add_numbering_entry";
    private static final String PARAMETER_ADD_NUMBERING_ENTRY_PREFIX = "add_numbering_entry_prefix";
    private static final String PARAMETER_ID_WORKFLOW = "id_workflow";
    private static final String PARAMETER_IS_IN_RESULT_LIST = "is_in_result_list_";
    private static final String PARAMETER_IS_IN_SEARCH = "is_in_search_";
    private static final String PARAMETER_THUMBNAIL_HEIGHT = "thumbnail_height_";
    private static final String PARAMETER_THUMBNAIL_WIDTH = "thumbnail_width_";
    private static final String PARAMETER_BIG_THUMBNAIL_WIDTH = "big_thumbnail_width_";
    private static final String PARAMETER_BIG_THUMBNAIL_HEIGHT = "big_thumbnail_height_";
    private static final String PARAMETER_CREATE_THUMBNAIL = "create_thumbnail_";
    private static final String PARAMETER_CREATE_BIG_THUMBNAIL = "create_big_thumbnail_";
    private static final String PARAMETER_ACTIVATE_DIRECTORY_RECORD = "activate_directory_record";
    private static final String PARAMETER_IS_INDEXED = "is_indexed";

    //property
    private static final String PROPERTY_DIRECTORY_ID_FORM_SEARCH_STYLE = "form-exportdirectory.directory.id_form_search_style";
    private static final String PROPERTY_DIRECTORY_ID_RESULT_LIST_STYLE = "form-exportdirectory.directory.id_result_list_style";
    private static final String PROPERTY_DIRECTORY_ID_RESULT_RECORD_STYLE = "form-exportdirectory.directory.id_result_record_style";

    /**
     * form-exportdirectory.form-entry-type_geolocation
     */
    private static final String PROPERTY_DIRECTORY_ID_ENTRY_TYPE_GEOLOCATION = "form-exportdirectory.directory-entry-type_geolocation";
    private static final String PROPERTY_NUMBER_RECORD_PER_PAGE = "form-exportdirectory.directory.number_record_per_page";
    private static final String PROPERTY_MAPPING_ENTRY_TYPE = "form-exportdirectory.mapping_entry_type.id";
    private static final String PROPERTY_MAPPING_ENTRY_TYPE_FILE = "form-exportdirectory.mapping_entry_type_file";
    private static final String PROPERTY_MAPPING_ENTRY_TYPE_IMAGE = "form-exportdirectory.mapping_entry_type_image";
    private static final String PROPERTY_MAPPING_ENTRY_TYPE_NUMBERING = "form-exportdirectory.mapping_entry_type_numbering";
    private static final String PROPERTY_TITLE_ENTRY_TYPE_NUMBERING = "form-exportdirectory.title_entry_type_numbering";
    private static final int PROPERTY_DEFAULT_DIRECTORY_ID_RESULT_RECORD_STYLE = 5;
    private static final int PROPERTY_DEFAULT_DIRECTORY_ID_FORM_SEARCH_STYLE = 3;
    private static final int PROPERTY_DEFAULT_DIRECTORY_ID_RESULT_LIST_STYLE = 4;
    private static final String FIELD_IMAGE = "image_full_size";
    private static final String FIELD_THUMBNAIL = "little_thumbnail";
    private static final String FIELD_BIG_THUMBNAIL = "big_thumbnail";
    private static final int INTEGER_QUALITY_MAXIMUM = 1;
    private static final int CONST_NONE = -1;

    //MESSAGES
    private static final String MESSAGE_ERROR_FIELD_THUMBNAIL = "module.form.exportdirectory.configuration_exportdirectory.message.errorThumbnail";
    private static final String MESSAGE_ERROR_FIELD_BIG_THUMBNAIL = "module.form.exportdirectory.configuration_exportdirectory.message.errorBigThumbnail";

    /**
     * ExportDirectoryUtils
     *
     */
    private ExportDirectoryUtils(  )
    {
    }

    /**
     * Get the directory entry for form entry
     * @param entryType the entry type
     * @return a list of entry types
     */
    public static List<EntryType> getDirectoryEntryForFormEntry( 
        fr.paris.lutece.plugins.form.business.EntryType entryType )
    {
        List<EntryType> listEntryTypeDirectory = new ArrayList<EntryType>(  );
        String strMappingEntryType = AppPropertiesService.getProperty( PROPERTY_MAPPING_ENTRY_TYPE + "_" +
                entryType.getIdType(  ) );

        if ( strMappingEntryType != null )
        {
            String[] strTabMappingEntryType = strMappingEntryType.split( "," );

            for ( int i = 0; i < strTabMappingEntryType.length; i++ )
            {
                listEntryTypeDirectory.add( EntryTypeHome.findByPrimaryKey( Integer.parseInt( strTabMappingEntryType[i] ),
                        PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ) ) );
            }
        }

        return listEntryTypeDirectory;
    }

    /**
     * Create directory by id form
     * @param nIdForm the id form
     * @param request the HTTP request
     * @param pluginForm the plugin form
     * @param pluginDirectory the plugin directory
     * @return a property error message if there is an error
     */
    public static String createDirectoryByIdForm( int nIdForm, HttpServletRequest request, Plugin pluginForm,
        Plugin pluginDirectory )
    {
        Plugin pluginExport = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );

        int nIdFormSearchStyle = AppPropertiesService.getPropertyInt( PROPERTY_DIRECTORY_ID_FORM_SEARCH_STYLE,
                PROPERTY_DEFAULT_DIRECTORY_ID_FORM_SEARCH_STYLE );
        int nIdResultListStyle = AppPropertiesService.getPropertyInt( PROPERTY_DIRECTORY_ID_RESULT_LIST_STYLE,
                PROPERTY_DEFAULT_DIRECTORY_ID_RESULT_LIST_STYLE );
        int nIdResultRecordStyle = AppPropertiesService.getPropertyInt( PROPERTY_DIRECTORY_ID_RESULT_RECORD_STYLE,
                PROPERTY_DEFAULT_DIRECTORY_ID_RESULT_RECORD_STYLE );
        int nNumberRecordPerPage = AppPropertiesService.getPropertyInt( PROPERTY_NUMBER_RECORD_PER_PAGE,
                PROPERTY_DEFAULT_DIRECTORY_ID_RESULT_RECORD_STYLE );
        String strRecordActivated = request.getParameter( PARAMETER_ACTIVATE_DIRECTORY_RECORD );
        String strIsIndexed = request.getParameter( PARAMETER_IS_INDEXED );

        if ( nIdResultRecordStyle < 1 )
        {
            nIdResultRecordStyle = PROPERTY_DEFAULT_DIRECTORY_ID_RESULT_RECORD_STYLE;
        }

        Form form = FormHome.findByPrimaryKey( nIdForm, pluginForm );
        Directory directory = new Directory(  );
        directory.setTitle( form.getTitle(  ) );
        directory.setDescription( form.getDescription(  ) );
        directory.setUnavailabilityMessage( form.getUnavailabilityMessage(  ) );
        directory.setIdFormSearchTemplate( nIdFormSearchStyle );
        directory.setIdResultListTemplate( nIdResultListStyle );
        directory.setNumberRecordPerPage( nNumberRecordPerPage );
        directory.setIdResultRecordTemplate( nIdResultRecordStyle );
        directory.setDateCreation( DirectoryUtils.getCurrentTimestamp(  ) );
        directory.setWorkgroup( form.getWorkgroup(  ) );
        directory.setRecordActivated( strRecordActivated != null );
        directory.setIndexed( strIsIndexed != null );

        if ( ( request.getParameter( PARAMETER_ID_WORKFLOW ) != null ) &&
                ( DirectoryUtils.convertStringToInt( request.getParameter( PARAMETER_ID_WORKFLOW ) ) != DirectoryUtils.CONSTANT_ID_NULL ) )
        {
            directory.setIdWorkflow( DirectoryUtils.convertStringToInt( request.getParameter( PARAMETER_ID_WORKFLOW ) ) );
        }

        //create directory
        DirectoryHome.create( directory, pluginDirectory );

        //create FormConfiguration
        FormConfiguration formConfiguration = new FormConfiguration(  );
        formConfiguration.setIdForm( form.getIdForm(  ) );
        formConfiguration.setIdDirectory( directory.getIdDirectory(  ) );
        FormConfigurationHome.insert( formConfiguration, pluginExport );

        //get List Entry
        List<fr.paris.lutece.plugins.form.business.IEntry> listFormEntry = FormUtils.getEntriesList( nIdForm, pluginForm );

        String error = null;

        if ( request.getParameter( PARAMETER_ADD_NUMBERING_ENTRY ).equals( "1" ) )
        {
            String strPrefix = request.getParameter( PARAMETER_ADD_NUMBERING_ENTRY_PREFIX );
            createDirectoryNumberingEntry( pluginDirectory, directory, strPrefix );
        }

        for ( fr.paris.lutece.plugins.form.business.IEntry formEntry : listFormEntry )
        {
            error = createDirectoryEntry( formEntry, request, pluginForm, pluginDirectory, directory, null );

            if ( error != null )
            {
                return error;
            }
        }

        return null;
    }

    /**
     * Create a directory entry
     * @param entryForm the entry form
     * @param request the HTTP request
     * @param pluginForm the pluginf form
     * @param pluginDirectory the plugin directory
     * @param directory the directory
     * @param entryGroup the entry group
     * @return a property error message if there is an error
     */
    public static String createDirectoryEntry( fr.paris.lutece.plugins.form.business.IEntry entryForm,
        HttpServletRequest request, Plugin pluginForm, Plugin pluginDirectory, Directory directory,
        fr.paris.lutece.plugins.directory.business.IEntry entryGroup )
    {
        Plugin pluginExport = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        String strMappingEntryType = AppPropertiesService.getProperty( PROPERTY_MAPPING_ENTRY_TYPE + "_" +
                entryForm.getEntryType(  ).getIdType(  ) );
        int nIdDirectoryEntryType = DirectoryUtils.CONSTANT_ID_NULL;

        if ( strMappingEntryType != null )
        {
            if ( strMappingEntryType.split( "," ).length == 1 )
            {
                nIdDirectoryEntryType = DirectoryUtils.convertStringToInt( strMappingEntryType );
            }
            else
            {
                nIdDirectoryEntryType = DirectoryUtils.convertStringToInt( request.getParameter( PARAMETER_ID_ENTRY_TYPE +
                            "_" + entryForm.getIdEntry(  ) ) );
            }

            EntryType entryType = EntryTypeHome.findByPrimaryKey( nIdDirectoryEntryType, pluginDirectory );
            fr.paris.lutece.plugins.directory.business.IEntry entryDirectory = DirectoryUtils.createEntryByType( nIdDirectoryEntryType,
                    pluginDirectory );

            if ( entryDirectory != null )
            {
                entryDirectory.setEntryType( entryType );

                if ( isGeolocationEntry( entryType.getIdType(  ) ) )
                {
                    entryDirectory.setMapProvider( MapProviderManager.getMapProvider( request.getParameter( PARAMETER_PREFIX_KEY_GEOLOCATION +
                                entryForm.getIdEntry(  ) ) ) );
                }

                //if it's an image
                if ( entryType.getIdType(  ) == 10 )
                {
                    entryDirectory.setDisplayHeight( CONST_NONE );
                    entryDirectory.setDisplayWidth( CONST_NONE );
                }

                entryDirectory.setDirectory( directory );
                entryDirectory.setTitle( entryForm.getTitle(  ) );
                entryDirectory.setComment( entryForm.getComment(  ) );
                entryDirectory.setHelpMessage( entryForm.getHelpMessage(  ) );
                entryDirectory.setHelpMessageSearch( entryForm.getHelpMessage(  ) );

                if ( request.getParameter( PARAMETER_IS_IN_RESULT_LIST + String.valueOf( entryForm.getIdEntry(  ) ) ) != null )
                {
                    entryDirectory.setShownInResultList( true );
                }
                else
                {
                    entryDirectory.setShownInResultList( false );
                }

                if ( request.getParameter( PARAMETER_IS_IN_SEARCH + String.valueOf( entryForm.getIdEntry(  ) ) ) != null )
                {
                    entryDirectory.setIndexed( true );
                }
                else
                {
                    entryDirectory.setIndexed( false );
                }

                entryDirectory.setShownInResultRecord( true );
                entryDirectory.setShownInExport( true );
                entryDirectory.setMandatory( entryForm.isMandatory(  ) );

                //For entry type sql
                entryDirectory.setRequestSQL( entryForm.getComment(  ) );

                if ( entryGroup != null )
                {
                    entryDirectory.setParent( entryGroup );
                }

                EntryHome.create( entryDirectory, pluginDirectory );

                //add association between entry Form and entry directory
                EntryConfiguration entryConfiguration = new EntryConfiguration(  );
                entryConfiguration.setIdFormEntry( entryForm.getIdEntry(  ) );
                entryConfiguration.setIdDirectoryEntry( entryDirectory.getIdEntry(  ) );
                entryConfiguration.setIdForm( entryForm.getForm(  ).getIdForm(  ) );
                EntryConfigurationHome.insert( entryConfiguration, pluginExport );

                createAllDirectoryField( entryForm.getIdEntry(  ), entryDirectory, pluginForm, pluginDirectory );

                if ( isDirectoryImageType( entryDirectory.getEntryType(  ).getIdType(  ) ) )
                {
                    //create the fields here for image because thumbnails are not generated in form so there is no formfield for thumbnails
                    int nIdEntryForm = entryForm.getIdEntry(  );
                    String strCreateThumbnail = request.getParameter( PARAMETER_CREATE_THUMBNAIL + nIdEntryForm );
                    String strCreateBigThumbnail = request.getParameter( PARAMETER_CREATE_BIG_THUMBNAIL + nIdEntryForm );
                    String strThumbnailWidth = request.getParameter( PARAMETER_THUMBNAIL_WIDTH + nIdEntryForm );
                    String strThumbnailHeight = request.getParameter( PARAMETER_THUMBNAIL_HEIGHT + nIdEntryForm );
                    String strBigThumbnailWidth = request.getParameter( PARAMETER_BIG_THUMBNAIL_WIDTH + nIdEntryForm );
                    String strBigThumbnailHeight = request.getParameter( PARAMETER_BIG_THUMBNAIL_HEIGHT + nIdEntryForm );

                    int nThumbnailWidth = -1;
                    int nThumbnailHeight = -1;
                    int nBigThumbnailHeight = -1;
                    int nBigThumbnailWidth = -1;

                    if ( strThumbnailWidth != null )
                    {
                        nThumbnailWidth = DirectoryUtils.convertStringToInt( strThumbnailWidth );
                    }

                    if ( strThumbnailHeight != null )
                    {
                        nThumbnailHeight = DirectoryUtils.convertStringToInt( strThumbnailHeight );
                    }

                    if ( strBigThumbnailWidth != null )
                    {
                        nBigThumbnailWidth = DirectoryUtils.convertStringToInt( strBigThumbnailWidth );
                    }

                    if ( strBigThumbnailHeight != null )
                    {
                        nBigThumbnailHeight = DirectoryUtils.convertStringToInt( strBigThumbnailHeight );
                    }

                    if ( strCreateThumbnail != null )
                    {
                        if ( ( nThumbnailWidth <= 0 ) || ( nThumbnailHeight <= 0 ) )
                        {
                            return MESSAGE_ERROR_FIELD_THUMBNAIL;
                        }

                        fr.paris.lutece.plugins.directory.business.Field thumbnailField = new Field(  );
                        thumbnailField.setWidth( nThumbnailWidth );
                        thumbnailField.setHeight( nThumbnailHeight );
                        thumbnailField.setValue( FIELD_THUMBNAIL );
                        thumbnailField.setEntry( entryDirectory );
                        thumbnailField.setShownInResultList( true );
                        fr.paris.lutece.plugins.directory.business.FieldHome.create( thumbnailField, pluginDirectory );
                    }

                    if ( strCreateBigThumbnail != null )
                    {
                        if ( ( nBigThumbnailWidth <= 0 ) || ( nBigThumbnailHeight <= 0 ) )
                        {
                            return MESSAGE_ERROR_FIELD_BIG_THUMBNAIL;
                        }

                        fr.paris.lutece.plugins.directory.business.Field bigThumbnailField = new Field(  );
                        bigThumbnailField.setWidth( nBigThumbnailWidth );
                        bigThumbnailField.setHeight( nBigThumbnailHeight );
                        bigThumbnailField.setValue( FIELD_BIG_THUMBNAIL );
                        bigThumbnailField.setEntry( entryDirectory );
                        fr.paris.lutece.plugins.directory.business.FieldHome.create( bigThumbnailField, pluginDirectory );
                    }
                }

                if ( entryForm.getEntryType(  ).getGroup(  ) )
                {
                    String error = null;

                    for ( fr.paris.lutece.plugins.form.business.IEntry entryFormChildren : entryForm.getChildren(  ) )
                    {
                        error = createDirectoryEntry( entryFormChildren, request, pluginForm, pluginDirectory,
                                directory, entryDirectory );

                        if ( error != null )
                        {
                            return error;
                        }
                    }
                }
            }
        }

        return null;
    }

    /**
     * Create a directory entry type numbering
     * @param pluginDirectory the plugin directory
     * @param directory the directory
     * @param strPrefix the prefix of the entry numbering
     */
    public static void createDirectoryNumberingEntry( Plugin pluginDirectory, Directory directory, String strPrefix )
    {
        int directoryNumberingType = DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( 
                    PROPERTY_MAPPING_ENTRY_TYPE_NUMBERING ) );
        EntryType entryType = EntryTypeHome.findByPrimaryKey( directoryNumberingType, pluginDirectory );
        fr.paris.lutece.plugins.directory.business.IEntry entryDirectory = DirectoryUtils.createEntryByType( directoryNumberingType,
                pluginDirectory );

        if ( entryDirectory != null )
        {
            entryDirectory.setEntryType( entryType );
            entryDirectory.setDirectory( directory );
            entryDirectory.setTitle( AppPropertiesService.getProperty( PROPERTY_TITLE_ENTRY_TYPE_NUMBERING ) );
            entryDirectory.setHelpMessageSearch( "" );
            entryDirectory.setComment( "" );
            entryDirectory.setIndexed( true );
            entryDirectory.setShownInAdvancedSearch( false );
            entryDirectory.setShownInResultList( true );
            entryDirectory.setShownInResultRecord( true );

            EntryHome.create( entryDirectory, pluginDirectory );

            Field fieldDirectory = new Field(  );
            fieldDirectory.setEntry( entryDirectory );
            fieldDirectory.setTitle( strPrefix );
            fieldDirectory.setDefaultValue( false );
            fieldDirectory.setHeight( 0 );
            fieldDirectory.setWidth( 0 );
            fieldDirectory.setMaxSizeEnter( 0 );
            fieldDirectory.setValue( "1" );
            fieldDirectory.setValueTypeDate( null );

            fr.paris.lutece.plugins.directory.business.FieldHome.create( fieldDirectory, pluginDirectory );
        }
    }

    /**
     * Create a directory entry type numbering
     * @param pluginDirectory the plugin directory
     * @param directory the directory
     */
    public static void createDirectoryNumberingEntry( Plugin pluginDirectory, Directory directory )
    {
        createDirectoryNumberingEntry( pluginDirectory, directory, StringUtils.EMPTY );
    }

    /**
     * Create all directory fields
     * @param nIdEntryForm the id entry form
     * @param entryDirectory the entry directory
     * @param pluginForm the plugin form
     * @param pluginDirectory the plugin directory
     */
    public static void createAllDirectoryField( int nIdEntryForm,
        fr.paris.lutece.plugins.directory.business.IEntry entryDirectory, Plugin pluginForm, Plugin pluginDirectory )
    {
        fr.paris.lutece.plugins.form.business.IEntry entryForm = fr.paris.lutece.plugins.form.business.EntryHome.findByPrimaryKey( nIdEntryForm,
                pluginForm );
        fr.paris.lutece.plugins.directory.business.Field fieldDirectory = null;

        if ( entryForm.getFields(  ) != null )
        {
            for ( fr.paris.lutece.plugins.form.business.Field fieldForm : entryForm.getFields(  ) )
            {
                fieldDirectory = new Field(  );
                fieldDirectory.setEntry( entryDirectory );
                fieldDirectory.setTitle( fieldForm.getTitle(  ) );
                fieldDirectory.setDefaultValue( fieldForm.isDefaultValue(  ) );
                fieldDirectory.setHeight( fieldForm.getHeight(  ) );
                fieldDirectory.setWidth( fieldForm.getWidth(  ) );
                fieldDirectory.setMaxSizeEnter( fieldForm.getMaxSizeEnter(  ) );

                fieldDirectory.setValueTypeDate( fieldForm.getValueTypeDate(  ) );
                fieldDirectory.setShownInResultRecord( true );

                if ( ( entryForm.getEntryType(  ).getIdType(  ) == AppPropertiesService.getPropertyInt( 
                            ProcessorExportdirectory.PROPERTY_FORM_ENTRY_TYPE_IMAGE, 12 ) ) &&
                        StringUtils.isBlank( fieldForm.getTitle(  ) ) )
                {
                    fieldDirectory.setValue( FIELD_IMAGE );
                }
                else
                {
                    fieldDirectory.setValue( fieldForm.getValue(  ) );
                }

                fr.paris.lutece.plugins.directory.business.FieldHome.create( fieldDirectory, pluginDirectory );
            }
        }
    }

    /**
     * Create a directory record
     * @param request the HTTP request
     * @param formConfiguration the form configuration
     * @param formSubmit the form submit
     * @param pluginForm the plugin form
     * @param pluginDirectory the plugin directory
     * @throws UnsupportedEncodingException error if there is an encoding problem
     */
    public static void createDirectoryRecord( HttpServletRequest request, FormConfiguration formConfiguration,
        FormSubmit formSubmit, Plugin pluginForm, Plugin pluginDirectory )
        throws UnsupportedEncodingException
    {
        Plugin pluginExport = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        Record record = new Record(  );
        Directory directory = DirectoryHome.findByPrimaryKey( formConfiguration.getIdDirectory(  ), pluginDirectory );
        record.setDirectory( directory );
        record.setDateCreation( formSubmit.getDateResponse(  ) );
        record.setEnabled( directory.isRecordActivated(  ) );
        record.setListRecordField( new ArrayList<RecordField>(  ) );
        RecordHome.create( record, pluginDirectory );

        ResponseService responseService = (ResponseService) SpringContextService.getPluginBean( FormPlugin.PLUGIN_NAME,
                FormUtils.BEAN_FORM_RESPONSE_SERVICE );

        // The index is used to distinguish the thumbnails of one image from another
        int nIndexForImg = 0;

        for ( Response response : formSubmit.getListResponse(  ) )
        {
            EntryConfiguration entryConfiguration = EntryConfigurationHome.findByPrimaryKey( formSubmit.getForm(  )
                                                                                                       .getIdForm(  ),
                    response.getEntry(  ).getIdEntry(  ), pluginExport );

            if ( entryConfiguration != null )
            {
                IEntry entryDirectory = EntryHome.findByPrimaryKey( entryConfiguration.getIdDirectoryEntry(  ),
                        pluginDirectory );

                if ( entryDirectory != null )
                {
                    RecordField recordField = new RecordField(  );
                    recordField.setEntry( entryDirectory );

                    // Entry of type file
                    if ( isDirectoryFileType( entryDirectory.getEntryType(  ).getIdType(  ) ) )
                    {
                        Response responseFile = responseService.findByPrimaryKey( response.getIdResponse(  ), true );

                        if ( ( responseFile.getFile(  ) != null ) &&
                                ( responseFile.getFile(  ).getPhysicalFile(  ) != null ) &&
                                ( responseFile.getFile(  ).getPhysicalFile(  ).getValue(  ) != null ) )
                        {
                            File file = new File(  );
                            file.setTitle( responseFile.getFile(  ).getTitle(  ) );
                            file.setExtension( FilenameUtils.getExtension( responseFile.getFile(  ).getTitle(  ) ) );
                            file.setMimeType( responseFile.getFile(  ).getMimeType(  ) );

                            PhysicalFile physicalFile = new PhysicalFile(  );
                            physicalFile.setValue( responseFile.getFile(  ).getPhysicalFile(  ).getValue(  ) );
                            file.setPhysicalFile( physicalFile );
                            file.setSize( responseFile.getFile(  ).getSize(  ) );

                            recordField.setFile( file );
                        }
                    }

                    // Entry of type file
                    else if ( isDirectoryImageType( entryDirectory.getEntryType(  ).getIdType(  ) ) )
                    {
                        Response responseFile = responseService.findByPrimaryKey( response.getIdResponse(  ), true );

                        if ( ( responseFile.getFile(  ) != null ) &&
                                ( responseFile.getFile(  ).getPhysicalFile(  ) != null ) &&
                                ( responseFile.getFile(  ).getPhysicalFile(  ).getValue(  ) != null ) )
                        {
                            File file = new File(  );
                            file.setTitle( responseFile.getFile(  ).getTitle(  ) );
                            file.setExtension( FilenameUtils.getExtension( responseFile.getFile(  ).getTitle(  ) ) );
                            file.setMimeType( responseFile.getFile(  ).getMimeType(  ) );

                            PhysicalFile physicalFile = new PhysicalFile(  );
                            physicalFile.setValue( responseFile.getFile(  ).getPhysicalFile(  ).getValue(  ) );
                            file.setPhysicalFile( physicalFile );
                            file.setSize( responseFile.getFile(  ).getSize(  ) );

                            recordField.setFile( file );

                            //Create thumbnails records
                            try
                            {
                                //verify that the file is an image
                                ImageIO.read( new ByteArrayInputStream( 
                                        responseFile.getFile(  ).getPhysicalFile(  ).getValue(  ) ) );

                                for ( Field field : entryDirectory.getFields(  ) )
                                {
                                    if ( ( field.getValue(  ) != null ) && ( field.getValue(  ).equals( FIELD_IMAGE ) ) )
                                    {
                                        recordField.setValue( FIELD_IMAGE + FormUtils.CONSTANT_UNDERSCORE +
                                            nIndexForImg );
                                        recordField.setField( field );
                                    }

                                    if ( ( field.getValue(  ) != null ) &&
                                            ( ( field.getValue(  ).equals( FIELD_THUMBNAIL ) ) ||
                                            ( field.getValue(  ).equals( FIELD_BIG_THUMBNAIL ) ) ) )
                                    {
                                        byte[] resizedImage = ImageUtil.resizeImage( responseFile.getFile(  )
                                                                                                 .getPhysicalFile(  )
                                                                                                 .getValue(  ),
                                                String.valueOf( field.getWidth(  ) ),
                                                String.valueOf( field.getHeight(  ) ), INTEGER_QUALITY_MAXIMUM );

                                        RecordField thbnailRecordField = new RecordField(  );
                                        thbnailRecordField.setEntry( entryDirectory );

                                        PhysicalFile thbnailPhysicalFile = new PhysicalFile(  );
                                        thbnailPhysicalFile.setValue( resizedImage );

                                        File thbnailFile = new File(  );
                                        thbnailFile.setTitle( responseFile.getFile(  ).getTitle(  ) );
                                        thbnailFile.setExtension( FilenameUtils.getExtension( 
                                                responseFile.getFile(  ).getTitle(  ) ) );
                                        thbnailFile.setMimeType( responseFile.getFile(  ).getMimeType(  ) );
                                        thbnailFile.setPhysicalFile( thbnailPhysicalFile );
                                        thbnailFile.setSize( resizedImage.length );

                                        thbnailRecordField.setFile( thbnailFile );
                                        thbnailRecordField.setField( field );

                                        thbnailRecordField.setRecord( record );

                                        if ( field.getValue(  ).equals( FIELD_THUMBNAIL ) )
                                        {
                                            thbnailRecordField.setValue( FIELD_THUMBNAIL +
                                                FormUtils.CONSTANT_UNDERSCORE + nIndexForImg );
                                        }
                                        else if ( field.getValue(  ).equals( FIELD_BIG_THUMBNAIL ) )
                                        {
                                            thbnailRecordField.setValue( FIELD_BIG_THUMBNAIL +
                                                FormUtils.CONSTANT_UNDERSCORE + nIndexForImg );
                                        }

                                        RecordFieldHome.create( thbnailRecordField, pluginDirectory );
                                    }
                                }
                            }
                            catch ( IOException e )
                            {
                                AppLogService.error( e );
                            }
                        }

                        nIndexForImg++;
                    }
                    else
                    {
                        String strValue = DirectoryUtils.EMPTY_STRING;

                        if ( StringUtils.isNotBlank( response.getResponseValue(  ) ) )
                        {
                            strValue = response.getResponseValue(  );
                        }

                        recordField.setValue( strValue );

                        if ( ( response.getField(  ) != null ) )
                        {
                            Field fieldDirectory = null;

                            // entry of type geolocation ==> get response value
                            if ( ( entryDirectory != null ) && ( entryDirectory.getEntryType(  ) != null ) &&
                                    ( isGeolocationEntry( entryDirectory.getEntryType(  ).getIdType(  ) ) ) )
                            {
                                fr.paris.lutece.plugins.form.business.Field formField = fr.paris.lutece.plugins.form.business.FieldHome.findByPrimaryKey( response.getField(  )
                                                                                                                                                                  .getIdField(  ),
                                        pluginForm );
                                fieldDirectory = FieldHome.findByValue( entryDirectory.getIdEntry(  ),
                                        formField.getValue(  ), pluginDirectory );
                            }

                            // Entry of type select box, checkbox, radio
                            else if ( ( response.getField(  ).getIdField(  ) != DirectoryUtils.CONSTANT_ID_NULL ) )
                            {
                                fieldDirectory = FieldHome.findByValue( entryDirectory.getIdEntry(  ), strValue,
                                        pluginDirectory );
                            }

                            recordField.setField( fieldDirectory );
                        }
                    }

                    recordField.setRecord( record );
                    RecordFieldHome.create( recordField, pluginDirectory );
                }
            }
        }

        EntryFilter entryFilter = new EntryFilter(  );
        entryFilter.setIdDirectory( directory.getIdDirectory(  ) );

        List<IEntry> listEntry = EntryHome.getEntryList( entryFilter, pluginDirectory );

        for ( IEntry entry : listEntry )
        {
            if ( isDirectoryNumberingType( entry.getEntryType(  ).getIdType(  ) ) )
            {
                RecordField recordField = new RecordField(  );
                recordField.setEntry( entry );
                recordField.setRecord( record );

                List<Field> listField = FieldHome.getFieldListByIdEntry( entry.getIdEntry(  ), pluginDirectory );
                int numbering = DirectoryService.getInstance(  ).getMaxNumber( entry );
                recordField.setValue( String.valueOf( numbering ) );
                RecordFieldHome.create( recordField, pluginDirectory );

                listField.get( 0 ).setValue( String.valueOf( numbering + 1 ) );
                FieldHome.update( listField.get( 0 ), pluginDirectory );
            }
        }

        WorkflowService workflowService = WorkflowService.getInstance(  );

        if ( workflowService.isAvailable(  ) && ( directory.getIdWorkflow(  ) != DirectoryUtils.CONSTANT_ID_NULL ) )
        {
            State state = workflowService.getState( record.getIdRecord(  ), Record.WORKFLOW_RESOURCE_TYPE,
                    directory.getIdWorkflow(  ), Integer.valueOf( directory.getIdDirectory(  ) ), null );

            if ( state != null )
            {
                workflowService.executeActionAutomatic( record.getIdRecord(  ), Record.WORKFLOW_RESOURCE_TYPE,
                    directory.getIdWorkflow(  ), Integer.valueOf( directory.getIdDirectory(  ) ) );
            }
            else
            {
                AppLogService.info( " FormExportDirectory : No initial state for workflow : " +
                    directory.getIdWorkflow(  ) + ". The form will be recorded but will not be shown in directory." );
            }
        }
    }

    /**
     * Check if the given id entry type is an entry type file
     * @param nIdEntryType the id entry type
     * @return true if it is an entry type file, false otherwise
     */
    public static boolean isDirectoryFileType( int nIdEntryType )
    {
        String strMappingFileType = AppPropertiesService.getProperty( PROPERTY_MAPPING_ENTRY_TYPE_FILE );

        if ( strMappingFileType != null )
        {
            String[] tabFileType = strMappingFileType.split( "," );

            for ( String strIdTypeFile : tabFileType )
            {
                if ( nIdEntryType == DirectoryUtils.convertStringToInt( strIdTypeFile ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if the given id entry type is an entry type image
     * @param nIdEntryType the id entry type
     * @return true if it is an entry type image
     */
    public static boolean isDirectoryImageType( int nIdEntryType )
    {
        String strMappingImageType = AppPropertiesService.getProperty( PROPERTY_MAPPING_ENTRY_TYPE_IMAGE );

        if ( strMappingImageType != null )
        {
            String[] tabImageType = strMappingImageType.split( "," );

            for ( String strIdTypeFile : tabImageType )
            {
                if ( nIdEntryType == DirectoryUtils.convertStringToInt( strIdTypeFile ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Check if the given id entry type is an entry type numbering
     * @param nIdEntryType the id entry type
     * @return true if it is an entry type numbering, false otherwise
     */
    public static boolean isDirectoryNumberingType( int nIdEntryType )
    {
        String strMappingFileType = AppPropertiesService.getProperty( PROPERTY_MAPPING_ENTRY_TYPE_NUMBERING );

        if ( strMappingFileType != null )
        {
            String[] tabFileType = strMappingFileType.split( "," );

            for ( String strIdTypeFile : tabFileType )
            {
                if ( nIdEntryType == DirectoryUtils.convertStringToInt( strIdTypeFile ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Finds if the given form entry is a geolocation entry
     * @param formEntry the form entry
     * @return <code>true</code> if the entry type is a geolocation type, <code>false</code> otherwise.
     */
    public static boolean isGeolocationFormEntry( fr.paris.lutece.plugins.form.business.IEntry formEntry )
    {
        List<EntryType> entryTypes = getDirectoryEntryForFormEntry( formEntry.getEntryType(  ) );

        for ( EntryType entryType : entryTypes )
        {
            if ( isGeolocationEntry( entryType.getIdType(  ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Geolocation type ids are set with the proprety {@link ExportDirectoryUtils#PROPERTY_DIRECTORY_ID_ENTRY_TYPE_GEOLOCATION}
     * @param nIdEntryType the type to check
     * @return <code>true</code> if the entry type is a geolocation type, <code>false</code> otherwise.
     */
    public static boolean isGeolocationEntry( int nIdEntryType )
    {
        String strIdsGeolocation = AppPropertiesService.getProperty( PROPERTY_DIRECTORY_ID_ENTRY_TYPE_GEOLOCATION,
                DirectoryUtils.EMPTY_STRING );

        if ( StringUtils.isNotBlank( strIdsGeolocation ) )
        {
            String[] tabGeolocationType = strIdsGeolocation.split( "," );

            for ( String strIdTypeGeolocation : tabGeolocationType )
            {
                if ( nIdEntryType == DirectoryUtils.convertStringToInt( strIdTypeGeolocation ) )
                {
                    return true;
                }
            }
        }

        return false;
    }
}
