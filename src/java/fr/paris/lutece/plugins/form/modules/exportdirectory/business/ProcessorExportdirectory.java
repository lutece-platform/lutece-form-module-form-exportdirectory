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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.directory.business.DirectoryHome;
import fr.paris.lutece.plugins.directory.business.EntryFilter;
import fr.paris.lutece.plugins.directory.business.EntryHome;
import fr.paris.lutece.plugins.directory.business.EntryType;
import fr.paris.lutece.plugins.directory.business.MapProviderManager;
import fr.paris.lutece.plugins.directory.service.DirectoryPlugin;
import fr.paris.lutece.plugins.directory.utils.DirectoryUtils;
import fr.paris.lutece.plugins.form.business.Form;
import fr.paris.lutece.plugins.form.business.FormHome;
import fr.paris.lutece.plugins.form.business.FormSubmit;
import fr.paris.lutece.plugins.form.business.FormSubmitHome;
import fr.paris.lutece.plugins.form.business.outputprocessor.OutputProcessor;
import fr.paris.lutece.plugins.form.modules.exportdirectory.service.ExportdirectoryPlugin;
import fr.paris.lutece.plugins.form.modules.exportdirectory.service.ExportdirectoryResourceIdService;
import fr.paris.lutece.plugins.form.modules.exportdirectory.utils.ExportDirectoryUtils;
import fr.paris.lutece.plugins.form.service.FormPlugin;
import fr.paris.lutece.plugins.form.service.IResponseService;
import fr.paris.lutece.plugins.form.service.entrytype.EntryTypeImage;
import fr.paris.lutece.plugins.form.utils.EntryTypeGroupUtils;
import fr.paris.lutece.plugins.form.utils.FormConstants;
import fr.paris.lutece.plugins.form.utils.FormUtils;
import fr.paris.lutece.plugins.form.web.FormJspBean;
import fr.paris.lutece.plugins.genericattributes.business.Entry;
import fr.paris.lutece.plugins.genericattributes.business.Field;
import fr.paris.lutece.plugins.genericattributes.business.FieldHome;
import fr.paris.lutece.plugins.genericattributes.business.ResponseFilter;
import fr.paris.lutece.portal.business.rbac.RBAC;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.rbac.RBACService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * 
 * @author FBU
 * 
 */
public class ProcessorExportdirectory extends OutputProcessor
{
    public static final String PROPERTY_FORM_ENTRY_TYPE_IMAGE = "form-exportdirectory.form-entry-type_image";

    // Templates
    private static final String TEMPLATE_CONFIGURATION_EXPORTDIRECTORY = "admin/plugins/form/modules/exportdirectory/processorexportdirectory/configuration_exportdirectory.html";

    // Parameters
    private static final String PARAMETER_ID_FORM = "id_form";
    private static final String PARAMETER_EXPORT_ALL = "export_all";
    private static final String PARAMETER_SELECTED_ACTION_EXPORTDIRECTORY = "selected_action_exportdirectory";
    private static final String PARAMETER_ID_DIRECTORY = "id_directory";
    private static final String PARAMETER_ID_DIRECTORY_FOR_FORM_ENTRY = "id_entry_directory_for_form_entry";
    private static final String PARAMETER_ID_ENTRY_TYPE = "id_entry_type";

