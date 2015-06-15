package antelope.services.supportclasses;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.servlet.ServletOutputStream;
import javax.xml.namespace.QName;

import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.ZipPackagePart;
import org.apache.poi.openxml4j.util.ZipInputStreamZipEntrySource.FakeZipEntry;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.impl.CTNonVisualDrawingPropsImpl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHdrFtr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPicture;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTbl;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

import antelope.db.DBUtil;
import antelope.services.FileUploadService;
import antelope.springmvc.SpringUtils;
import antelope.utils.JSONException;
import antelope.utils.JSONObject;
import antelope.utils.RegExpUtil;


public class WordTmpl {

	private XWPFDocument xword;
	
	private OutputStream out;
	
	private static Pattern ptSqlatoms = Pattern.compile("(?<=\\$\\{)[^\\}]*(?=\\})");
	
	public WordTmpl(String tmplname, String filename, ServletOutputStream out) throws IOException, BiffException {
		String filePath = this.getClass().getResource("/wordtmpls/" + tmplname + ".docx").getFile();
		
		this.out = out;
		// linux下不要去除最前面的斜线
		if (filePath.indexOf(":") != -1)
			filePath = filePath.replaceFirst("^/*", "");
		
		FileInputStream file = new FileInputStream(filePath);
		xword = new XWPFDocument(file);
		

		
		file.close();
	}
	
