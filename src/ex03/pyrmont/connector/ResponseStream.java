package ex03.pyrmont.connector;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

import ex03.pyrmont.connector.http.HttpResponse;
/**
 * 方便实现ServletOutputStream，与Response的标准ResponseBase实现协同工作。
 * 如果在关联的响应上设置了内容长度，则此实现将强制在基础流上写入的字节数不超过该字节数。
 *  
 *
 */
public class ResponseStream extends ServletOutputStream{
	
    public ResponseStream(HttpResponse response) {

        super();
        closed = false;
        commit = false;
        count = 0;
        this.response = response;
      //  this.stream = response.getStream();

    }

	
    protected boolean closed = false;//流是否关闭
    
    protected boolean commit = false;//我们应该在刷新时提交响应吗
    
    protected int count = 0;//已写入此流的字节数。
    
    protected int length = -1;//如果没有定义的内容长度，则为-1。
    
    protected HttpResponse response = null;//与此输入流关联的响应。
    
    protected OutputStream stream = null;//我们应该向其写入数据的底层输出流。
	
    //[Package Private]返回“commit response on flush”标志。
    public boolean getCommit() {

        return (this.commit);

    }
/**
 * [Package Private]设置“commit response on flush”标志。
 */
    public void setCommit(boolean commit) {

        this.commit = commit;

    }
    
    /**
     * 关闭这个输出流，导致刷新任何缓冲数据，并引发任何进一步的输出数据抛出IOException。
     */
    public void close() throws IOException {
        if (closed)
            throw new IOException("responseStream.close.closed");
        response.flushBuffer();
        closed = true;
    }

    /**
     * 刷新此输出流的任何缓冲数据，这也会导致提交响应。
     */
    public void flush() throws IOException {
        if (closed)
                throw new IOException("responseStream.flush.closed");
           if (commit)
                response.flushBuffer();

        }
    
    /**
     * 将指定的字节写入输出流。
     */
    
	@Override
	public void write(int b) throws IOException {
        if (closed)
            throw new IOException("responseStream.write.closed");

        if ((length > 0) && (count >= length))
            throw new IOException("responseStream.write.count");

        response.write(b);
        count++;
	}
	
	
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);

    }

	
	public void write(byte b[], int off, int len) throws IOException {
        if (closed)
            throw new IOException("responseStream.write.closed");

        int actual = len;
        if ((length > 0) && ((count + len) >= length))
            actual = length - count;
        response.write(b, off, actual);
        count += actual;
        if (actual < len)
            throw new IOException("responseStream.write.count");

    }
	
	
    /**
     * 相应流是否关闭
     */
    boolean closed() {
        return (this.closed);

    }
	
	
/**
 * 将写入此流的字节数重置为零。
 */
    void reset() {

        count = 0;

    }

}
