package dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.opensymphony.xwork2.ActionContext;

import model.AnalysisGrade;
import model.Exam;
import model.Student;
import model.Sturesult;

public class StuResultDao {
	//���Է������󼯺�
	private ArrayList<AnalysisGrade> analysisGrades;
	
	public ArrayList<AnalysisGrade> getAnalysisGrades() {
		return analysisGrades;
	}

	public void setAnalysisGrades(ArrayList<AnalysisGrade> analysisGrades) {
		this.analysisGrades = analysisGrades;
	}

	//ѧ���ɼ����󼯺�
	private ArrayList<Sturesult> sturesults;
	
	public ArrayList<Sturesult> getSturesults() {
		return sturesults;
	}

	public void setSturesults(ArrayList<Sturesult> sturesults) {
		this.sturesults = sturesults;
	}

	//hibernate ��ؽӿ�
	private Configuration configuration;
	private SessionFactory sessionFactory;
	private Session session;
//	private Transaction transaction;
	
	//���session����
	@SuppressWarnings("deprecation")
	public Session getSession(){
		configuration = new Configuration().configure();
		sessionFactory = configuration.buildSessionFactory();
		session =  sessionFactory.openSession();
		return session;
	}
	
	public void close_interface(){
		session.close();
		sessionFactory.close();
	}
	
	//ģ����ѯ��ĳ�ſ��Ե�ѧ���ɼ�����
	@SuppressWarnings("unchecked")
	public ArrayList<Sturesult> list_sturesult_hibernate(String condition,String exam_id,int totalUsers) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Sturesult";
		
		Query query = session.createQuery(hql);
		 
		if(query.list().size() != totalUsers && totalUsers != -1) {
			return sturesults;
		}
		
		hql = "from Sturesult where exam.id = ? and ( exam.lesson.name like ? or student.name like ? or student.id like ?";
		
		//��֤condition�Ƿ�Ϊ������
		String pattern = "[0-9]{1,}";
		boolean isMatch = Pattern.matches(pattern, condition);
		
		if(isMatch) {
			hql += " or id = "+ Long.parseLong(condition);
			
		}
		
		//��֤conditions�Ƿ�ΪС��
		pattern = "[0-9]{1,}+(.[0-9]{1,})";
		
		isMatch = Pattern.matches(pattern, condition);
		if(isMatch) {
			hql += " or resSingle = ? or resMore = ? or resJudge = ? or resTotal = ? ";
		}
		hql += ")";
		
		query = session.createQuery(hql);
		
		query.setParameter(0, Long.parseLong(exam_id));
		query.setParameter(1, "%" + condition + "%");
		query.setParameter(2, "%" + condition + "%");
		query.setParameter(3, "%" + condition + "%");
		if(isMatch) {
			query.setParameter(4, Double.valueOf(condition.toString()));
			query.setParameter(5, Double.valueOf(condition.toString()));
			query.setParameter(6, Double.valueOf(condition.toString()));
			query.setParameter(7, Double.valueOf(condition.toString()));
		}
				
