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
import model.AnalysisGrade;
import util.SortExamStuResult;

@WebServlet(name = "ExamStuResultBranchPageServlet", urlPatterns = {"/manager_sturesult_exam_message.servlet"})
public class ExamStuResultBranchPage extends HttpServlet {
	private static final long serialVersionUID = 5952689219411916553L;
	
	//ģ����ѯ��������
	private String condition = "";

	private ArrayList<AnalysisGrade> analysisGrades;
	
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
		
		//�����ݿ��ѯ��Ӧexam_id��ȫ���ɼ�
		analysisGrades = sturesultdao.sturesult_exam_message_hibernate(condition,totalUsers_page);
		
		Collections.sort(analysisGrades, new SortExamStuResult());
		
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
        int totalUsers = analysisGrades.size();
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
        req.setAttribute("analysisGrades", analysisGrades);
        req.getRequestDispatcher("jsp/manager/sturesult_exam_message.jsp").forward(req, resp);
		
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
	}

}