    // Markers
    private static final String MARK_FORM = "form";
    private static final String MARK_LOCALE = "locale";
    private static final String MARK_FORM_CONFIGURATION = "form_configuration";
    private static final String MARK_ENTRY_CONFIGURATION_LIST = "entry_configuration_list";
    private static final String MARK_DIRECTORY_LIST = "directory_list";
    private static final String MARK_DIRECTORY_LIST_IS_EMPTY = "directory_list_is_empty";
    private static final String MARK_DIRECTORY_LIST_DEFAULT_ITEM = "directory_list_default_item";
    private static final String MARK_USE_DIRECTORY_EXIST = "use_directory_exist";
    private static final String MARK_CREATE_DIRECTORY = "create_directory";
    private static final String MARK_USE_DIRECTORY_ENTRY_FORM = "use_directory_form_entry";
    private static final String MARK_USE_DIRECTORY_ENTRY_FORM_TYPE = "use_directory_form_entry_type";
    private static final String MARK_USE_DIRECTORY_LIST_ENTRY_DIRECTORY = "use_directory_list_directory_entry";
    private static final String MARK_USE_DIRECTORY_DEFAULT_ENTRY_DIRECTORY = "use_directory_default_directory_entry";
    private static final String MARK_FORM_ENTRY = "form_entry";
    private static final String MARK_FORM_ENTRY_CONFIGURATION = "form_entry_configuration";
    private static final String MARK_DIRECTORY_ENTRY_TYPE = "directory_entry_type";
    private static final String MARK_MAPPED = "is_mapped";
    private static final String MARK_MAPPED_DIRECTORY_TITLE = "mapped_directory_title";
    private static final String MARK_RECORD_FORM = "record_form";
    private static final String MARK_PERMISSION_MANAGE = "permission_manage";
    private static final String MARK_WORKFLOW = "workflow_list";
    private static final String MARK_FORM_ENTRY_FILE = "entry_type_file";
    private static final String MARK_FORM_ENTRY_IMAGE = "entry_type_image";
    private static final String MARK_MAP_ENTRY_GEOLOCATION = "map_entry_geolocalisation";
    private static final String MARK_LIST_DIRECTORY_MAP_PROVIDERS = "list_directory_map_providers";
    private static final String MARK_MAP_ENTRY_IMAGE = "map_entry_image";

    // I18n messages
    private static final String MESSAGE_ERROR_ENTRY_FORM_NOT_TYPE_DIRECTORY = "module.form.exportdirectory.configuration_exportdirectory.message.errorCreateDirectory";
    private static final String MESSAGE_ERROR_ENTRY_GEOLOCATION_MISSING = "module.form.exportdirectory.configuration_exportdirectory.message.errorGeolocationMissing";
    private static final String MESSAGE_ERROR_ENTRY_FORM_NOT_ID_DIRECTORY = "module.form.exportdirectory.configuration_exportdirectory.message.errorUseDirectoryExist";
    private static final String MESSAGE_ERROR_TWO_ENTRY_FORM_FOR_ONE_ID_DIRECTORY = "module.form.exportdirectory.configuration_exportdirectory.message.errorIdDirectoryUseDirectoryExist";

    // Miscellaneous
    private static final String REGEX_ID = "^[\\d]+$";
    private static final String ACTION_CREATE_DIRECTORY = "create_directory";
    private static final String ACTION_USE_DIRECTORY_EXIST = "use_directory_exist";
    private static final String ACTION_DELETE_MAPPING_DIRECTORY = "delete_mapping_directory";

    // property
    private static final String PROPERTY_FORM_ENTRY_TYPE_COMMENT = "form-exportdirectory.form-entry-type_comment";
    private static final String PROPERTY_FORM_ENTRY_TYPE_FILE = "form-exportdirectory.form-entry-type_file";

    // DEFAULT
    private static final String DEFAULT_FORM_IMAGE_TYPE = "12";

