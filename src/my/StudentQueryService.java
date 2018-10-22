package my;

import java.util.HashMap;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;


import af.web.restful.AfRestfulService;

@WebServlet("/StudentQuery")
public class StudentQueryService extends AfRestfulService
{

	@Override
	protected Object execute(HttpServletRequest httpReq, HttpServletResponse httpResp, JSONObject jreq,
			HashMap<String, String> queryParams) throws Exception
	{
		String name = jreq.getString("filter");
		List<Student> rows = DemoDb.i.list(name);
		JSONArray result = new JSONArray(rows);
		return result;
	}

}
