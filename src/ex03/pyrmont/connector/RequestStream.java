package ex03.pyrmont.connector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ClosedByInterruptException;

import javax.servlet.ServletInputStream;

import org.apache.catalina.util.StringManager;

import ex03.pyrmont.connector.http.Constants;
import ex03.pyrmont.connector.http.HttpRequest;
/**
 * 作用：读取InputStream
 * 
 * 
 * 方便的实现ServletInputStream的工作

	请求的标准实现。如果内容长度有

	已在关联请求上设置，此实现将强制执行

	在底层流上读取的字节数不超过这个数。
 *
 */

public class RequestStream extends ServletInputStream{
	

    /**
     * 这个流 是否关闭
     */
    protected boolean closed = false;


    /**
     * 这个流已经返回的字节数。
     */
    protected int count = 0;


    /**
     *如果没有定义的内容长度，则为-1。
     */
    protected int length = -1;
    
    /**
     * 我们应该从中读取数据的底层输入流。
     */
    protected InputStream stream = null;
	
	 public RequestStream(HttpRequest request) {
		 super();
		 this.closed = false;
		 this.count = 0;
		 this.length = request.getContentLength();
		 this.stream = request.getStream();
		 
	}

	 protected static StringManager sm =
	            StringManager.getManager(Constants.Package);
	    
	 /**
	  * 关闭这个输入流。没有执行物理级别I-O，但是任何从该流读取的进一步尝试都将抛出IOException。如果设置了内容长度，但还没有使用所有字节，则会吞下剩余的字节。
	  */
	   public void close() throws IOException {

	        if (closed)
	            throw new IOException(sm.getString("requestStream.close.closed"));

	        if (length > 0) {
	            while (count < length) {
	                int b = read();
	                if (b < 0)
	                    break;
	            }
	        }
	        closed = true;
	    }
	   
	 /**
	  * 从该输入流读取并返回一个字节，如果遇到文件末尾，则返回-1。
	  */
	@Override
	public int read() throws IOException {
		if(closed)
			throw new IOException(sm.getString("requestStream.read.closed"));
		
		//我们是否已经读取了指定的内容长度?
        if ((length >= 0) && (count >= length))
            return (-1);        // 文件结束指示器
        //读取count的下一个字节，然后返回
        int b = stream.read();
        if (b >= 0)
            count++;
        return (b);
		
	}
	
/**
 * 从输入流中读取一定数量的字节，并存储它们
 */
    public int read(byte b[]) throws IOException {

        return (read(b, 0, b.length));

    }
	/**
	 * 
	 * @param 读取数据的缓冲区
     * @param 将起始偏移量
     *  
     * @param 要读取的最大字节数
     */
    public int read(byte b[], int off, int len) throws IOException {

        int toRead = len;
        if (length > 0) {
            if (count >= length)
                return (-1);
            if ((count + len) > length)
                toRead = length - count;
        }
        int actuallyRead = super.read(b, off, toRead);
        return (actuallyRead);

    }

}