	public void wrapData(JSONObject obj) throws JSONException {
		if (xword != null) {
			
			List<XWPFHeader> headerList = xword.getHeaderList();
			
			for (XWPFHeader xwpfHeader : headerList) {
				List<XWPFTable> tables = xwpfHeader.getTables();
				for (XWPFTable xwpfTable : tables) {
					List<XWPFTableRow> rows = xwpfTable.getRows();
					for (XWPFTableRow xwpfTableRow : rows) {
						List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
						for (XWPFTableCell xwpfTableCell : tableCells) {
							List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
							for (XWPFParagraph xwpfParagraph : paragraphs) {
								
								String finalstringValue = "";
								String p = xwpfParagraph.getParagraphText();
								List<XWPFRun> runs = xwpfParagraph.getRuns();
								for (XWPFRun xwpfRun : runs) {
									CTR ctr = xwpfRun.getCTR();
									List<CTText> tList = ctr.getTList();
									if (!tList.isEmpty()) {
										String stringValue = tList.get(0).getStringValue();
										finalstringValue += stringValue;
										Matcher sqlatoms = ptSqlatoms.matcher(stringValue);
										while (sqlatoms.find()) {
											String group = sqlatoms.group();
											if (obj.has(group)) {
												stringValue = stringValue.replaceAll("\\$\\{" + group + "\\}", obj.getString(group));
											}
										}
										//tList.get(0).setStringValue(stringValue);
									}
								}
								
								if (runs.size() == 0)
									continue;
								
								CTR ctr = runs.get(0).getCTR();
								List<CTText> tList = ctr.getTList();
								
								if (finalstringValue.indexOf("$") == -1)  {
									continue;
								}
								
								Matcher sqlatoms = ptSqlatoms.matcher(finalstringValue);
								while (sqlatoms.find()) {
									String group = sqlatoms.group();
									if (obj.has(group)) {
										finalstringValue = finalstringValue.replaceAll("\\$\\{" + group + "\\}", obj.getString(group));
									} else {
										finalstringValue = finalstringValue.replaceAll("\\$\\{" + group + "\\}", "");
									}
								}
								
								if (finalstringValue.indexOf("\r\n") != -1) {
									String[] split = finalstringValue.split("\r\n");
									for (String string : split) {
										if (string.startsWith(" ")) {
											String firstMatched = RegExpUtil.getFirstMatched("\\s*", string);
											for (int i = 0; i < firstMatched.length(); ++i) {
												CTText addNewInstrText = ctr.addNewInstrText();
												addNewInstrText.setStringValue("         " + string);
											}
										}
										runs.get(0).getCTR().addNewTab();
										ctr.addNewBr();
									}
									
									if (tList.size() > 0)
										tList.get(0).setStringValue("");
								} else {
									if (tList.size() > 0)
										tList.get(0).setStringValue(finalstringValue);
									else
										ctr.addNewInstrText().setStringValue(finalstringValue);
										
								}
								
								while(runs.size() > 1) {
									xwpfParagraph.removeRun(1);
								}
							}
						}
					}
				}
			}
			
			
			List<XWPFTable> tables = xword.getTables();
			for (XWPFTable xwpfTable : tables) {
				List<XWPFTableRow> rows = xwpfTable.getRows();
				for (XWPFTableRow xwpfTableRow : rows) {
					List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
					for (XWPFTableCell xwpfTableCell : tableCells) {
						List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
						for (XWPFParagraph xwpfParagraph : paragraphs) {
							String p = xwpfParagraph.getParagraphText();
							
							
							List<XWPFRun> runs = xwpfParagraph.getRuns();
							
							String finalstringValue = "";
							
							for (int i = 0; i < runs.size(); ++i) {
								XWPFRun xwpfRun = runs.get(i);
								CTR ctr = xwpfRun.getCTR();
								List<CTText> tList = ctr.getTList();
								boolean runend = false;
								if (xwpfRun.getEmbeddedPictures().size() != 0) {
									List<XWPFPicture> embeddedPictures = xwpfRun.getEmbeddedPictures();
									
									XWPFPictureData pictureData = embeddedPictures.get(0).getPictureData();
									
									CTR ctr2 = xwpfRun.getCTR();
									List<CTDrawing> drawingList = ctr2.getDrawingList();
									CTNonVisualDrawingPropsImpl docPr = (CTNonVisualDrawingPropsImpl) drawingList.get(0).getAnchorList().get(0).getDocPr();
									
									
									Matcher sqlatoms = ptSqlatoms.matcher(docPr.toString());
													
									while (sqlatoms.find()) {
										String group = sqlatoms.group();
										if (obj.has(group)) {
											String string = obj.getString(group);
											FileUploadService bean = SpringUtils.getBean(FileUploadService.class);
											try {
												
												byte[] filedata = new byte[0];
												List<String> filesidsByGroupSid = bean.getFilesidsByGroupSid(string);
												if (filesidsByGroupSid.size() > 0)
													filedata = bean.getFileDataBySid(filesidsByGroupSid.get(0));
												
												if (filedata.length > 0) {
													ZipPackagePart packagePart = (ZipPackagePart)pictureData.getPackagePart();
													
													FakeZipEntry zipArchive = (FakeZipEntry) packagePart.getZipArchive();
													
													Field declaredField = FakeZipEntry.class.getDeclaredField("data");
													
													declaredField.setAccessible(true);
													declaredField.set(zipArchive, filedata);
												} else {
													xwpfParagraph.removeRun(i);
													runend = true;
													break;
												}
												
												//OutputStream outputStream = packagePart.getOutputStream();
												//outputStream.write(data);
												//outputStream.flush();
												//pictureData.getPackagePart().getOutputStream().write(filedata);
												//outputStream.flush();
											} catch (IOException e) {
												e.printStackTrace();
											} catch (Exception e) {
												e.printStackTrace();
											}
										} else {
											xwpfParagraph.removeRun(i);
											runend = true;
											break;
										}
									}
								}
								
								if (runend) {
									i--;
									continue;
								}
								
								if (!tList.isEmpty()) {
									String stringValue = tList.get(0).getStringValue();
									
									finalstringValue += stringValue;
								}
							}
							
							
							if (runs.size() == 0)
								continue;
							
							CTR ctr = runs.get(0).getCTR();
							
							List<CTText> tList = ctr.getTList();
							
							Matcher sqlatoms = ptSqlatoms.matcher(finalstringValue);
							while (sqlatoms.find()) {
								String group = sqlatoms.group();
								if (obj.has(group)) {
									finalstringValue = finalstringValue.replaceAll("\\$\\{" + group + "\\}", obj.getString(group));
								} else {
									finalstringValue = finalstringValue.replaceAll("\\$\\{" + group + "\\}", "");
								}
							}
							
							if (finalstringValue.indexOf("\r\n") != -1) {
								String[] split = finalstringValue.split("\r\n");
								for (String string : split) {
									if (string.startsWith(" ")) {
										String firstMatched = RegExpUtil.getFirstMatched("\\s*", string);
										for (int i = 0; i < firstMatched.length(); ++i) {
											ctr.addNewTab();
										}
									}
									CTText addNewInstrText = ctr.addNewInstrText();
									addNewInstrText.setStringValue(string);
									runs.get(0).getCTR().addNewTab();
									ctr.addNewBr();
								}
								
								if (tList.size() > 0)
									tList.get(0).setStringValue("");
							} else {
								if (tList.size() > 0) {
									tList.get(0).setStringValue(finalstringValue);
								} else {
									ctr.addNewInstrText().setStringValue(finalstringValue);
								}
									
							}
							
							while(runs.size() > 1) {
								xwpfParagraph.removeRun(1);
							}
							
						}
					}
				}
			}
			
			
			List<XWPFParagraph> paragraphs = xword.getParagraphs();
			for (XWPFParagraph xwpfParagraph : paragraphs) {
				String p = xwpfParagraph.getParagraphText();
				
				String finalstringValue = "";
				
				List<XWPFRun> runs = xwpfParagraph.getRuns();
				for (XWPFRun xwpfRun : runs) {
					CTR ctr = xwpfRun.getCTR();
					List<CTText> tList = ctr.getTList();
					if (!tList.isEmpty()) {
						String stringValue = tList.get(0).getStringValue();
						finalstringValue += stringValue; 
					}
				}
				
				if (runs.size() == 0)
					continue;
				
				CTR ctr = runs.get(0).getCTR();
				
				List<CTText> tList = ctr.getTList();
				
				Matcher sqlatoms = ptSqlatoms.matcher(finalstringValue);
				while (sqlatoms.find()) {
					String group = sqlatoms.group();
					if (obj.has(group)) {
						finalstringValue = finalstringValue.replaceAll("\\$\\{" + group + "\\}", obj.getString(group));
					} else {
						finalstringValue = finalstringValue.replaceAll("\\$\\{" + group + "\\}", "");
					}
				}
				
				if (finalstringValue.indexOf("\r\n") != -1) {
					String[] split = finalstringValue.split("\r\n");
					for (String string : split) {
						if (string.startsWith(" ")) {
							String firstMatched = RegExpUtil.getFirstMatched("\\s*", string);
							for (int i = 0; i < firstMatched.length(); ++i) {
								ctr.addNewTab();
							}
						}
						CTText addNewInstrText = ctr.addNewInstrText();
						addNewInstrText.setStringValue(string);
						runs.get(0).getCTR().addNewTab();
						ctr.addNewBr();
					}
					
					if (tList.size() > 0)
						tList.get(0).setStringValue("");
				} else {
					if (tList.size() > 0) {
						tList.get(0).setStringValue(finalstringValue);
					} else {
						ctr.addNewInstrText().setStringValue(finalstringValue);
					}
						
				}
				
				while(runs.size() > 1) {
					xwpfParagraph.removeRun(1);
				}
				
			}
			return;
		}
	}
	
