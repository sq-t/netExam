package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import dao.ManagerDao;
import model.Suggest;
import util.SortSuggest;

@WebServlet(name = "ViewSuggestBranchPageServlet", urlPatterns = {"/manager_view_suggest.servlet"})
public class ViewSuggestBranchPage extends HttpServlet{
	private static final long serialVersionUID = 5952689219411916553L;
		
		private ArrayList<Suggest> suggests;
	
	    
	    //exam��dao����
	    @Autowired
	
	    private ManagerDao managerdao;
	    
		@SuppressWarnings("unchecked")
		@Override
	    protected void service(HttpServletRequest req, HttpServletResponse resp)
	    		throws ServletException, IOException {
	        String condition = req.getParameter("condition");
	        
	        if(condition == null) {
	        	condition = "";
	        }
	        suggests =managerdao.list_suggest();
	        
	        Collections.sort(suggests, new SortSuggest());
	        
			String p = req.getParameter("page");
	        int page;
	        try {
	            //��ǰҳ��
	            page = Integer.valueOf(p);
	        } catch (NumberFormatException e) {
	            page = 1;
	        }
	        //�û�����
	        int totalUsers = suggests.size();
	        //ÿҳ�û���
	        int usersPerPage = 10;
	        //��ҳ��
	        int totalPages = totalUsers % usersPerPage == 0 ? totalUsers / usersPerPage : totalUsers / usersPerPage + 1;
	        //��ҳ��ʼ�û����
	        int beginIndex = (page - 1) * usersPerPage;
	        //��ҳĩβ�û���ŵ���һ��
	        int endIndex = beginIndex + usersPerPage;
	        if (endIndex > totalUsers)
	            endIndex = totalUsers;
	        req.setAttribute("totalUsers", totalUsers);
	        req.setAttribute("usersPerPage", usersPerPage);
	        req.setAttribute("totalPages", totalPages);
	        req.setAttribute("beginIndex", beginIndex);
	        req.setAttribute("endIndex", endIndex);
	        req.setAttribute("page", page);
	        req.setAttribute("suggests", suggests);
        	req.getRequestDispatcher("jsp/manager/view_suggest.jsp").forward(req, resp);
		}
	     

		@Override
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
		    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
		}

}
