package antelope.wcm.assets.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;
import antelope.wcm.entities.WCMAssetItem;

@Controller
public class ImgAssetController extends BaseController {
	
	@RequestMapping("/assets/imgasset/ImgAssetController/getImageData")
	public void getImageData(String sid, HttpServletRequest req, HttpServletResponse res) throws Exception {
		WCMAssetItem item = dao.getBy(sid, WCMAssetItem.class);
		printBytes(item.assetdata, res);
	}
}
