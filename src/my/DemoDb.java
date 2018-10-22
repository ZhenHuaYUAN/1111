package my;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import af.util.AfReflect2;
import af.util.AfTextFile;

/* 一个演示用的数据库
 * 
 */
public class DemoDb
{
	// 全局对象
	public static DemoDb i = new DemoDb();
	
	// 存储位置
	private File jsonfile = new File("c:/webdata/students.json");
	
	// 内部数据
	private List<Student> data = new ArrayList<Student>();
	
	private DemoDb()
	{
		// 创建相关路径
		jsonfile.getParentFile().mkdirs();
		
		// 启动时加载数据
		try{
			this.load();
		}catch(Exception e){}
	}
	
	// 保存数据到文件
	public void save() throws Exception
	{	
		JSONArray jarray = new JSONArray(data);
		AfTextFile.write(jsonfile, jarray.toString(2), "UTF-8");
	}
	
	// 从文件中加载数据
	public void load() throws Exception
	{	
		String text = AfTextFile.read(jsonfile, "UTF-8");
		JSONArray jarray = new JSONArray(text);
		
		this.data.clear();
		for(int i=0; i<jarray.length(); i++)
		{
			JSONObject jrow = jarray.getJSONObject(i);
			Student s = (Student) AfReflect2.map(Student.class, jrow.toMap());
			this.data.add( s );			
		}
	}
		
	// 添加
	public void add(Student s)
	{
		data.add( s );
	}
	// 获取全部
	public List<Student> list()
	{
		return data;
	}
	
	// 按学号查询
	public List<Student> list(int from , int to)
	{
		List<Student> result = new ArrayList<Student>();
		for(Student s: data)
		{
			if(s.id >= from && s.id <= to)
			{
				result.add( s );
			}
		}
		return result;
	}
	
	// 按名字查询
	public List<Student> list(String name)
	{
		List<Student> result = new ArrayList<Student>();
		for(Student s: data)
		{
			if(s.name.indexOf( name ) >=0 ) 
			{
				result.add( s );
			}
		}
		return result;
	}
	
	public boolean remove(int id){
		Iterator<Student> iter = data.iterator();
		while(iter.hasNext()){
			Student s = iter.next();
			if(s.getId() == id){
				iter.remove();
				return true;
			}
		}
		return false;
	}
	
}
