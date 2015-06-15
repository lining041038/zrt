package antelope.quartz;

/**
 *
 * @author huanggc
 * @since 2012-2-17
 */
public enum TaskGroup{
	DEFAULT("其他"),
	MAIL("邮件"),
	SYNCDATA("数据同步");
	private  String name;
	TaskGroup(String name){
		this.name = name;	
	}
	public String getName(){
		return this.name;
	}
	@Override
	public String toString(){
		return this.name;
	}
	
}
