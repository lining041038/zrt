package antelope.controllers.components;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.CharsetDecoder;
import java.util.HashMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.tomcat.util.buf.Utf8Decoder;

public class FileUploadMinaAdapter extends IoHandlerAdapter
{
	
	FileOutputStream fos = null;
	
	public long totalsize = 0;
    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
    {
        cause.printStackTrace();
    }

    @Override
    public void messageReceived( IoSession session, Object message ) throws Exception {
    	
    	
    	session.getId();
    	
    	
    	IoBuffer simpbuff = (IoBuffer) message;
    	
    	byte[] array = simpbuff.array();
    	
    	String str = new String(simpbuff.array(), 0, simpbuff.limit());
    	
    	
    	//System.out.println(str);
    	
    	//simpbuff.getString(arg0, arg1)
    	//simpbuff.get
    	
    //	String string = simpbuff.getString(simpbuff.limit(), new Utf8Decoder());
    	
    	
    	if (str.length() == 10)
    		return;
    	
    	fos.write(array, 0, simpbuff.limit());
    	totalsize += simpbuff.limit();
    	//System.out.println(totalsize);
    	//fos.flush();
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	
    	InetSocketAddress socketaddress = (InetSocketAddress) session.getRemoteAddress();
    	System.out.println(socketaddress.getHostName());
    	System.out.println(socketaddress.getHostString());
    	
    	System.out.println(socketaddress.getAddress().getHostAddress());
    	
    	fos = new FileOutputStream("D:/tmp.mp4", true);
    	
    	System.out.println(session.getId());
    }
    
    @Override
    public void sessionClosed(IoSession session) throws Exception {
    	fos.close();
    	
        System.out.println(totalsize);
    }

    @Override
    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception {
    	
    	//System.out.println(session.getId());
    	
       // System.out.println( "IDLE " + session.getIdleCount( status ));
    }
    
}
