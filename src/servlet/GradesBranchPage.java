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
import model.Sturesult;
import util.SortSturesult;
import dao.StuResultDao;
/**
 * Servlet implementation class GradesBranchPage
 */
@WebServlet(name="/GradesBranchPageServlet", urlPatterns = {"/list_student_grades.servlet"})
public class GradesBranchPage extends HttpServlet {
	private static final long serialVersionUID = 5952689219411916553L;
    private List<Sturesult> grades;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    @Autowired
    private StuResultDao sturesultdao;
    public StuResultDao getSturesultdao(){
    	return sturesultdao;
    }
    public void setSturesultdao(StuResultDao sturesultdao){
    	this.sturesultdao=sturesultdao;
    }
    @SuppressWarnings("unchecked")
	@Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String condition = req.getParameter("condition");
        if(condition==null){
			condition="";
		}
        //��ȡȫ������
        grades = sturesultdao.list_sturesult_grades(condition);
        
        Collections.sort(grades, new SortSturesult());
        
		String p = req.getParameter("page");
        int page;
        try {
            //��ǰҳ��
            page = Integer.valueOf(p);
        } catch (NumberFormatException e) {
            page = 1;
        }
        //�û�����
        int totalUsers = grades.size();
        
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
        req.setAttribute("grades", grades);
        req.getRequestDispatcher("jsp/student/student_list_grades.jsp").forward(req, resp);
    }

    @Override
	public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
	}

}
