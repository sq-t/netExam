package servlet;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import model.Judgequestion;
import util.SortJudgequestion;
import dao.JudgeDao;

@WebServlet(name = "JudgeBranchPageServlet", urlPatterns = {"/manager_judge.servlet"})
public class JudgeBranchPage extends HttpServlet{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private List<Judgequestion> judges;
	    //question��dao����
	    @Autowired
	    private JudgeDao judgedao;
	    
		public JudgeDao getJudgedao() {
			return judgedao;
		}

		public void setJudgedao(JudgeDao judgedao) {
			this.judgedao = judgedao;
		}

		@SuppressWarnings("unchecked")
		@Override
	    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        String condition = req.getParameter("condition");
	        String totalUsersStr = req.getParameter("totalUsers");
	        
	        int totalUsers_page;
	        if(totalUsersStr == null) {
	        	totalUsers_page = -1;
	        }
	        else {
	        	totalUsers_page = Integer.parseInt(totalUsersStr);
	        }
	        judges = judgedao.list_judges(condition,totalUsers_page);
	        
	        Collections.sort(judges, new SortJudgequestion());
	        
			String p = req.getParameter("page");
	        int page;
	        try {
	            //��ǰҳ��
	            page = Integer.valueOf(p);
	        } catch (NumberFormatException e) {
	            page = 1;
	        }
	        //�û�����
	        int totalUsers = judges.size();
	        //ÿҳ�û���
	        int usersPerPage = 5;
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
	        req.setAttribute("judges", judges);
	        req.getRequestDispatcher("jsp/manager/manager_judge.jsp").forward(req, resp);
	    }

	    @Override
		public void init(ServletConfig config) throws ServletException {
	    	super.init(config);
	    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
		}


}
