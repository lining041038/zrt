package antelope.system;

// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FCExporter.java


import antelope.utils.ClasspathResourceUtil;

import com.fusioncharts.exporter.FusionChartsExportHelper;
import com.fusioncharts.exporter.beans.*;
import com.fusioncharts.exporter.error.ErrorHandler;
import com.fusioncharts.exporter.error.LOGMESSAGE;
import com.fusioncharts.exporter.resources.FCExporter_Format;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

public class FCExporter extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private boolean SAVEFOLDEREXISTS;
	private static Logger logger = null;

	public FCExporter()
	{
		SAVEFOLDEREXISTS = true;
	}

	private boolean checkExportResources()
	{
		boolean allExportResourcesFound = true;
		for (Iterator iterator = FusionChartsExportHelper.getHandlerAssociationsMap().values().iterator(); iterator.hasNext();)
		{
			String exportFormat = (String)iterator.next();
			String exporterClassName = FusionChartsExportHelper.getExportHandlerClassName(exportFormat);
			try
			{
				Class exporterClass = Class.forName(exporterClassName);
				FCExporter_Format fcExporter = (FCExporter_Format)exporterClass.newInstance();
			}
			catch (ClassNotFoundException e)
			{
				logger.log(Level.SEVERE, (new StringBuilder(String.valueOf(LOGMESSAGE.E404.toString()))).append(":").append(exporterClassName).toString());
				allExportResourcesFound = false;
			}
			catch (InstantiationException e)
			{
				logger.log(Level.SEVERE, (new StringBuilder(String.valueOf(LOGMESSAGE.E404.toString()))).append(":").append(exporterClassName).toString());
				allExportResourcesFound = false;
			}
			catch (IllegalAccessException e)
			{
				logger.log(Level.SEVERE, (new StringBuilder(String.valueOf(LOGMESSAGE.E404.toString()))).append(":").append(exporterClassName).toString());
				allExportResourcesFound = false;
			}
		}

		return allExportResourcesFound;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		FusionChartsExportData exportData = new FusionChartsExportData(request.getParameter("stream"), request.getParameter("parameters"), request.getParameter("meta_width"), request.getParameter("meta_height"), request.getParameter("meta_DOMId"), request.getParameter("meta_bgColor"));
		ExportBean exportBean = FusionChartsExportHelper.parseExportRequestStream(exportData);
		String exportTargetWindow = (String)exportBean.getExportParameterValue(ExportParameterNames.EXPORTTARGETWINDOW.toString());
		String exportAction = (String)exportBean.getExportParameterValue(ExportParameterNames.EXPORTACTION.toString());
		String exportFormat = (String)exportBean.getExportParameterValue(ExportParameterNames.EXPORTFORMAT.toString());
		LogMessageSetVO logMessageSetVO = exportBean.validate();
		Set errorsSet = logMessageSetVO.getErrorsSet();
		boolean isHTML = exportBean.isHTMLResponse();
		if (errorsSet != null && !errorsSet.isEmpty())
		{
			String meta_values = exportBean.getMetadataAsQueryString(null, true, isHTML);
			logMessageSetVO.setOtherMessages(meta_values);
			writeError(response, isHTML, logMessageSetVO, exportTargetWindow);
			return;
		}
		if (!exportAction.equals("download"))
		{
			if (!SAVEFOLDEREXISTS)
			{
				logMessageSetVO.addError(LOGMESSAGE.E508);
				String meta_values = exportBean.getMetadataAsQueryString(null, true, isHTML);
				logMessageSetVO.setOtherMessages(meta_values);
				writeError(response, isHTML, logMessageSetVO, exportTargetWindow);
				return;
			}
			String fileNameWithoutExt = (String)exportBean.getExportParameterValue(ExportParameterNames.EXPORTFILENAME.toString());
			String extension = FusionChartsExportHelper.getExtensionFor(exportFormat.toLowerCase());
			String fileName = (new StringBuilder(String.valueOf(fileNameWithoutExt))).append(".").append(extension).toString();
			logMessageSetVO = ErrorHandler.checkServerSaveStatus(fileName);
			errorsSet = logMessageSetVO.getErrorsSet();
		}
		if (errorsSet != null && !errorsSet.isEmpty())
		{
			String meta_values = exportBean.getMetadataAsQueryString(null, true, isHTML);
			logMessageSetVO.setOtherMessages(meta_values);
			writeError(response, isHTML, logMessageSetVO, exportTargetWindow);
			return;
		}
		String exporterClassName = FusionChartsExportHelper.getExportHandlerClassName(exportFormat);
		try
		{
			Class exporterClass = Class.forName(exporterClassName);
			FCExporter_Format fcExporter = (FCExporter_Format)exporterClass.newInstance();
			Object exportObject = fcExporter.exportProcessor(exportBean);
			String status = fcExporter.exportOutput(exportObject, response);
		}
		catch (ClassNotFoundException e)
		{
			logMessageSetVO.addError(LOGMESSAGE.E404);
			String meta_values = exportBean.getMetadataAsQueryString(null, true, isHTML);
			logMessageSetVO.setOtherMessages(meta_values);
			writeError(response, isHTML, logMessageSetVO, exportTargetWindow);
		}
		catch (InstantiationException e)
		{
			logMessageSetVO.addError(LOGMESSAGE.E404);
			String meta_values = exportBean.getMetadataAsQueryString(null, true, isHTML);
			logMessageSetVO.setOtherMessages(meta_values);
			writeError(response, isHTML, logMessageSetVO, exportTargetWindow);
		}
		catch (IllegalAccessException e)
		{
			logMessageSetVO.addError(LOGMESSAGE.E404);
			String meta_values = exportBean.getMetadataAsQueryString(null, true, isHTML);
			logMessageSetVO.setOtherMessages(meta_values);
			writeError(response, isHTML, logMessageSetVO, exportTargetWindow);
		}
	}

	public void init(ServletConfig config)
		throws ServletException
	{
		logger.info("FCExporter Servlet Init called");
		ExportConfiguration.loadProperties();
		File f = new File(ExportConfiguration.SAVEPATH);
		boolean savePathAbsolute = f.isAbsolute();
		logger.info((new StringBuilder("Is SAVEPATH on server absolute?")).append(savePathAbsolute).toString());
		ExportConfiguration.SAVEABSOLUTEPATH = savePathAbsolute ? ExportConfiguration.SAVEPATH : 
			ClasspathResourceUtil.getWebappFolderFile("\\" + ExportConfiguration.SAVEPATH).getAbsolutePath();
		SAVEFOLDEREXISTS = ErrorHandler.doesServerSaveFolderExist();
		if (!SAVEFOLDEREXISTS)
			logger.warning((new StringBuilder(String.valueOf(LOGMESSAGE.E508.toString()))).append("Path used: ").append(ExportConfiguration.SAVEABSOLUTEPATH).toString());
		checkExportResources();
	}

	private void writeError(HttpServletResponse response, boolean isHTML, LogMessageSetVO logMessageSetVO, String exportTargetWindow)
	{
		response.setContentType("text/html");
		if (exportTargetWindow != null && exportTargetWindow.equalsIgnoreCase("_self"))
			response.addHeader("Content-Disposition", "attachment;");
		else
			response.addHeader("Content-Disposition", "inline;");
		try
		{
			PrintWriter out = response.getWriter();
			out.print(ErrorHandler.buildResponse(logMessageSetVO, isHTML));
		}
		catch (IOException ioexception) { }
	}

	static 
	{
		logger = Logger.getLogger(FCExporter.class.getName());
	}
}