    /**
     * {@inheritDoc}
     */
    public String getOutputConfigForm( HttpServletRequest request, Form form, Locale locale, Plugin plugin )
    {
        boolean apply = false;
        int nFormEntryTypeComment = DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_FORM_ENTRY_TYPE_COMMENT ) );
        Plugin pluginExportdirectory = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Map<String, Object> model = new HashMap<String, Object>( );
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ), pluginExportdirectory );

        model.put( MARK_PERMISSION_MANAGE, RBACService.isAuthorized( ExportdirectoryResourceIdService.RESOURCE_TYPE, RBAC.WILDCARD_RESOURCES_ID,
                ExportdirectoryResourceIdService.PERMISSION_MANAGE_EXPORT_DIRECTORY, AdminUserService.getAdminUser( request ) ) );

        boolean isMapped = false;

        if ( formConfiguration == null )
        {
            formConfiguration = new FormConfiguration( );
            formConfiguration.setIdForm( form.getIdForm( ) );
        }
        else
        {
            isMapped = true;
            model.put( MARK_MAPPED_DIRECTORY_TITLE, DirectoryHome.findByPrimaryKey( formConfiguration.getIdDirectory( ), pluginDirectory ).getTitle( ) );
        }

        ReferenceList listDirectory = DirectoryHome.getDirectoryList( pluginDirectory );
        Collection<Map<String, Object>> entryConfigurationList = new ArrayList<Map<String, Object>>( );
        Collection<Map<String, Object>> formEntryTypeWithSeveralDirectoryEntryType = new ArrayList<Map<String, Object>>( );
        Collection<Map<String, Object>> formGeolocalisationEntriesMap = new ArrayList<Map<String, Object>>( );
        Collection<Map<String, Object>> formEntriesImageConfig = new ArrayList<Map<String, Object>>( );
        Map<Integer, Integer> mapIdEntryNumIter = new LinkedHashMap<>( );

        for ( Entry entry : FormUtils.getAllQuestionList( form.getIdForm( ), plugin ) )
        {
            int nIdEntry = entry.getIdEntry( );

            // Retrieve the current iteration number for the current entry
            boolean bEntryBelongIterableGroup = EntryTypeGroupUtils.entryBelongIterableGroup( entry );
            int nCurrentIterationNumber = FormConstants.DEFAULT_ITERATION_NUMBER;
            if ( bEntryBelongIterableGroup )
            {
                Integer nIterationNumber = mapIdEntryNumIter.get( nIdEntry );
                if ( nIterationNumber != null )
                {
                    mapIdEntryNumIter.put( nIdEntry, nIterationNumber + NumberUtils.INTEGER_ONE );
                }
                else
                {
                    mapIdEntryNumIter.put( nIdEntry, NumberUtils.INTEGER_ONE );
                }

                nCurrentIterationNumber = mapIdEntryNumIter.get( nIdEntry );
            }

            // Retrieve or create the entry configuration of the current entry
            int nIdForm = form.getIdForm( );
            EntryConfiguration entryConfigurationFromEntry = EntryConfigurationHome.findByPrimaryKey( nIdForm, nIdEntry, nCurrentIterationNumber,
                    pluginExportdirectory );

            if ( entryConfigurationFromEntry == null )
            {
                entryConfigurationFromEntry = new EntryConfiguration( );
                entryConfigurationFromEntry.setIdFormEntry( nIdEntry );
                entryConfigurationFromEntry.setIdForm( nIdForm );
                entryConfigurationFromEntry.setIterationNumber( nCurrentIterationNumber );
            }

            if ( ExportDirectoryUtils.isGeolocationFormEntry( entry ) )
            {
                Map<String, Object> mapGeolocalisationEntryConfiguration = new LinkedHashMap<>( );
                mapGeolocalisationEntryConfiguration.put( MARK_FORM_ENTRY, entry );
                mapGeolocalisationEntryConfiguration.put( MARK_FORM_ENTRY_CONFIGURATION, entryConfigurationFromEntry );

                formGeolocalisationEntriesMap.add( mapGeolocalisationEntryConfiguration );
            }

            fr.paris.lutece.plugins.genericattributes.business.EntryType entryType = entry.getEntryType( );
            if ( entryType != null && StringUtils.equals( entryType.getBeanName( ), EntryTypeImage.BEAN_NAME ) )
            {
                Map<String, Object> mapImageEntryConfiguration = new LinkedHashMap<>( );
                mapImageEntryConfiguration.put( MARK_FORM_ENTRY, entry );
                mapImageEntryConfiguration.put( MARK_FORM_ENTRY_CONFIGURATION, entryConfigurationFromEntry );

                formEntriesImageConfig.add( mapImageEntryConfiguration );
            }

            if ( entryType.getIdType( ) != nFormEntryTypeComment )
            {
                int nIdParent = NumberUtils.INTEGER_MINUS_ONE;
                int nMaximumIterationNumber = FormConstants.DEFAULT_ITERATION_NUMBER;
                Entry entryParent = entry.getParent( );
                if ( entryParent != null )
                {
                    nIdParent = entryParent.getIdEntry( );
                    nMaximumIterationNumber = EntryTypeGroupUtils.getEntryMaxIterationAllowed( nIdParent );
                }
                // Check if the entry is a conditional entry
                if ( entryParent == null && entry.getFieldDepend( ) != null )
                {
                    Field field = FieldHome.findByPrimaryKey( entry.getFieldDepend( ).getIdField( ) );
                    Entry entryField = fr.paris.lutece.plugins.genericattributes.business.EntryHome.findByPrimaryKey( field.getParentEntry( ).getIdEntry( ) );
                    while ( entryField.getParent( ) == null && entryField.getFieldDepend( ) != null )
                    {
                        field = FieldHome.findByPrimaryKey( entryField.getFieldDepend( ).getIdField( ) );
                        entryField = fr.paris.lutece.plugins.genericattributes.business.EntryHome.findByPrimaryKey( field.getParentEntry( ).getIdEntry( ) );
                    }

                    entryParent = entryField.getParent( );
                    if ( entryParent != null )
                    {
                        nIdParent = entryParent.getIdEntry( );
                        nMaximumIterationNumber = EntryTypeGroupUtils.getEntryMaxIterationAllowed( nIdParent );
                    }
                }
                FormEntryConfiguration formEntryConfiguration = new FormEntryConfiguration( entryConfigurationFromEntry, nIdParent, nMaximumIterationNumber );

                Map<String, Object> resourceActions = new HashMap<String, Object>( );
                Map<String, Object> entryFormType = new HashMap<String, Object>( );
                List<EntryType> listEntryTypeDirectory = ExportDirectoryUtils.getDirectoryEntryForFormEntry( entryType );
                if ( listEntryTypeDirectory.size( ) > 1 )
                {
                    entryFormType.put( MARK_FORM_ENTRY, entry );
                    entryFormType.put( MARK_FORM_ENTRY_CONFIGURATION, formEntryConfiguration );
                    entryFormType.put( MARK_DIRECTORY_ENTRY_TYPE, listEntryTypeDirectory );
                    formEntryTypeWithSeveralDirectoryEntryType.add( entryFormType );
                }

                List<fr.paris.lutece.plugins.directory.business.IEntry> listEntryDirectory = new ArrayList<fr.paris.lutece.plugins.directory.business.IEntry>( );

                if ( listDirectory != null && !listDirectory.isEmpty( ) )
                {
                    EntryFilter entryFilterDirectory = new EntryFilter( );

                    if ( request.getParameter( PARAMETER_ID_DIRECTORY ) != null )
                    {
                        entryFilterDirectory.setIdDirectory( Integer.parseInt( request.getParameter( PARAMETER_ID_DIRECTORY ) ) );
                        apply = true;
                    }
                    else
                    {
                        if ( isMapped )
                        {
                            entryFilterDirectory.setIdDirectory( formConfiguration.getIdDirectory( ) );
                        }
                        else
                        {
                            entryFilterDirectory.setIdDirectory( Integer.parseInt( listDirectory.get( 0 ).getCode( ) ) );
                        }
                    }

                    // Create the map which associate the list of Entry of each EntryType already treated
                    Map<EntryType, List<fr.paris.lutece.plugins.directory.business.IEntry>> mapEntryTypeListEntry = new LinkedHashMap<>( );
                    for ( EntryType entryTypeDirectory : ExportDirectoryUtils.getDirectoryEntryForFormEntry( entryType ) )
                    {
                        List<fr.paris.lutece.plugins.directory.business.IEntry> listEntryDirectoryForEntryTypeForm = new ArrayList<>( );

                        if ( mapEntryTypeListEntry.containsKey( entryTypeDirectory ) )
                        {
                            listEntryDirectoryForEntryTypeForm = mapEntryTypeListEntry.get( entryTypeDirectory );
                        }
                        else
                        {
                            entryFilterDirectory.setIdType( entryTypeDirectory.getIdType( ) );
                            listEntryDirectoryForEntryTypeForm = EntryHome.getEntryList( entryFilterDirectory, pluginDirectory );

                            mapEntryTypeListEntry.put( entryTypeDirectory, listEntryDirectoryForEntryTypeForm );
                        }

                        listEntryDirectory.addAll( listEntryDirectoryForEntryTypeForm );
                    }
                }

                ReferenceList referenceListEntryDirectory = ReferenceList.convert( listEntryDirectory, "idEntry", "title", true );

                referenceListEntryDirectory.addItem( -1, "" );
                resourceActions.put( MARK_USE_DIRECTORY_LIST_ENTRY_DIRECTORY, referenceListEntryDirectory );

                EntryConfiguration entryConfigurationDefaultEntry = EntryConfigurationHome.findByPrimaryKey( nIdForm, nIdEntry, nCurrentIterationNumber,
                        pluginExportdirectory );
                if ( isMapped && entryConfigurationDefaultEntry != null && !apply )
                {
                    resourceActions.put( MARK_USE_DIRECTORY_DEFAULT_ENTRY_DIRECTORY, entryConfigurationDefaultEntry.getIdDirectoryEntry( ) );
                }
                else
                {
                    resourceActions.put( MARK_USE_DIRECTORY_DEFAULT_ENTRY_DIRECTORY, -1 );
                }

                resourceActions.put( MARK_USE_DIRECTORY_ENTRY_FORM, formEntryConfiguration );
                resourceActions.put( MARK_USE_DIRECTORY_ENTRY_FORM_TYPE, entryType.getIdType( ) );
                entryConfigurationList.add( resourceActions );
            }
        }

        ResponseFilter filter = new ResponseFilter( );
        filter.setIdResource( form.getIdForm( ) );

        model.put( MARK_MAP_ENTRY_GEOLOCATION, formGeolocalisationEntriesMap );
        model.put( MARK_MAP_ENTRY_IMAGE, formEntriesImageConfig );
        model.put( MARK_LIST_DIRECTORY_MAP_PROVIDERS, MapProviderManager.getMapProvidersList( ) );

        int nCountFormSubmit = FormSubmitHome.getCountFormSubmit( filter, plugin );
        model.put( MARK_RECORD_FORM, nCountFormSubmit );
        model.put( MARK_CREATE_DIRECTORY, formEntryTypeWithSeveralDirectoryEntryType );

        if ( ( listDirectory != null ) && !listDirectory.isEmpty( ) )
        {
            if ( request.getParameter( PARAMETER_ID_DIRECTORY ) != null )
            {
                model.put( MARK_DIRECTORY_LIST_DEFAULT_ITEM, request.getParameter( PARAMETER_ID_DIRECTORY ) );
                model.put( MARK_USE_DIRECTORY_EXIST, true );
            }
            else
                if ( isMapped )
                {
                    model.put( MARK_DIRECTORY_LIST_DEFAULT_ITEM, formConfiguration.getIdDirectory( ) );
                    model.put( MARK_USE_DIRECTORY_EXIST, false );
                }
                else
                {
                    model.put( MARK_DIRECTORY_LIST_DEFAULT_ITEM, listDirectory.get( 0 ).getCode( ) );
                    model.put( MARK_USE_DIRECTORY_EXIST, false );
                }

            model.put( MARK_DIRECTORY_LIST, listDirectory );
        }

        model.put( MARK_ENTRY_CONFIGURATION_LIST, entryConfigurationList );
        model.put( MARK_DIRECTORY_LIST_IS_EMPTY, ( listDirectory != null ) ? listDirectory.isEmpty( ) : true );
        model.put( MARK_MAPPED, isMapped );
        model.put( MARK_FORM, form );
        model.put( MARK_LOCALE, locale );
        model.put( MARK_FORM_CONFIGURATION, formConfiguration );
        model.put( MARK_FORM_ENTRY_FILE, DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_FORM_ENTRY_TYPE_FILE ) ) );
        model.put( MARK_FORM_ENTRY_IMAGE,
                DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_FORM_ENTRY_TYPE_IMAGE, DEFAULT_FORM_IMAGE_TYPE ) ) );

        if ( WorkflowService.getInstance( ).isAvailable( )
                && ( WorkflowService.getInstance( ).getWorkflowsEnabled( AdminUserService.getAdminUser( request ), locale ) != null ) )
        {
            model.put( MARK_WORKFLOW, WorkflowService.getInstance( ).getWorkflowsEnabled( AdminUserService.getAdminUser( request ), locale ) );
        }

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CONFIGURATION_EXPORTDIRECTORY, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    public String doOutputConfigForm( HttpServletRequest request, Locale locale, Plugin plugin )
    {
        Plugin pluginExportdirectory = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        String strIdForm = request.getParameter( PARAMETER_ID_FORM );

        String strActionExportdatabase = request.getParameter( PARAMETER_SELECTED_ACTION_EXPORTDIRECTORY );

        if ( ( strIdForm == null ) || !strIdForm.matches( REGEX_ID ) )
        {
            return Messages.MANDATORY_FIELDS;
        }

        Form form = FormHome.findByPrimaryKey( Integer.parseInt( strIdForm ), plugin );

        if ( form == null )
        {
            return Messages.MANDATORY_FIELDS;
        }

        if ( request.getParameter( FormJspBean.PARAMETER_ACTION_REDIRECT ) != null )
        {
            return FormJspBean
                    .getJspManageOutputProcessForm( request, form.getIdForm( ), PARAMETER_ID_DIRECTORY, request.getParameter( PARAMETER_ID_DIRECTORY ) );
        }

        if ( ( strActionExportdatabase == null ) )
        {
            return null;
        }
        else
            if ( strActionExportdatabase.equals( ACTION_CREATE_DIRECTORY ) )
            {
                return doActionCreateDirectory( request, form, plugin, pluginExportdirectory );
            }
            else
                if ( strActionExportdatabase.equals( ACTION_USE_DIRECTORY_EXIST ) )
                {
                    return doActionUseDirectoryExist( request, form, plugin, pluginExportdirectory );
                }
                else
                    if ( strActionExportdatabase.equals( ACTION_DELETE_MAPPING_DIRECTORY ) )
                    {
                        return doActionRemoveMapping( request, form, pluginExportdirectory );
                    }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String process( FormSubmit formSubmit, HttpServletRequest request, Plugin plugin )
    {
        Plugin pluginExportdirectory = PluginService.getPlugin( ExportdirectoryPlugin.PLUGIN_NAME );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Plugin pluginForm = PluginService.getPlugin( FormPlugin.PLUGIN_NAME );
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( formSubmit.getForm( ).getIdForm( ), pluginExportdirectory );

        if ( ( formConfiguration != null ) && ( DirectoryHome.findByPrimaryKey( formConfiguration.getIdDirectory( ), pluginDirectory ) != null ) )
        {
            try
            {
                ExportDirectoryUtils.createDirectoryRecord( request, formConfiguration, formSubmit, pluginForm, pluginDirectory );
            }
            catch( UnsupportedEncodingException e )
            {
                return null;
            }
        }

        return null; // No error
    }

    /**
     * Process the create directorty
     * 
     * @param request
     *            The {@link HttpServletRequest}
     * @param form
     *            The {@link Form} linked to this outputProcessor
     * @param plugin
     *            The {@link Plugin}
     * @param pluginExportdirectory
     *            the plugin export directory
     * @return An error message key or null if no error
     */
    private String doActionCreateDirectory( HttpServletRequest request, Form form, Plugin plugin, Plugin pluginExportdirectory )
    {
        FormConfiguration formConfiguration = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ), pluginExportdirectory );

        if ( formConfiguration != null )
        {
            doActionRemoveMapping( request, form, pluginExportdirectory );
        }

        Plugin pluginForm = PluginService.getPlugin( FormPlugin.PLUGIN_NAME );
        Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
        Map<Integer, Integer> mapIdEntryIterationNumber = new LinkedHashMap<>( );

        for ( Entry entry : FormUtils.getAllQuestionList( form.getIdForm( ), plugin ) )
        {
            int nIdEntry = entry.getIdEntry( );
            boolean bBelongIterableGroup = EntryTypeGroupUtils.entryBelongIterableGroup( entry );
            if ( bBelongIterableGroup && !mapIdEntryIterationNumber.containsKey( nIdEntry ) )
            {
                mapIdEntryIterationNumber.put( nIdEntry, NumberUtils.INTEGER_ONE );
            }

            List<EntryType> listEntryTypeDirectory = ExportDirectoryUtils.getDirectoryEntryForFormEntry( entry.getEntryType( ) );

            if ( ExportDirectoryUtils.isGeolocationFormEntry( entry ) )
            {
                if ( StringUtils.isBlank( request.getParameter( ExportDirectoryUtils.PARAMETER_PREFIX_KEY_GEOLOCATION + nIdEntry ) ) )
                {
                    // If the entry belong to an iterable the name of the request parameter must be modified
                    if ( bBelongIterableGroup )
                    {
                        // Create the parameter name
                        String strParameterName = ExportDirectoryUtils.computeIterableEntryParameterName(
                                ExportDirectoryUtils.PARAMETER_PREFIX_KEY_GEOLOCATION, mapIdEntryIterationNumber.get( nIdEntry ), nIdEntry, Boolean.TRUE );

                        if ( StringUtils.isBlank( request.getParameter( strParameterName ) ) )
                        {
                            return MESSAGE_ERROR_ENTRY_GEOLOCATION_MISSING;
                        }
                    }
                    else
                    {
                        return MESSAGE_ERROR_ENTRY_GEOLOCATION_MISSING;
                    }
                }
            }

            if ( listEntryTypeDirectory.size( ) > 1 )
            {
                String strParameterName = PARAMETER_ID_ENTRY_TYPE + FormUtils.CONSTANT_UNDERSCORE + nIdEntry;
                Integer nIterationNumber = mapIdEntryIterationNumber.get( nIdEntry );
                if ( nIterationNumber != null )
                {
                    strParameterName = ExportDirectoryUtils.computeIterableEntryParameterName( strParameterName, nIterationNumber,
                            NumberUtils.INTEGER_MINUS_ONE, Boolean.FALSE );
                    mapIdEntryIterationNumber.put( nIdEntry, nIterationNumber );
                }

                if ( request.getParameter( strParameterName ) == null )
                {
                    return MESSAGE_ERROR_ENTRY_FORM_NOT_TYPE_DIRECTORY;
                }
            }
        }

        String error = null;
        error = ExportDirectoryUtils.createDirectoryByIdForm( form.getIdForm( ), request, PluginService.getPlugin( FormPlugin.PLUGIN_NAME ),
                PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME ) );

        if ( error != null )
        {
            return error;
        }

        if ( ( request.getParameter( PARAMETER_EXPORT_ALL ) != null )
                && ( DirectoryUtils.convertStringToInt( request.getParameter( PARAMETER_EXPORT_ALL ) ) == 1 ) )
        {
            ResponseFilter responseFilter = new ResponseFilter( );
            responseFilter.setIdResource( form.getIdForm( ) );

            for ( FormSubmit formSubmit : FormSubmitHome.getFormSubmitList( responseFilter, pluginForm ) )
            {
                ResponseFilter responseFilterFormSubmit = new ResponseFilter( );

                List<Integer> responseId = FormSubmitHome.getResponseListFromIdFormSubmit( formSubmit.getIdFormSubmit( ), plugin );
                responseFilterFormSubmit.setListId( responseId );

                IResponseService responseService = SpringContextService.getBean( FormUtils.BEAN_FORM_RESPONSE_SERVICE );
                formSubmit.setListResponse( responseService.getResponseList( responseFilterFormSubmit, false ) );

                try
                {
                    ExportDirectoryUtils.createDirectoryRecord( request, FormConfigurationHome.findByPrimaryKey( form.getIdForm( ), pluginExportdirectory ),
                            formSubmit, pluginForm, pluginDirectory );
                }
                catch( UnsupportedEncodingException e )
                {
                    return null;
                }
            }
        }

        return null;
    }

    /**
     * Process use a directory already exist
     * 
     * @param request
     *            The {@link HttpServletRequest}
     * @param form
     *            The {@link Form} linked to this outputProcessor
     * @param plugin
     *            The {@link Plugin}
     * @param pluginExportdirectory
     *            the plugin export directory
     * @return An error message key or null if no error
     */
    private String doActionUseDirectoryExist( HttpServletRequest request, Form form, Plugin plugin, Plugin pluginExportdirectory )
    {
        int nFormEntryTypeComment = DirectoryUtils.convertStringToInt( AppPropertiesService.getProperty( PROPERTY_FORM_ENTRY_TYPE_COMMENT ) );
        FormConfiguration formConfigurationInitial = FormConfigurationHome.findByPrimaryKey( form.getIdForm( ), pluginExportdirectory );

        if ( request.getParameter( PARAMETER_ID_DIRECTORY ) != null )
        {
            Plugin pluginDirectory = PluginService.getPlugin( DirectoryPlugin.PLUGIN_NAME );
            Plugin pluginForm = PluginService.getPlugin( FormPlugin.PLUGIN_NAME );
            List<EntryConfiguration> listEntryConfiguration = new ArrayList<EntryConfiguration>( );
            Map<Integer, Integer> mapIdEntryNumberIteration = new LinkedHashMap<>( );

            for ( Entry entry : FormUtils.getAllQuestionList( form.getIdForm( ), plugin ) )
            {
                if ( entry.getEntryType( ).getIdType( ) != nFormEntryTypeComment )
                {
                    // Construct the parameter name for the iteration entry
                    int nIterationNumber = FormConstants.DEFAULT_ITERATION_NUMBER;
                    int nIdEntry = entry.getIdEntry( );
                    String strParameterName = PARAMETER_ID_DIRECTORY_FOR_FORM_ENTRY + FormUtils.CONSTANT_UNDERSCORE + nIdEntry;
                    if ( EntryTypeGroupUtils.entryBelongIterableGroup( entry ) )
                    {
                        if ( !mapIdEntryNumberIteration.containsKey( nIdEntry ) )
                        {
                            mapIdEntryNumberIteration.put( nIdEntry, NumberUtils.INTEGER_ONE );
                        }

                        nIterationNumber = mapIdEntryNumberIteration.get( nIdEntry );
                        strParameterName = ExportDirectoryUtils.computeIterableEntryParameterName( strParameterName, nIterationNumber,
                                NumberUtils.INTEGER_MINUS_ONE, Boolean.FALSE );
                        mapIdEntryNumberIteration.put( nIdEntry, nIterationNumber + NumberUtils.INTEGER_ONE );
                    }

                    if ( ( request.getParameter( strParameterName ) == null ) || request.getParameter( strParameterName ).equals( "-1" ) )
                    {
                        return MESSAGE_ERROR_ENTRY_FORM_NOT_ID_DIRECTORY;
                    }
                    for ( EntryConfiguration entryConfiguration : listEntryConfiguration )
                    {
                        if ( entryConfiguration.getIdDirectoryEntry( ) == DirectoryUtils.convertStringToInt( request.getParameter( strParameterName ) ) )
                        {
                            return MESSAGE_ERROR_TWO_ENTRY_FORM_FOR_ONE_ID_DIRECTORY;
                        }
                    }

                    EntryConfiguration entryConfiguration = new EntryConfiguration( );
                    entryConfiguration.setIdForm( form.getIdForm( ) );
                    entryConfiguration.setIdFormEntry( entry.getIdEntry( ) );
                    entryConfiguration.setIterationNumber( nIterationNumber );
                    entryConfiguration.setIdDirectoryEntry( DirectoryUtils.convertStringToInt( request.getParameter( strParameterName ) ) );
                    listEntryConfiguration.add( entryConfiguration );
                }
            }

            if ( formConfigurationInitial != null )
            {
                doActionRemoveMapping( request, form, pluginExportdirectory );
            }

            FormConfiguration formConfiguration = new FormConfiguration( );
            formConfiguration.setIdDirectory( DirectoryUtils.convertStringToInt( request.getParameter( PARAMETER_ID_DIRECTORY ) ) );
            formConfiguration.setIdForm( form.getIdForm( ) );
            FormConfigurationHome.insert( formConfiguration, pluginExportdirectory );

            for ( EntryConfiguration entryConfiguration : listEntryConfiguration )
            {
                EntryConfigurationHome.insert( entryConfiguration, pluginExportdirectory );
            }

            if ( ( request.getParameter( PARAMETER_EXPORT_ALL ) != null )
                    && ( DirectoryUtils.convertStringToInt( request.getParameter( PARAMETER_EXPORT_ALL ) ) == 1 ) )
            {
                ResponseFilter responseFilter = new ResponseFilter( );
                responseFilter.setIdResource( form.getIdForm( ) );

                for ( FormSubmit formSubmit : FormSubmitHome.getFormSubmitList( responseFilter, pluginForm ) )
                {

                    ResponseFilter responseFilterFormSubmit = new ResponseFilter( );

                    List<Integer> responseId = FormSubmitHome.getResponseListFromIdFormSubmit( formSubmit.getIdFormSubmit( ), plugin );
                    responseFilterFormSubmit.setListId( responseId );

                    IResponseService responseService = SpringContextService.getBean( FormUtils.BEAN_FORM_RESPONSE_SERVICE );
                    formSubmit.setListResponse( responseService.getResponseList( responseFilterFormSubmit, false ) );

                    try
                    {
                        ExportDirectoryUtils.createDirectoryRecord( request,
                                FormConfigurationHome.findByPrimaryKey( form.getIdForm( ), pluginExportdirectory ), formSubmit, pluginForm, pluginDirectory );
                    }
                    catch( UnsupportedEncodingException e )
                    {
                        return null;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Process deletion of export directory
     * 
     * @param request
     *            The {@link HttpServletRequest}
     * @param form
     *            The {@link Form} linked to this outputProcessor
     * @param pluginExportdirectory
     *            the plugin export directory
     * @return An error message key or null if no error
     */
    private String doActionRemoveMapping( HttpServletRequest request, Form form, Plugin pluginExportdirectory )
    {
        FormConfigurationHome.delete( form.getIdForm( ), pluginExportdirectory );
        EntryConfigurationHome.deleteByForm( form.getIdForm( ), pluginExportdirectory );

        return null;
    }
}
