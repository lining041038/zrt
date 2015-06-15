package antelope.james;


public class JamesJMX_Test {

	/**
	 * @time 2012-10-29
	 * @author daijun
	 */
	public static void main(String[] args) {
		// test add user
		JamesMgtInterface tester = new JamesMgtJMX();
		try {
			tester.addUser("pink", "red"); //add a user -> user:pink, pw:red
			System.out.println("--------pls. pause by the break point and check the user table in the database-------");
			tester.removeUser("pink"); //remove a user -> user:pink

			tester.addDomain("testDomain");
			System.out.println("--------pls. pause by the break point and check the user table in the database-------");
			tester.removeDomain("testDomain");

		}catch (Exception e){
			e.printStackTrace();
		}

	}

}
