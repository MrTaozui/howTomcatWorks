package ex03.pyrmont.connector.http;

/**
 * 请求头
 * @author taojiajun
 *
 */
public final class HttpHeader {
	
	//--------------------常量-----------------------
	
	public static final int INITIAL_NAME_SIZE = 32;//初始化名称大小
	public static final int INIT_VALUE_SEIZ= 64;//初始化值的大小
	public static final int MAX_NAME_SIZE = 128;
	public static final int MAX_VALUE_SIZE =4096;
	
	//-----------------实例变量------------------
	public char[] name;
	public int nameEnd;
	public char[] value;
	public int valueEnd;
	protected int hashCode = 0;
	
	
	public HttpHeader() {
		this(new char[INITIAL_NAME_SIZE], 0, new char[INIT_VALUE_SEIZ], 0);
		
	}
	
	public HttpHeader(char[] name, int nameEnd, char[] value, int valueEnd) {
		
		this.name = name;
		this.nameEnd = nameEnd;
		this.value = value;
		this.valueEnd = valueEnd;
		
	}

	public HttpHeader(String name, String value) {
		this.name = name.toCharArray();
		this.nameEnd = name.length();
		this.value = value.toCharArray();
		this.valueEnd = value.length();
	
	}
	
	public void recycle() {
		this.nameEnd = 0;
		this.valueEnd = 0;
		this.hashCode = 0;
	}
	/**
	 *检测header 的name 是否等于给定的字节数组
	 * 所有的字节必须小写
	 * 
	 * @param buf
	 * @return
	 */
	public boolean equals(char[] buf){
		return this.equals(buf, buf.length);
	}
	
	/**
	 * 检测header 的name 是否等于给定的字节数组
	 * 所有的字节必须小写
	 * @param buf
	 * @param end
	 * @return
	 */
	public boolean equals(char[] buf, int end) {
		if(end != nameEnd)
			return false;
		for (int i=0; i<end; i++){
			if (buf[i] != name[i])
				return false;
		}
		return true;
	}

	/**
	 * 检测header 的name 是否等于给定的字节数组
	 * 所有的字节必须小写
	 * @param str
	 * @return
	 */
	public boolean equals(String str) {
		return this.equals(str.toCharArray(), str.length());
	}
	
	public boolean valueEquals(char[] buf) {
		
		return this.valueEqueals(buf, buf.length);
	}
	
	/**
	 * 校验header的value是否与给定的字节数组相同
	 * @param buf
	 * @param end
	 * @return
	 */
	public boolean valueEqueals(char[] buf, int end) {
		if (end != valueEnd)
			return false;
		for (int i=0; i<end; i++){
			if (buf[i] != value[i])
				return false;
			
		}
		return true;
	}
	
	/**
	 * 校验header的value是否与给定的字符串是否相同
	 * @param str
	 * @return
	 */
	public boolean valueEqueals(String str) {
		return this.valueEqueals(str.toCharArray(), str.length());
	}
	
	
	/**
	 * 检测字符数组是否包含在 header中的value中
	 * @param buf
	 * @return
	 */
	public boolean valueIncludes(char[] buf) {
		return valueIncludes(buf, buf.length);		
	}
	
	
	/**
	 * 检测字符数组是否包含在 header中的value中
	 * @param buf
	 * @param end
	 * @return
	 */
	public boolean valueIncludes(char[] buf, int end) {
		char firstChar = buf[0];
		int pos = 0;
		while (pos < valueEnd) {
			pos = valueIndexOf(firstChar, pos);
			if (pos == -1)
				return false;
			if ((this.valueEnd - pos) < end) //如果剩余长度没有 给的参数的长度长 则肯定不包含
				return false;
			
			for (int i=0; i < end; i++) {
				if (this.value[i + pos] != buf[i])
					break;
				if (i == (end -1)) // 如果连续相等直至 end长度 则 包含返回 ture
					return true;
			}
			pos++;
			
		}
		return false;
	}
	
	public boolean valueIncludes(String str) {
		return this.valueIncludes(str.toCharArray(), str.length());
	}
	
	
	/**
	 * 返回字符c在 value中的索引
	 * @param c
	 * @param start
	 * @return
	 */
	public int valueIndexOf(char c, int start) {
		for(int i=start; i<valueEnd; i++) {
			if (this.value[i] == c)
				return i;
		}
		return -1;
	}
	
	/**
	 * 检测header的name是否与给定的header相同
	 * 所有的name字符必须小写
	 * @param header
	 * @return
	 */
	public boolean queals(HttpHeader header) {
		return this.equals(header.name, header.nameEnd);
	}
	
	/**
	 * 检测header中的value和name是否与给定的header相同
	 * 所有的name字符必须小写
	 * @param header
	 * @return
	 */
	public boolean headerEquals(HttpHeader header) {
		return this.equals(header.name, header.nameEnd)
				&& this.valueEqueals(header.value, valueEnd);
	}
}
