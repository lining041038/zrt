package antelope.james;

/**
 *
 *
 * @time 2012-10-29
 * @author dai jun
 *
 */
public interface JamesMgtInterface {

	/**
	 * 添加用户
	 *
	 * @param user
	 * @param pw
	 * @throws Exception
	 */
	public void addUser(String user, String pw) throws Exception;

	/**
	 * 删除用户
	 * @param user
	 * @throws Exception
	 */
	public void removeUser(String user) throws Exception;

	/**
	 * 添加domain
	 * @throws Exception
	 */
	public void addDomain(String domain) throws Exception;

	/**
	 * 删除domain
	 * @throws Exception
	 */
	public void removeDomain(String domain) throws Exception;

}
