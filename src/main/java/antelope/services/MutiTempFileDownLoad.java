package antelope.services;

import java.io.File;

/**
 * 在接口中填充多个文件
 * @author huanggc
 * @since 2012-3-25
 */
public interface MutiTempFileDownLoad {
	
	void  genFiles(File zipTemp) throws Exception;

}
