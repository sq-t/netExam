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

import model.Exam;
import util.SortExam;
import dao.ExamDao;

@WebServlet(name = "ExamBranchPageServlet", urlPatterns = {"/manager_exam_message.servlet"})
public class ExamBranchPage extends HttpServlet{
	private static final long serialVersionUID = 5952689219411916553L;
		
		private ArrayList<Exam> exams;
		private ArrayList<Exam> start_exams;
		private ArrayList<Exam> end_exams;
	    
	    //exam��dao����
	    @Autowired
	    private ExamDao examdao;

		@SuppressWarnings("unchecked")
		@Override
	    protected void service(HttpServletRequest req, HttpServletResponse resp)
	    		throws ServletException, IOException {
	        String condition = req.getParameter("condition");
	        String status = req.getParameter("status");
	        String to_page = req.getParameter("to_page");
	        String totalUsersStr = req.getParameter("totalUsers");
	        
	        int totalUsers_page;
	        if(totalUsersStr == null) {
	        	totalUsers_page = -1;
	        }
	        else {
	        	totalUsers_page = Integer.parseInt(totalUsersStr);
	        }
	        if(status == null) {
	        	status = "";
	        }
	        if(condition == null) {
	        	condition = "";
	        }
	        if(to_page == null) {
	        	to_page = "";
	        }
	        
	        exams = examdao.list_exam(condition,status,totalUsers_page);
	        
	        Collections.sort(exams, new SortExam());
	        
	        
		
	        
	        start_exams = new ArrayList<Exam>();
	        end_exams = new ArrayList<Exam>();
	        
	        for(int i = 0;i < exams.size();i++) {
	        	if(exams.get(i).getStatus().equals("���Խ�����")) {
	        		start_exams.add(exams.get(i));
	        	}
	        	else {
	        		end_exams.add(exams.get(i));
	        	}
	        }
	        
			String p = req.getParameter("page");
	        int page;
	        try {
	            //��ǰҳ��
	            page = Integer.valueOf(p);
	        } catch (NumberFormatException e) {
	            page = 1;
	        }
	        //�û�����
	        int totalUsers;
	        if(status.equals("�����ѽ���")) {
	        	exams = end_exams;
	        	
		        totalUsers = exams.size();
	        }
	        else {
	        	exams = start_exams;
	        	//�û�����
		        totalUsers = exams.size();
	        }
	        
	        //ÿҳ�û���
	        int usersPerPage = 10;
	        //��ҳ��=(
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
	        
	        if(status.equals("�����ѽ���")) {
	        	req.setAttribute("exams", exams);
	        	if(to_page.equals("end_exam_manager.jsp")) {
	        		req.getRequestDispatcher("jsp/manager/end_exam_manager.jsp").forward(req, resp);
	        	}
	        	else {
	        		req.getRequestDispatcher("jsp/manager/end_exam_message.jsp").forward(req, resp);
	        	}
	        }
	        else {
	        	req.setAttribute("exams", exams);
	        	req.getRequestDispatcher("jsp/manager/exam_message.jsp").forward(req, resp);
	        }
	    }

		@Override
		public void init(ServletConfig config) throws ServletException {
			super.init(config);
		    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,config.getServletContext());
		}

}