package dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Exam;
import model.Lesson;
import model.Sturesult;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ExamDao {
	//
	ArrayList<Exam> exams;
	
	public ArrayList<Exam> getExams() {
		return exams;
	}

	public void setExams(ArrayList<Exam> exams) {
		this.exams = exams;
	}
	
	

	//hibernate ��ؽӿ�
	private Configuration configuration;
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
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
	
	//ģ����ѯ��ʾ���е�exam����
	@SuppressWarnings("unchecked")
	public ArrayList<Exam> list_exam (String condition,String status,int totalUsers) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		//���conditionΪnull������Ϊ""
		if(condition == null) {
			condition = "";
		}
		
		if(status == null) {
			status = "";
		}
		
		String hql = "from Exam";
		
		Query query = session.createQuery(hql);
		 
		if(query.list().size() != totalUsers && totalUsers != -1) {
			return exams;
		}
		
		hql = "from Exam where (lesson.name like ? or status like ? or level like ?)";
		
		//��֤condition�Ƿ�Ϊ������
		String pattern = "[0-9]{1,}";
		boolean isMatch = Pattern.matches(pattern, condition);
		
		//�����ּ���id��ʱ����ģ����ѯ
		if(isMatch) {
			hql += " or (id = " + Integer.parseInt(condition) +" or duration = " + Integer.parseInt(condition)
				+ " or singleNum = " + Integer.parseInt(condition) +" or singleScore = " + Integer.parseInt(condition)
				+ " or moreNum = " + Integer.parseInt(condition) +" or moreScore = " + Integer.parseInt(condition)
				+ " or judgeNum = " + Integer.parseInt(condition) +" or judgeScore = " + Integer.parseInt(condition) + ")";
		}
		
		if(status.equals("�����ѽ���")) {
			hql += " and (status = '" + status + "')";
		}
		
		query = session.createQuery(hql);
		query.setParameter(0, "%"+condition+"%");
		query.setParameter(1, "%"+condition+"%");
		query.setParameter(2, "%"+condition+"%");
		exams = (ArrayList<Exam>)query.list();
		
		return exams;
	}
	
	//��ô������Կ�ѡ�Ŀγ���
	public ArrayList<String> list_select_course_hibernate() {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Lesson";
		
		Query query = session.createQuery(hql);
		
		//�õ�lessons����
		@SuppressWarnings("unchecked")
		ArrayList<Lesson> lessons = (ArrayList<Lesson>)query.list();
		int len = query.list().size();
		ArrayList<String> lesson_names = new ArrayList<String>();
		
		//�õ�lesson_names����
		for(int i = 0;i < len;i++) {
			lesson_names.add(lessons.get(i).getName());
		}
		
		return lesson_names;
	}
	
	//��������У��
	public String question_num_validation_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		//У�鵥ѡ����Ŀ
		String hql = "from Optionalquestion where type = '��ѡ��' and lesson.name = ?";
		
		Query query = session.createQuery(hql);
		query.setParameter(0, exam.getLesson().getName());
		int num = query.list().size();
		if(query.list().size() < exam.getSingleNum()) {
			return "��ѡ����Ŀ���㣬���������Ŀ��";
		}
		
		//У���ѡ����Ŀ
		hql = "from Optionalquestion where type = '��ѡ��' and lesson.name = ?";
		
		query = session.createQuery(hql);
		query.setParameter(0, exam.getLesson().getName());
		if(query.list().size() < exam.getMoreNum()) {
			return "��ѡ����Ŀ���㣬���������Ŀ��";
		}
		
		//У���ж�����Ŀ
		hql = "from Judgequestion where lesson.name = ?";
		
		query = session.createQuery(hql);
		query.setParameter(0, exam.getLesson().getName());
		if(query.list().size() < exam.getJudgeNum()) {
			return "�ж�����Ŀ���㣬���������Ŀ��";
		}
		
		//������
		return "";
	}
	
	//��������
	public void create_exam_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Lesson where name = ?";
		
		Query query = session.createQuery(hql);
		query.setParameter(0, exam.getLesson().getName());
		
		Lesson lesson = (Lesson)query.list().get(0);
		
		exam.setLesson(lesson);
		exam.setJoinTime(new Timestamp(System.currentTimeMillis()));
		exam.setStatus("���Խ�����");
		
		transaction = session.beginTransaction();
		session.save(exam);
		transaction.commit();
		
	}
	
	//ɾ������
	@SuppressWarnings("unchecked")
	public void delete_exam_hibernate(String[] sourceStrArray) {
		//��ȡhibernate��session����
		session = this.getSession();
		String hql;
		Query query;
		ArrayList<Sturesult> sturesults;
		Sturesult sturesult;
		Exam exam = new Exam();
		for(int i = 0;i < sourceStrArray.length;i++) {
			//��ȡ�������
			transaction = session.beginTransaction();
			exam.setId(Long.parseLong(sourceStrArray[i]));
			session.delete(exam);
			//�ύ����
			transaction.commit();
			
			//��ѯҪɾ���Ŀ������гɼ�
			hql = "from Sturesult where exam.id = " + Long.parseLong(sourceStrArray[i]);
			query = session.createQuery(hql);
			sturesults = (ArrayList<Sturesult>)query.list();
			
			//ɾ���ÿ������гɼ�
			for(int j = 0;j < sturesults.size();j++) {
				sturesult = sturesults.get(j);
				//��ȡ�������
				transaction = session.beginTransaction();
				session.delete(sturesult);
				//�ύ����
				transaction.commit();
			}
		}	
		
	}
	
	//�༭���ԣ����뿼��ҳ�棩
	public Exam edit_exam_one_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		Exam exam1 = (Exam)session.get(Exam.class, exam.getId());
		
		return exam1;
	}
	
	//�༭���ԣ��޸����ݿ⣩
	public void edit_exam_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Lesson where name = ?";
		
		Query query = session.createQuery(hql);
		query.setParameter(0, exam.getLesson().getName());
		
		Lesson lesson = (Lesson)query.list().get(0);
		
		exam.setLesson(lesson);
		
		//��ȡ�������
		transaction = session.beginTransaction();
		
		session.update(exam);
		
		//�ύ����
		transaction.commit();
		
	}

	
	//��ֹ����
	public void end_exam_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		exam = (Exam)session.get(Exam.class, exam.getId());
		
		exam.setStatus("�����ѽ���");
		
		//��ȡ�������
		transaction = session.beginTransaction();
		
		session.update(exam);
		
		//�ύ����
		transaction.commit();
		
	}
	//������ֹ����
	public void ends_exam_hibernate(String[] sourceStrArray) {
		session = this.getSession();
		Exam exam;
		for(int i = 0;i < sourceStrArray.length;i++) {
			//��ȡ�������
			transaction = session.beginTransaction();
			
			exam = (Exam)session.get(Exam.class, Long.parseLong(sourceStrArray[i]));
			exam.setStatus("�����ѽ���");
			
			session.update(exam);
			
			//�ύ����
			transaction.commit();
		}
		
	}
	
	//��������
	public void restart_exam_hibernate(Exam exam) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		exam = (Exam)session.get(Exam.class, exam.getId());
		
		exam.setStatus("���Խ�����");
		
		//��ȡ�������
		transaction = session.beginTransaction();
		
		session.update(exam);
		
		//�ύ����
		transaction.commit();
	}
	
	//������������
	public void restarts_exam_hibernate(String[] sourceStrArray) {
		session = this.getSession();
		Exam exam;
		for(int i = 0;i < sourceStrArray.length;i++) {
			//��ȡ�������
			transaction = session.beginTransaction();
			
			exam = (Exam)session.get(Exam.class, Long.parseLong(sourceStrArray[i]));
			exam.setStatus("���Խ�����");
			
			session.update(exam);
			
			//�ύ����
			transaction.commit();
		}
		
	}
	
	private ArrayList<Exam> stuExams;
	
	public ArrayList<Exam> getStuExams() {
		return stuExams;
	}

	public void setStuExams(ArrayList<Exam> stuExams) {
		this.stuExams = stuExams;
	}

	//����ѧ��
	@SuppressWarnings("unchecked")
	public ArrayList<Exam> list_student_exam(String condition, int totalUsers) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Exam where status='���Խ�����'";
		
		Query query = session.createQuery(hql);
		 
		if(query.list().size() != totalUsers && totalUsers != -1) {
			return stuExams;
		}
		
		hql = "from Exam where status='���Խ�����' and (lesson.name like ? ";
		
		//��֤condition�Ƿ�Ϊ������
		String pattern = "[0-9]{1,}";
		boolean isMatch = Pattern.matches(pattern, condition);
		
		//�����ּ���id��ʱ����ģ����ѯ
		if(isMatch) {
			hql += " or id = " + Integer.parseInt(condition) +" or duration = " + Integer.parseInt(condition);
		}
		
		hql += ")";
		
		query = session.createQuery(hql);
		query.setParameter(0, "%"+condition+"%");
		
		stuExams = (ArrayList<Exam>)query.list();
		
		return stuExams;
	}

}
