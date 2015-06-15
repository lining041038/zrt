package antelope.wcm.assets;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import antelope.wcm.entities.WCMAssetItem;

/**
 * 文章列表资源类
 * @author lining
 * @since 2013-4-2
 */
@Component
public class ArticleListAsset extends BaseAsset {

	@Override
	public String getAssetType() {
		return "4";
	}

	@Override
	public String getAssetTypeName() {
		return "文章列表";
	}

	@Override
	public String getAssetRelativePath() {
		return "articlelist";
	}

	@Override
	public String extractAssetData(WCMAssetItem assetItem, String sitesid) throws SQLException, Exception {
		return "";
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

	@Override
	public boolean needToAdd() {
		return false;
	}

}
