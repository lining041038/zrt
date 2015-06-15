package antelope.example;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.springmvc.BaseController;

@Controller
public class ChangeUserController extends BaseController{
	
	@RequestMapping("/example/changeuser")
	public void changeUser(String username, HttpServletRequest req) {
		req.getSession().setAttribute("user", username);
	}
	
	@RequestMapping("/example/getuser")
	public void getUser(HttpServletRequest req, HttpServletResponse res) throws IOException {
		String username = (String) req.getSession().getAttribute("user");
		if (username == null)
			getOut(res).print("admin");
		else
			getOut(res).print(username);
		System.out.println(req.getSession().getAttribute("user"));
	}
}
