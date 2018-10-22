package my;

import java.util.HashMap;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import af.web.restful.AfRestfulService;

@WebServlet("/StudentRemove")
public class StudentRemoveService extends AfRestfulService
{

	@Override
	protected Object execute(HttpServletRequest httpReq, HttpServletResponse httpResp, JSONObject jreq,
			HashMap<String, String> queryParams) throws Exception
	{
		int id = jreq.getInt("id");
		if(DemoDb.i.remove(id)){
			DemoDb.i.save();
		}
		return null;
	} 

}
