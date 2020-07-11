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

import dao.StuResultDao;
import model.Sturesult;
import util.SortSturesult;

@WebServlet(name = "StuResultBranchPageServlet", urlPatterns = {"/manager_sturesult_message.servlet"})
public class StuReslultBranchPage extends HttpServlet {
	private static final long serialVersionUID = 5952689219411916553L;
	
	//ģ����ѯ��������
	private String condition = "";
	
	//Ҫ��ѯ�ɼ��Ŀ��Ե�id
	private String exam_id;
	
	//Ҫ��ѯ�ɼ��Ŀγ�����
	private String exam_lesson_name;

	private ArrayList<Sturesult> sturesults;
	
	//exam��dao����
    @Autowired
    private StuResultDao sturesultdao;

	@SuppressWarnings("unchecked")
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		condition = req.getParameter("condition");
		String totalUsersStr = req.getParameter("totalUsers");
        
        int totalUsers_page;
        if(totalUsersStr == null) {
        	totalUsers_page = -1;
        }
        else {
        	totalUsers_page = Integer.parseInt(totalUsersStr);
        }
		
		//���conditionΪnull������Ϊ""
		if(condition == null) {
			condition = "";
		}
		exam_id = req.getParameter("exam_id");
		
		//���exam_idΪnull������Ϊ""
		if(exam_id == null) {
			exam_id = "";
		}
		
		exam_lesson_name = req.getParameter("exam_lesson_name");
		
		//�����ݿ��ѯ��Ӧexam_id��ȫ���ɼ�
		sturesults = sturesultdao.list_sturesult_hibernate(condition,exam_id,totalUsers_page);
		
		Collections.sort(sturesults, new SortSturesult());
		
		//��ҳ����
		String p = req.getParameter("page");
		int page;
        try {
            //��ǰҳ��
            page = Integer.valueOf(p);
        } catch (NumberFormatException e) {
            page = 1;
        }
        //�û�����
        int totalUsers = sturesults.size();
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
        req.setAttribute("exam_id", exam_id);
        req.setAttribute("exam_lesson_name", exam_lesson_name);
        req.setAttribute("sturesults", sturesults);
        req.getRequestDispatcher("jsp/manager/sturesult_message.jsp").forward(req, resp);
		
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
	}

}