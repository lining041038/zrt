package antelope.interfaces;

import java.util.List;

import antelope.beans.TodoListItem;
import antelope.services.SessionService;

/**
 * 全局待办事项接口
 * @author lining
 */
public interface TodoListDataProvider {
	public List<TodoListItem> getTodoListItems(SessionService service) throws Exception;
}
