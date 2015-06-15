package antelope.wcm.assets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Component;

import antelope.db.DBUtil;
import antelope.entities.SysFile;
import antelope.utils.ClasspathResourceUtil;
import antelope.wcm.entities.WCMAssetItem;

@Component
public class HyperLinkAsset extends BaseAsset {

	@Override
	public String getAssetType() {
		return "1";
	}

	@Override
	public String getAssetTypeName() {
		return "超链接";
	}

	@Override
	public String getAssetRelativePath() {
		return "hyperlink";
	}

	@Override
	public String extractAssetData(WCMAssetItem assetItem, String sitesid) throws SQLException, Exception {
		String pageName = DBUtil.querySingleString("select name from WCM_PAGE where sid=?", new Object[]{new String (assetItem.assetdata, "utf-8")});
		JSONObject obj = new JSONObject();
		obj.put("pagename", pageName);
		obj.put("hyperlink", new String (assetItem.assetdata, "utf-8"));
		return obj.toString();
	}

	@Override
	public int getAddPageWidth() {
		return 500;
	}

	@Override
	public int getAddPageHeight() {
		return 500;
	}

	@Override
	public int getConfigPageWidth() {
		return 300;
	}

	@Override
	public int getConfigPageHeight() {
		return 500;
	}

	@Override
	public void saveAsset(WCMAssetItem assetItem, String assetdatasymbol, HttpServletRequest req, HttpServletResponse res) throws Exception {
		assetItem.assetdata = assetdatasymbol.getBytes("utf-8");
	}

}



