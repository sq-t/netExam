package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Exam;
import model.Lesson;
import model.Sturesult;

public class LessonDao {
	private Configuration configuration;
	private SessionFactory sessionFactory;
	private Session session;
	private Transaction transaction;
	
	private ArrayList<Lesson> lessons;
	
    public ArrayList<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(ArrayList<Lesson> lessons) {
		this.lessons = lessons;
	}

	private Lesson lesson;
    
	public Lesson getLesson() {
		return lesson;
	}

	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}

	@SuppressWarnings("deprecation")
	public Session getSession() {
		configuration = new Configuration().configure();
		sessionFactory = configuration.buildSessionFactory();
		session = sessionFactory.openSession();
		return session;
	}

	public void add_hibernate(Lesson lesson) {
		session = this.getSession();
		transaction = session.beginTransaction();
		lesson.setJoinTime(new Timestamp(System.currentTimeMillis()));
		session.save(lesson);
		transaction.commit();
		
	}

	public String add_validate(String name) {
		session = this.getSession();
		// transaction = session.beginTransaction();
		Query query = session.createQuery("from Lesson where name=?");
		query.setString(0, name);
		Lesson lesson = (Lesson) query.uniqueResult();
		if (lesson == null) {
			return "";
		} else {
			return "�ÿγ��Ѿ����ڣ�";
		}
	}

	// ģ����ѯ��ʾ���е�Lesson����
	@SuppressWarnings("unchecked")
	public ArrayList<Lesson> list_lesson(String condition,int totalUsers) {
		// ��ȡhibernate��session����
		session = this.getSession();
		
		// ���conditionΪnull������Ϊ""
		if (condition == null) {
			condition = "";
		}
		
		String hql = "from Lesson";
		
		Query query = session.createQuery(hql);
		 
		if(query.list().size() != totalUsers && totalUsers != -1) {
			return lessons;
		}

		hql = "from Lesson where name like  ? ";

		// ��֤condition�Ƿ�Ϊ������
		String pattern = "[0-9]{1,}";
		boolean isMatch = Pattern.matches(pattern, condition);

		// �����ּ���id��ʱ����ģ����ѯ(
		if (isMatch) {
			hql += " or  id = " + Integer.parseInt(condition) + " ";
		}

		query = session.createQuery(hql);
		query.setParameter(0, "%" + condition + "%");
		lessons = (ArrayList<Lesson>)query.list();
		return lessons;
	}
//�޸Ŀγ�
	public void edit_lesson(Lesson lesson) {
		//��ȡhibernate��session����
		session = this.getSession();
		
		String hql = "from Lesson where id = ?";
		
		Query query = session.createQuery(hql);
		query.setParameter(0, lesson.getId());
		
		Lesson lesson1 = (Lesson)query.list().get(0);
		
		lesson1.setName(lesson.getName());
		//��ȡ�������
		transaction = session.beginTransaction();
		
		session.update(lesson1);
		
		//�ύ����
		transaction.commit();
	}

	@SuppressWarnings("unchecked")
	public void delete_lesson_hibernate(String[] sourceStrArray) {
		//��ȡhibernate��session����
		session = this.getSession();
		String hql;
		Query query;
		ArrayList<Exam> exams;
		ArrayList<Sturesult> sturesults;
		Sturesult sturesult;
		Exam exam;
		Lesson lesson = new Lesson();
		for(int i = 0;i < sourceStrArray.length;i++) {
			//��ȡ�������
			transaction = session.beginTransaction();
			lesson.setId(Long.parseLong(sourceStrArray[i]));
			session.delete(lesson);
			//�ύ����
			transaction.commit();
			
			//��ѯҪɾ�������п���
			hql = "from Exam where lesson.id = " + Long.parseLong(sourceStrArray[i]);
			query = session.createQuery(hql);
			exams = (ArrayList<Exam>)query.list();
			
			//ɾ�������п���
			for(int j = 0;j < exams.size();j++) {
				exam = exams.get(j);
				//��ȡ�������
				transaction = session.beginTransaction();
				session.delete(exam);
				//�ύ����
				transaction.commit();
				
				//��ѯҪɾ���Ŀ������гɼ�
				hql = "from Sturesult where exam.id = " + exam.getId();
				query = session.createQuery(hql);
				sturesults = (ArrayList<Sturesult>)query.list();
				
				//ɾ���ÿ������гɼ�
				for(int z = 0;z < sturesults.size();z++) {
					sturesult = sturesults.get(z);
					//��ȡ�������
					transaction = session.beginTransaction();
					session.delete(sturesult);
					//�ύ����
					transaction.commit();
				}
			}
		}
	}
}
