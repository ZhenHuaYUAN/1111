package my;

import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import af.web.restful.AfRestfulService;

@WebServlet("/StudentAdd")
public class StudentAddService extends AfRestfulService
{

	@Override
	protected Object execute(HttpServletRequest httpReq, HttpServletResponse httpResp, JSONObject jreq,
			HashMap<String, String> queryParams) throws Exception
	{
		int id = jreq.getInt("id");
		String name = jreq.getString("name");
		String phone = jreq.getString("phone");
		boolean sex = "male".equals(jreq.getString("sex"));
		
		Student row = new Student(id, name, sex, phone);
		DemoDb.i.add(row);
		DemoDb.i.save();
		
		System.out.println("StudentAdd OK");
		return null;
	}

}
