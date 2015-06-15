package antelope.services;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import antelope.db.DBUtil;
import antelope.db.DBUtil.DataType;
import antelope.entities.SysFile;
import antelope.springmvc.BaseComponent;
import antelope.utils.SpeedIDUtil;

/**
 * 文件上传组件服务类
 * @author lining
 */
@Service
public class FileUploadService extends BaseComponent{
	/**
	 * 根据文件组sid删除系统文件
	 * @param filegroupsid
	 * @return
	 */
	public int deleteByFileGroupSid(String filegroupsid) {
		return dao.updateBySQL("delete from SYS_FILES where filegroupsid=?", filegroupsid);
	}
	
	/**
	 * 将上传上去的文件持久化
	 * @param filegroupsid
	 * @return
	 */
	public int setPermanent(List<FileUploadParams> params) {
		int affectfiles = 0;
		for (FileUploadParams fileUploadParams : params) {
			affectfiles += dao.updateBySQL("update SYS_FILES set ispermanent=1 where filegroupsid=? and uploadtimeid=?", new Object[]{fileUploadParams.filegroupsid, fileUploadParams.uploadtimeid});	
			affectfiles += dao.updateBySQL("delete from SYS_FILES where willdelete='1' and filegroupsid=? and uploadtimeid=?", new Object[]{fileUploadParams.filegroupsid, fileUploadParams.uploadtimeid});
		}
		return affectfiles;
	}
	
	/**
	 * 获取系统所存储的文件相关数据
	 * @param sid
	 */
	public byte[] getFileDataBySid(String sid) throws Exception {
		SysFile file = dao.getBy(sid, SysFile.class);
		return file.filedata;
	}
	
	/**
	 * 对文件数据进行备份，并返回备份后的新filegroupsid
	 * @param filegroupsid 需要进行备份的文件组sid
	 * @return 备份后的新文件组sid
	 * @throws Exception 
	 */
	public String duplicateGroupFiles(String filegroupsid) throws Exception {
		
		List<String> filesids = getFilesidsByGroupSid(filegroupsid);
		
		List<Object[]> paramlists = new ArrayList<Object[]>();
		String newfilegroupsid = SpeedIDUtil.getId();
		for (int i = 0; i < filesids.size(); ++i) {
			paramlists.add(new Object[]{SpeedIDUtil.getId(), newfilegroupsid, filesids.get(i)});
		}
		DBUtil.batchUpdateWithHighSpeed("insert into SYS_FILES(sid, filegroupsid,filename," +
				"filesize,filedata,uploadtimeid,willdelete,filedesc) select ?, ?,filename,filesize,filedata,uploadtimeid,willdelete,filedesc" +
				" from SYS_FILES where sid=?", paramlists);
		return newfilegroupsid;
	}
	
	/**
	 * 根据文件组sid获取所有的文件信息，仅获取已持久化的文件
	 * @param filegroupsid
	 */
	public List<String> getFilesidsByGroupSid(String filegroupsid) throws SQLException, Exception {
		List<Object> vallist = DBUtil.querySingleValList("select sid from SYS_FILES where filegroupsid=? and ispermanent='1'" , filegroupsid, DataType.String);
		
		List<String> valliststrs = new ArrayList<String>();
		for (Object object : vallist) {
			valliststrs.add(object.toString());
		}
		return valliststrs;
	}
}



