package antelope.wcm.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import antelope.db.DBUtil;
import antelope.interfaces.components.TileSelect;
import antelope.interfaces.components.supportclasses.TileSelectOptions;
import antelope.utils.JSONObject;
import antelope.utils.PageItem;

@Component("tileselectasset")
public class TileSelectAsset extends TileSelect {

	@Override
	public TileSelectOptions getOptions(HttpServletRequest req) {
		TileSelectOptions opts = new TileSelectOptions();
		opts.title = "资源选择";
		return opts;
	}

	@Override
	public PageItem getJSONPage(String queryval, HttpServletRequest req)
			throws Exception {
		PageItem assets = DBUtil.queryJSON("select sid, name, assettype from WCM_ASSET where assettype='0'", new Object[0], getPageParams(req));
		List<JSONObject> list = assets.getCurrList();
		for (JSONObject obj : list) {
			obj.put("imgpath", req.getContextPath() + "/wcm/management/assetsmanage/AssetsManageController/getImageData.vot?sid=" + obj.getString("sid"));
		}
		return assets;
	}

}