	/**
	 * 清空未替换参数(暂不支持)
	 * @throws RowsExceededException
	 * @throws WriteException
	 */
	private void removeAllParams() throws RowsExceededException, WriteException {
		if (xword != null) {
			
			
			List<XWPFHeader> headerList = xword.getHeaderList();
			
			for (XWPFHeader xwpfHeader : headerList) {
				List<XWPFTable> tables = xwpfHeader.getTables();
				for (XWPFTable xwpfTable : tables) {
					List<XWPFTableRow> rows = xwpfTable.getRows();
					for (XWPFTableRow xwpfTableRow : rows) {
						List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
						for (XWPFTableCell xwpfTableCell : tableCells) {
							List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
							for (XWPFParagraph xwpfParagraph : paragraphs) {
								String p = xwpfParagraph.getParagraphText();
								List<XWPFRun> runs = xwpfParagraph.getRuns();
								for (XWPFRun xwpfRun : runs) {
									CTR ctr = xwpfRun.getCTR();
									List<CTText> tList = ctr.getTList();
									if (!tList.isEmpty()) {
										
										for (CTText ctText : tList) {
											String stringValue = ctText.getStringValue();
											Matcher sqlatoms = ptSqlatoms.matcher(stringValue);
											while (sqlatoms.find()) {
												String group = sqlatoms.group();
												stringValue = stringValue.replaceAll("\\$\\{" + group + "\\}", "");
											}
											
											ctText.setStringValue(stringValue);	
										}
										
									}
								}
							}
						}
					}
				}
			}
			
			
			List<XWPFTable> tables = xword.getTables();
			for (XWPFTable xwpfTable : tables) {
				List<XWPFTableRow> rows = xwpfTable.getRows();
				for (XWPFTableRow xwpfTableRow : rows) {
					List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
					for (XWPFTableCell xwpfTableCell : tableCells) {
						List<XWPFParagraph> paragraphs = xwpfTableCell.getParagraphs();
						
						for (int j = 0; j < paragraphs.size();) {
							XWPFParagraph xwpfParagraph = paragraphs.get(j); 
							
							boolean removedparagraph = false;
							String p = xwpfParagraph.getParagraphText();
							List<XWPFRun> runs = xwpfParagraph.getRuns();
							
							for (int i = 0; i < runs.size(); ++i) {
								CTR ctr = runs.get(i).getCTR();
								List<CTText> tList = ctr.getTList();
								
								for (CTText ctText : tList) {
									String stringValue = ctText.getStringValue();
									Matcher sqlatoms = ptSqlatoms.matcher(stringValue);
									while (sqlatoms.find()) {
										String group = sqlatoms.group();
										stringValue = stringValue.replaceAll("\\$\\{" + group + "\\}", "");
									}
									ctText.setStringValue(stringValue);	
								}
							}
							
							if (!removedparagraph)
								++j;
						}
					}
				}
			}
			
			
			List<XWPFParagraph> paragraphs = xword.getParagraphs();
			for (XWPFParagraph xwpfParagraph : paragraphs) {
				String p = xwpfParagraph.getParagraphText();
				List<XWPFRun> runs = xwpfParagraph.getRuns();
				for (XWPFRun xwpfRun : runs) {
					CTR ctr = xwpfRun.getCTR();
					List<CTText> tList = ctr.getTList();
					if (!tList.isEmpty()) {
						for (CTText ctText : tList) {
							String stringValue = ctText.getStringValue();
							Matcher sqlatoms = ptSqlatoms.matcher(stringValue);
							while (sqlatoms.find()) {
								String group = sqlatoms.group();
								stringValue = stringValue.replaceAll("\\$\\{" + group + "\\}", "");
							}
							
							ctText.setStringValue(stringValue);	
						}
					}
				}
			}
			
			return;
		}
	}
	
	public void closeWord() {
		if (xword != null) {
			try {
				removeAllParams();
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			try {
				xword.write(out);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
	}
}
