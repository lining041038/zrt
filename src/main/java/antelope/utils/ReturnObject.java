/**
 * ReturnObject.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package antelope.utils;

public class ReturnObject <A> {
    private A msg = null;

    private boolean success = true;

    public ReturnObject() {
    }

    public ReturnObject(
           A msg,
           boolean success) {
           this.msg = msg;
           this.success = success;
    }


    /**
     * Gets the msg value for this ReturnObject.
     * 
     * @return msg
     */
    public A getMsg() {
        return msg;
    }


    /**
     * Sets the msg value for this ReturnObject.
     * 
     * @param msg
     */
    public void setMsg(A msg) {
        this.msg = msg;
    }
    
    public ReturnObject<A> setFalseMsg(A msg) {
    	this.success = false;
    	this.msg = msg;
    	return this;
    }
    
    /**
     * Gets the success value for this ReturnObject.
     * 
     * @return success
     */
    public boolean isSuccess() {
        return success;
    }


    /**
     * Sets the success value for this ReturnObject.
     * 
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public static <A> ReturnObject<A> createFailObj(A msg) {
    	ReturnObject<A> ro = new ReturnObject<A>();
    	return ro.setFalseMsg(msg);
    }
    
    public static <A> ReturnObject<A> createSuccessObj(A msg) {
    	return new ReturnObject<A>(msg, true);
    }
    
    public static <A> ReturnObject<A> createSuccessObj(){
    	return new ReturnObject<A>();
    }
}