		sturesults = (ArrayList<Sturesult>)query.list();
		return sturesults;
	}

	//���Է���dao����
	@SuppressWarnings("unchecked")
	public ArrayList<Sturesult> anlysis_grade_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Sturesult where exam.id = " + exam.getId();
		
		Query query = session.createQuery(hql);
		
		ArrayList<Sturesult> sturesults = (ArrayList<Sturesult>)query.list();
		
		return sturesults;
	}
	
	public AnalysisGrade analysis_sturesult(ArrayList<Sturesult> sturesults) {
		int examNum;
		examNum = sturesults.size();
		
		int passNum = 0;
		double passRate = 0;
		
		double maxScore = 0;
		double minScore = 0;
		
		double totalScore = 0;
		double averageScore = 0;
		
		double totalSingle = 0;
		double averageSingle = 0;
		
		double totalMore = 0;
		double averageMore = 0;
		
		double totalJudge = 0;
		double averageJudge = 0;
		
		if(examNum != 0) {
			maxScore = sturesults.get(0).getResTotal();
			minScore = sturesults.get(0).getResTotal();
			
			for(int i = 0;i < examNum;i++) {
				totalScore += sturesults.get(i).getResTotal();
				totalSingle += sturesults.get(i).getResSingle();
				totalMore += sturesults.get(i).getResMore();
				totalJudge += sturesults.get(i).getResJudge();
				if(sturesults.get(i).getResTotal() >= 60) {
					passNum++;
				}
				if(sturesults.get(i).getResTotal() > maxScore) {
					maxScore = sturesults.get(i).getResTotal();
				}
				if(sturesults.get(i).getResTotal() < minScore) {
					minScore = sturesults.get(i).getResTotal();
				}
			}
			
			if(passNum != 0) {
				passRate = examNum / passNum;
			}
			averageScore = (double) Math.round((totalScore / examNum) * 100) / 100;
			averageSingle = (double) Math.round((totalSingle / examNum) * 100) / 100;
			averageMore = (double) Math.round((totalMore / examNum) * 100) / 100;
			averageJudge = (double) Math.round((totalJudge / examNum) * 100) / 100;
			
		}
		
		
		AnalysisGrade analysisGrade = new AnalysisGrade(String.valueOf(examNum),String.valueOf(passRate * 100) + "%",String.valueOf(maxScore),
				String.valueOf(minScore),String.valueOf(averageScore),String.valueOf(averageSingle),
				String.valueOf(averageMore),String.valueOf(averageJudge));
		
		return analysisGrade;
	}

	public Exam total_by_exam_id(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		return (Exam)session.get(Exam.class, exam.getId());
	}

	@SuppressWarnings("unchecked")
	public ArrayList<AnalysisGrade> sturesult_exam_message_hibernate(String condition, int totalUsers) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Exam where status = '�����ѽ���'";
		
		Query query = session.createQuery(hql);
		 
		if(query.list().size() != totalUsers &&totalUsers != -1) {
			return analysisGrades;
		}
		
		hql = "from Exam where status = '�����ѽ���' and ( lesson.name like ?"; 
		
		//��֤condition�Ƿ�Ϊ������
		String pattern = "[0-9]{1,}";
		boolean isMatch = Pattern.matches(pattern, condition);
		
		if(isMatch) {
			hql += " or id = "+ Long.parseLong(condition);
			
		}
		hql += ")";
		
		query = session.createQuery(hql);
		query.setParameter(0, "%" +condition+ "%");
		
		ArrayList<Exam> exams;
		
		exams = (ArrayList<Exam>)query.list();
		
		analysisGrades = new ArrayList<AnalysisGrade>();
		
		AnalysisGrade analysisGrade = null;
 		
		for(int i = 0; i < exams.size();i++) {
			ArrayList<Sturesult> exam_sturesults = anlysis_grade_hibernate(exams.get(i));
			analysisGrade = analysis_sturesult(exam_sturesults);
			analysisGrade.setExam(exams.get(i));
			analysisGrades.add(analysisGrade);
		}
		
		return analysisGrades;
	}
	
	public ArrayList<Sturesult> list_sturesult_grades(String condition) {
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session2 = context.getSession();
		Student student = (Student)session2.get("student");
		//��ȡhibernate��session����
		session = this.getSession();
		String hql = "from Sturesult where student.id = ? and(exam.lesson.name like ?  ";
		//��֤condition�Ƿ�Ϊ������
		String pattern = "[0-9]{1,}";
		boolean isMatch = Pattern.matches(pattern, condition);
		if(isMatch) {
			hql += " or resSingle = ? or resMore = ? or resJudge = ? or resTotal = ? ";
		}
		hql+=")";
		Query query = session.createQuery(hql);
		query.setParameter(0, student.getId());
		query.setParameter(1, "%"+condition+"%");
		if(!condition.equals("")&&isMatch) {
			query.setParameter(2, Double.valueOf(condition.toString()));
			query.setParameter(3, Double.valueOf(condition.toString()));
			query.setParameter(4, Double.valueOf(condition.toString()));
			query.setParameter(5, Double.valueOf(condition.toString()));
		}
				
		@SuppressWarnings("unchecked")
		ArrayList<Sturesult> sturesults = (ArrayList<Sturesult>)query.list();
		return sturesults;
	}
}
