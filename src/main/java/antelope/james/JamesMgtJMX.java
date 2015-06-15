package antelope.james;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 *
 * 封装了采用JMX方式的用户操作
 * @author dai jun
 * @time 2012-10-29
 *
 */
public class JamesMgtJMX implements JamesMgtInterface{
	// JMX Server连接
	MBeanServerConnection serverConn = null;
	// JMX 用户仓库对象
	ObjectName userRepoObjName = null;
	// JMX domian对象
	ObjectName domainObjName = null;

	/**
	 * 获取连接
	 * @throws Exception
	 */
	private MBeanServerConnection getConn() throws Exception {
		if (null == serverConn){
				JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi");;
				JMXConnector jmxc = JMXConnectorFactory.connect(url);
				serverConn = jmxc.getMBeanServerConnection();
		}
		return serverConn;
	}

	/**
	 * 获取用户操作对象
	 * @return
	 * @throws Exception
	 */
	private ObjectName getUserRepoObject() throws Exception{
		if (null == userRepoObjName){
			userRepoObjName =new ObjectName("org.apache.james:type=component,name=usersrepository");
		}
		return userRepoObjName;
	}

	/**
	 * 获取domain操作对象
	 *
	 * @return
	 * @throws Exception
	 */
	private ObjectName getDomainObject() throws Exception {
		if (null == domainObjName){
			domainObjName =new ObjectName("org.apache.james:type=component,name=domainlist");
		}
		return domainObjName;
	}

	@Override
	public void addUser(String user, String pw) throws Exception {
		ObjectName objName = getUserRepoObject();
		getConn().invoke(objName, "addUser", new Object[] { user,pw }, new String[] { String.class.getName(),String.class.getName() });
	}

	@Override
	public void removeUser(String user) throws Exception{
		getConn().invoke(getUserRepoObject(), "deleteUser", new Object[] { user}, new String[] { String.class.getName()});
	}

	@Override
	public void addDomain(String domain) throws Exception{
		ObjectName objName = getDomainObject();
		getConn().invoke(objName, "addDomain", new Object[] { domain}, new String[] { String.class.getName()});
	}

	@Override
	public void removeDomain(String domain) throws Exception{
		ObjectName objName = getDomainObject();
		getConn().invoke(objName, "removeDomain", new Object[] { domain}, new String[] { String.class.getName()});
	}
}
