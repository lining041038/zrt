package antelope.quartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Task default datetimeCron
 * 
 * @author huanggc
 * @since 2012-2-17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD}) 
public @interface CronExpression {

	//支持两种方式
	String value();
}
