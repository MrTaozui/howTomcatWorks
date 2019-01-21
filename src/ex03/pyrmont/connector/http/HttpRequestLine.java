package ex03.pyrmont.connector.http;

public class HttpRequestLine {
	
	public static final int INITIAL_METHOD_SIZE = 8;//初始的方法数量
	public static final int INITIAL_URI_SIZE = 64;//
    public static final int INITIAL_PROTOCOL_SIZE = 8;
    public static final int MAX_METHOD_SIZE = 1024;
    public static final int MAX_URI_SIZE = 32768;
    public static final int MAX_PROTOCOL_SIZE = 1024;

    public char[] method;
    public int methodEnd;
    public char[] uri;
    public int uriEnd;
    public char[] protocol;
    public int protocolEnd;
    
    public HttpRequestLine() {

        this(new char[INITIAL_METHOD_SIZE], 0, new char[INITIAL_URI_SIZE], 0,
             new char[INITIAL_PROTOCOL_SIZE], 0);

    }
    
    public HttpRequestLine(char[] method, int methodEnd,char[] uri, int uriEnd,char[] protocol, int protocolEnd){
        this.method = method;
        this.methodEnd = methodEnd;
        this.uri = uri;
        this.uriEnd = uriEnd;
        this.protocol = protocol;
        this.protocolEnd = protocolEnd;
    }
    /**
     * 释放所有对象引用，并初始化实例变量，以便重用这个对象。
     */
    public void recycle() {
        this.methodEnd = 0;
        this.uriEnd = 0;
        this.protocolEnd = 0;
    }
    
    
    
    /**
     * 返回字符所在的索引位置
     * @param c
     * @param start
     * @return
     */
    public int indexOf(char c, int start ){
    	for(int i=start; i<this.uriEnd; i++) {
    		if(this.uri[i] == c)
    			return i;
    	}
    	return -1;
    }
    
   /**
    * 测试报头的值是否包含给定的字符数组
    * @param buf  要匹配的字符数组
    * @param end  连续匹配的长度
    * @return   索引位置
    */
    public int indexOf(char[] buf, int end) {
    	char firstChar = buf[0];
    	int pos = 0;
        while (pos < uriEnd) {
            pos = indexOf(firstChar, pos);// 先找到首字母的位置
            if (pos == -1)
                return -1;
            if ((uriEnd - pos) < end)
                return -1;
            for (int i = 0; i < end; i++) {
                if (uri[i + pos] != buf[i])
                    break;
                if (i == (end-1))
                    return pos;
            }
            pos++;
        }
        return -1;
    }
    
    // --------------------------------------------------------- Object Methods


    public int hashCode() {
        // FIXME
        return 0;
    }


    public boolean equals(Object obj) {
        return false;
    }



}
