package antelope.wcm.assets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import antelope.springmvc.BaseComponent;
import antelope.wcm.entities.WCMAssetItem;

/**
 * 基础资源类，不同的资源类型有不同的添加配置以及显示的行为
 * @author pc
 * @since 2013-3-19
 */
public abstract class BaseAsset extends BaseComponent {
	
	/**
	 * 获取资源类型
	 * @return
	 */
	public abstract String getAssetType();
	
	/**
	 * 是否需要在资源管理中添加该资源，一般用于当直接使用外部资源时使用。
	 * @return
	 */
	public boolean needToAdd() {
		return true;
	}
	
	/**
	 * 获取资源类型名称
	 * @return
	 */
	public abstract String getAssetTypeName();
	
	/**
	 * 资源添加时，不同类型的资源有不同类型的资源添加界面，宽高不尽相同
	 * @return
	 */
	public abstract int getAddPageWidth();
	
	/**
	 * 资源添加时，不同类型的资源有不同类型的资源添加界面，宽高不尽相同
	 * @return
	 */
	public abstract int getAddPageHeight();
	
	
	/**
	 * 给模板页面配置资源时，不同类型的资源有不同类型的资源配置界面，宽高不尽相同
	 * @return
	 */
	public abstract int getConfigPageWidth();
	
	/**
	 * 给模板页面配置资源时，不同类型的资源有不同类型的资源配置界面，宽高不尽相同
	 * @return
	 */
	public abstract int getConfigPageHeight();

	/**
	 * 给出相对于/webapp/wcm/assets的资源html布局及js代码路径，用以初始化资源
	 * 注意还有一个根据此路径生成的资源配置(选择添加)界面jsp路径 为此路径加 _config.jsp形成
	 * 使用时仅回传不含后缀名的名称，比如 imgasset.html 返回 imgasset
	 * @return
	 */
	public abstract String getAssetRelativePath();
	
	/**
	 * 将存储在数据库中的资源提取到资源文件当中，并返回一个html字符串待插入到资源html布局代码当中
	 * @throws FileNotFoundException 
	 */
	public abstract String extractAssetData(WCMAssetItem assetItem, String sitesid) throws FileNotFoundException, IOException, SQLException, Exception;
	
	/**
	 * 当点击保存时，处理资源保存结构,注意此位置不需要调用持久化操作命令，如insertOrUpdate等
	 * @param req
	 * @param res
	 */
	public abstract void saveAsset(WCMAssetItem assetItem, String assetdatasymbol, HttpServletRequest req, HttpServletResponse res) throws Exception;
	
}




