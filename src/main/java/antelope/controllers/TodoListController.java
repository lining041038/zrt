package antelope.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import antelope.beans.TodoListItem;
import antelope.interfaces.TodoListDataProvider;
import antelope.springmvc.BaseController;
import antelope.utils.JSONArray;
import antelope.utils.JSONObject;

/**
 * 全局待办事项访问控制器
 * @author lining
 */
@Controller
public class TodoListController extends BaseController {
	
	/**
	 * 获取全局待办事项列表
	 * @param req
	 */
	@RequestMapping("/common/getLogonUserAllTodoList")
	public void getLogonUserAllTodoList(HttpServletRequest req, HttpServletResponse res) throws Exception {
		List<TodoListDataProvider> dataProviders = spring.getBeans(TodoListDataProvider.class);
		JSONArray arr = new JSONArray();
		for (TodoListDataProvider todoListDataProvider : dataProviders) {
			try {
              List<TodoListItem> todolistitems = todoListDataProvider.getTodoListItems(getService(req));
              for (TodoListItem todoListItem : todolistitems) {
              	arr.put(new JSONObject(todoListItem));
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
		}
		getOut(res).print(arr);
	}
}
