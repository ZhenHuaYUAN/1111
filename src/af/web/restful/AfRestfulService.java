package af.web.restful;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public abstract class AfRestfulService extends HttpServlet
{
	protected boolean enableErrorLog = false; // 是否打印异常输出
	protected boolean niceJSONFormat = false; // 输出的JSON是否综进 (缩进影响运行效率)
	protected int MAX_REQUEST_SIZE = 1024 * 512; // 允许上传的JSON最大长度
	protected String defaultCharset = "UTF-8";
	
	// 子类须重写这个方法，进行业务处理
	// 处理返回后，可以返回 JSONObject, JSONArray, 或int long String 等基本类型
	protected abstract Object execute(HttpServletRequest httpReq, 
			HttpServletResponse httpResp,
			JSONObject jreq, 
			HashMap<String,String> queryParams) throws Exception;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException
	{
		// 无论是 GET/POST, 均统一处理
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{		
		// 处理请求数据
		JSONObject jresp = new JSONObject();		
		try{
			// 读取请求数据, 转成字符串, 转成 JSON
			String reqText = readAsText(request.getInputStream(), defaultCharset, MAX_REQUEST_SIZE);
			JSONObject jreq = null;
			if(reqText.length()>0) jreq = new JSONObject(reqText);
			
			// URL末尾 , 由问号号引号的参数字符串 
			String query = request.getQueryString(); 
			HashMap<String,String> queryParams = parseQuery(query, defaultCharset);
			
			// 子类应重写 execute() 方法
			Object data = execute(request, response, jreq, queryParams);
			jresp.put("error", 0); // 错误码,0表示成功
			jresp.put("reason", "OK"); // 错误原因描述, 如果没有错误则提示OK
			if(data != null)
				jresp.put("data", data); // 
		}
		catch(AfRestfulException e)
		{
			jresp.put("error", e.error); // 错误码,0表示成功
			jresp.put("reason", e.getMessage()); // 错误原因描述, 如果没有错误则提示OK
			if(enableErrorLog) e.printStackTrace();
		}
		catch(Exception e)
		{
			jresp.put("error", -1); // 错误码,0表示成功
			jresp.put("reason", e.getMessage()); // 错误原因描述, 如果没有错误则提示OK
			if(enableErrorLog) e.printStackTrace();
		}
		
		// 是否按可读风格生成JSON ( 缩进格式 or 紧凑格式 )
		String jsonstr = niceJSONFormat ? jresp.toString(2) : jresp.toString();
		
		// 发送应答给客户端
		response.setCharacterEncoding(defaultCharset);
		response.setContentType("text/plain");
		//response.setHeader("Connection", "close");
		Writer writer = response.getWriter();
		writer.write( jsonstr );
		writer.close();		
	}
	
	/////////////////// 工具方法 //////////////////
	// 从 Stream 中读取数据直到读完
	public static String readAsText(InputStream streamIn, String charset, int maxSize)
			throws IOException 
	{
		ByteArrayOutputStream cache = new ByteArrayOutputStream(1024*16);  
        byte[] data = new byte[1024]; 
        
        int numOfWait = 0;
        while (true)
        {
        	int n = streamIn.read(data); // n: 实际读取的字节数
        	if(n < 0) break; // 连接已经断开
        	if(n == 0) 
        	{
        		if( numOfWait ++ >= 3) break; // 此种情况不得连续3次
        		try{ Thread.sleep(5);}catch(Exception e){}
        		continue;// 数据未完 //  
        	}
        	numOfWait = 0;

        	// 缓存起来
        	cache.write(data, 0, n);        	
        	if(cache.size() > maxSize) // 上限, 最多读取512K
        		break;
        }  
        
        return cache.toString(charset);
	}
	
	// 解析 Query 字符串
	public static HashMap<String,String> parseQuery(String query, String charset)
	{
		HashMap<String,String> params = new HashMap<String,String>();
		if(query == null) return params; // 为空
		
		String[] ppp = query.split("&"); // 用&分隔
		for(String p : ppp)
		{
			String[] kv = p.split("="); // key=value
			String key = kv[0];
			String value = "";
			if(key.length() > 1) value = kv[1]; // 有时候参数里传的是空值
			if( value.indexOf('%')>=0) 
			{
				// 如果存在百分号, 则进行URL解码
				try{
					value = URLDecoder.decode(value,charset);
				}catch(Exception e){}
			}
			params.put(key, value); 
		}
		return params;
	}
		
	// 从参数中取值
	public static Integer getParamInt(HashMap<String,String> params, String key, Integer defValue)
	{
		try{
			return Integer.valueOf(params.get(key));
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	public static Long getParamLong(HashMap<String,String> params, String key, Long defValue)
	{
		try{
			return Long.valueOf(params.get(key));
		}catch(Exception e)
		{
			return defValue;
		}
	}
	public static String getParamString(HashMap<String,String> params, String key, String defValue)
	{
		try{
			String val = params.get(key);
			if(val != null) return val;
		}catch(Exception e)	{}	
		return defValue;
	}
	public static Boolean getParamBoolean(HashMap<String,String> params, String key, Boolean defValue)
	{
		try{
			return Boolean.valueOf(params.get(key));
		}catch(Exception e)
		{
			return defValue;
		}
	}	
	
	
}
