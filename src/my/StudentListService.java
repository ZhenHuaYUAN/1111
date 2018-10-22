package my;

import java.util.HashMap;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import af.web.restful.AfRestfulService;

@WebServlet("/StudentList")
public class StudentListService extends AfRestfulService
{

	@Override
	protected Object execute(HttpServletRequest httpReq, 
			HttpServletResponse httpResp, 
			JSONObject jreq,
			HashMap<String, String> queryParams) throws Exception
	{
		List<Student> rows = DemoDb.i.list();
		JSONArray result = new JSONArray(rows);
		return result;
	}

}
