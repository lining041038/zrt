package antelope.wcm.assets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import antelope.entities.SysFile;
import antelope.utils.ClasspathResourceUtil;
import antelope.wcm.entities.WCMAssetItem;

@Component
public class ImgAsset extends BaseAsset {

	@Override
	public String getAssetType() {
		return "0";
	}

	@Override
	public String getAssetTypeName() {
		return "图片";
	}

	@Override
	public String getAssetRelativePath() {
		return "imgasset";
	}

	@Override
	public String extractAssetData(WCMAssetItem assetItem, String sitesid) throws IOException {
		File folder = ClasspathResourceUtil.getWebappFolderFile("/wcm/published/" + sitesid);
		FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + "/assets/" + assetItem.sid + ".png");
		fos.write(assetItem.assetdata);
		fos.close();
		return assetItem.sid;
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
		List<SysFile> file = dao.getBy("filegroupsid", assetdatasymbol, SysFile.class);
		assetItem.assetdata = file.get(0).filedata;
	}

}



