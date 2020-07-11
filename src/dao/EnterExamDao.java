package dao;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Exam;
import model.Judgequestion;
import model.Optionalquestion;
import model.Student;
import model.Sturesult;

public class EnterExamDao {
	
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
		
		public Exam findExam(Exam exam) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			Exam exam1 = (Exam)session.get(Exam.class, exam.getId());
			
			return exam1;
		}
		@SuppressWarnings("unchecked")
		public ArrayList<Judgequestion> judge(Long lessonId,String level){
			ArrayList<Judgequestion> judge;
			String hql="from Judgequestion where lesson.id=? and level=?";
			session = this.getSession();
			Query query = session.createQuery(hql);
			query.setParameter(0, lessonId);
			query.setParameter(1, level);
			judge=(ArrayList<Judgequestion>)query.list();
			return judge;
		}
		@SuppressWarnings("unchecked")
		public ArrayList<Optionalquestion> option(Long lessonId,String type,String level){
			ArrayList<Optionalquestion> option;
			String hql="from Optionalquestion where lesson.id=? and type=? and level=?";
			session = this.getSession();
			Query query = session.createQuery(hql);
			query.setParameter(0, lessonId);
			query.setParameter(1, type);
			query.setParameter(2, level);
			option=(ArrayList<Optionalquestion>)query.list();
			return option;
		}
		
		//У�鿼���Ƿ��Ѿ����й��ÿ���
		public String sturesult_validation(Student student, Exam exam) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			String hql = "from Sturesult where exam.id = ? and student.id = ?";
			
			Query query = session.createQuery(hql);
			query.setParameter(0, exam.getId());
			query.setParameter(1, student.getId());
			
			if(query.list().size() != 0) {
				return "���Ѳμӹ��ÿ���--"+ exam.getLesson().getName() +"��";
			}
			return "";
		}
		
		//���㵥ѡ�����
		public Double single_score(String[] singleStrArray) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			double trueNum = 0;
			
			String hql;
			Query query;
			
			String[] single;
			
			for(int i = 0;i < singleStrArray.length;i++) {
				single = singleStrArray[i].split("-");
				hql = "from Optionalquestion where answer = ? and id = ?";
				query = session.createQuery(hql);
				query.setParameter(0, single[0].trim());
				query.setParameter(1, Long.parseLong(single[1]));
				
				if(query.list().size() != 0) {
					trueNum++;
				}
			}
			
			return trueNum;
		}

		//�����ѡ�����
		public Double more_score(String[] moreStrArray) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			double trueNum = 0;
			
			String hql;
			Query query;
			
			String[] more;
			
			for(int i = 0;i < moreStrArray.length;i++) {
				more = moreStrArray[i].split("-");
				hql = "from Optionalquestion where answer = ? and id = ?";
				query = session.createQuery(hql);
				query.setParameter(0, more[0].trim());
				query.setParameter(1, Long.parseLong(more[1]));
				
				if(query.list().size() != 0) {
					trueNum++;
				}
			}
			
			return trueNum;
		}

		//�����ж������
		public Double judge_score(String[] judgeStrArray) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			double trueNum = 0;
			
			String hql;
			Query query;
			
			String[] single;
			
			for(int i = 0;i < judgeStrArray.length;i++) {
				single = judgeStrArray[i].split("-");
				hql = "from Judgequestion where answer = ? and id = ?";
				query = session.createQuery(hql);
				query.setParameter(0, single[0].trim());
				query.setParameter(1, Long.parseLong(single[1]));
				
				if(query.list().size() != 0) {
					trueNum++;
				}
			}
			
			return trueNum;
		}

		//����ѧ���ɼ�
		public void insert_sturesult(Sturesult sturesult) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			transaction = session.beginTransaction();
			session.save(sturesult);
			transaction.commit();
			
		}

		@SuppressWarnings("unused")
		public String[] single_answer(String[] singleStrArray) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			int len = singleStrArray.length;
			String hql;
			Query query;
			String[] single;
			String[] singleAnswers = new String[len];
			
			for(int i = 0;i < singleStrArray.length;i++) {
				single = singleStrArray[i].split("-");
				
				Optionalquestion optionalquestion = (Optionalquestion)session.get(Optionalquestion.class, Long.parseLong(single[1]));
				
				singleAnswers[i] = optionalquestion.getAnswer();
			}
			return singleAnswers;
		}

		@SuppressWarnings("unused")
		public String[] more_answer(String[] moreStrArray) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			int len = moreStrArray.length;
			String hql;
			Query query;
			String[] more;
			String[] moreAnswers = new String[len];
			
			for(int i = 0;i < moreStrArray.length;i++) {
				more = moreStrArray[i].split("-");
				
				Optionalquestion optionalquestion = (Optionalquestion)session.get(Optionalquestion.class, Long.parseLong(more[1]));
				
				moreAnswers[i] = optionalquestion.getAnswer();
			}
			return moreAnswers;
		}

		@SuppressWarnings("unused")
		public String[] judge_answer(String[] judgeStrArray) {
			//��ȡhibernate��session����
			session = this.getSession();
			
			int len = judgeStrArray.length;
			String hql;
			Query query;
			String[] judge;
			String[] judgeAnswers = new String[len];
			
			for(int i = 0;i < judgeStrArray.length;i++) {
				judge = judgeStrArray[i].split("-");
				
				Judgequestion judgequestion = (Judgequestion)session.get(Judgequestion.class, Long.parseLong(judge[1]));
				
				judgeAnswers[i] = judgequestion.getAnswer();
			}
			return judgeAnswers;
		}

		

}
