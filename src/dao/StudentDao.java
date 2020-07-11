package dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.opensymphony.xwork2.ActionContext;

import model.Sturesult;
import model.Suggest;
import model.Student;

import tool.ChStr;
import tool.CipherEncryption;

public class StudentDao {
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
	
	//ѧ����¼Dao����
	public String student_login_hibernate(int flag,String id, Student student) {
		//hibernate ��ؽӿڳ�ʼ��
		session = getSession();
		
		//��flagȷ��hql���
		String tem;
		if(flag == 1) {
			tem = "id";
		}
		else {
			tem = "cardNo";
		}
		
		//��ȡ������������
		CipherEncryption cipherEncryption = new CipherEncryption();
		
		//����
		String pwd = cipherEncryption.encryption(student.getPwd());
		
		//hql���
		String hql = "from Student where " + tem + " = ? and pwd = ?";
		Query query=session.createQuery(hql);
		query.setParameter(0, id);
		query.setParameter(1, pwd);
		
		//�ж��Ƿ��и�ѧ��
		if(query.list().size() == 0 ){
			session.close();
			sessionFactory.close();
			return "���������˺Ų�����";
		}
		
		//�жϸ��˺��Ƿ��Ѿ���¼
		Student student1 = (Student)query.list().get(0);
		if(student1.getStatus().equals("����")) {
			session.close();
			sessionFactory.close();
			return "���˺����ڱ𴦵�¼";
		}
		
		//�����˺�״̬Ϊ��¼
		transaction = session.beginTransaction();
		student1.setStatus("����");
		session.update(student1);
		transaction.commit();
		
		//��student��������session�У��Ա��պ�ȡ��
		ActionContext context = ActionContext.getContext();
		Map<String, Object> session_web = context.getSession();
		session_web.put("student", query.list().get(0));
		
		session.close();
		sessionFactory.close();
		return "";
	}
	//ѧ���ɼ���ѯhibernate����
	@SuppressWarnings("deprecation")
	public  ArrayList<Sturesult> studentresult_hibernate(Student student) throws Exception{
		//hibernate ��ؽӿڳ�ʼ��
		configuration = new Configuration().configure();
		sessionFactory= configuration.buildSessionFactory();
		session = sessionFactory.openSession();
		
		//hql����ѯ��ѧ�������гɼ�
		String hql  = "from Sturesult where stuId = ?";	
		Query query = session.createQuery(hql);
		query.setParameter(0, student.getId());
		
		//System.out.println(query.list().size());
		//���ܲ�ѯ���ĳɼ�
		@SuppressWarnings("unchecked")
		ArrayList<Sturesult> studentresults = (ArrayList<Sturesult>)query.list();
		session.close();
		sessionFactory.close();
		return studentresults;
	}
	
	//ѧ��ע��
	public String student_register_hibernate(Student student) {
		session = getSession();
		
		//�ж��Ƿ��Ѿ�ע��
		String hql = "from Student where cardNo = ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, student.getCardNo());
		
		if(query.list().size() != 0) {
			session.close();
			sessionFactory.close();
			return "";
		}
		
		//��ȡ��ǰ����
		java.util.Date date = new java.util.Date(); 
		
		//��ʽ����ǰ����Ϊ�ַ���
		String date_str = new SimpleDateFormat("yyyyMMdd").format(date); 
		
		//���忼����
		String id = "";
		
		//��ѯ��ǰ�������
		hql = "select max(id) from Student";
		query = session.createQuery(hql);
		
		//�Ѿ��п���
		if(query.list().get(0) != null) {
			//��ȡ��ǰ�������
			String max_id = (String)query.list().get(0);
			
			//��ȡ������ŵ�ע������
			String max_date = max_id.substring(2,10);
			
			//��ȡ������ŵ����
			int old_id = Integer.parseInt(max_id.substring(10,16));
			
			//���嵱ǰ�������
			int new_id;
			
			//����û�п���ע��
			if(!max_date.equals(date_str)) {
				new_id = 1;
			}
			//�������п���ע��
			else {
				new_id = old_id + 1;
			}
			
			//�����ɵı�Ÿ�ʽ��Ϊ6λ�ַ���String no = chStr.formatNO(newId, 6);
			ChStr chStr=new ChStr();
			String no = chStr.formatNO(new_id, 6);
			
			//���������׼��֤��(CN + ע������ + ��������ע�����)
			id = "CN" + date_str + no;
		}
		//����һ������ע��ʱ
		else {
			id = "CN" + date_str + "000001";
		}
		
		//��ȡ������������
		CipherEncryption cipherEncryption = new CipherEncryption();
		
		//����
		String pwd = cipherEncryption.encryption(student.getPwd());
		
		student.setPwd(pwd);
		
		//�������Ŵ���studnet�����У��������ݿ�
		java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
		student.setId(id);
		student.setJoinTime(time);
		student.setStatus("����");
		transaction = session.beginTransaction();
		session.save(student);
		transaction.commit();
		
		session.close();
		sessionFactory.close();
		
		//�������Ÿ�ֵ����ʾ��Ϣ
		return id;
	}
	
	//ѧ��ע��
	public void student_logout_hibernate(Student student) {
		//hibernate ��ؽӿڳ�ʼ��
		session = getSession();
		transaction = session.beginTransaction();
		
		student.setStatus("����");
		session.update(student);
		transaction.commit();
		
		session.close();
		sessionFactory.close();
	}
	
	//�һ�����
	public void findPassword(Student student){
		session = this.getSession();
		String hql = "from Student where tel =?";
		Query query=session.createQuery(hql);
		query.setParameter(0, student.getTel());
		Student student1 = (Student)query.uniqueResult();
		
		//��ȡ������������
		CipherEncryption cipherEncryption = new CipherEncryption();
		
		//����
		String pwd = cipherEncryption.encryption(student.getPwd());
		
		student1.setPwd(pwd);
		transaction  = session.beginTransaction();
		session.update(student1);
		transaction.commit();
		session.close();
		sessionFactory.close();
	}
	
	//��֤������ֻ����Ƿ���ȷ
	public boolean validateTel(String tel){
		session=this.getSession();
		String hql = "from Student where tel =?";
		Query query=session.createQuery(hql);
	    query.setParameter(0, tel);
	    Student stu = (Student)query.uniqueResult();
		if(stu==null){
			 return false;
		}else{
			return true;
		}
	}
	
	//�ύ����
	public void feedback(Suggest suggest){
		session=this.getSession();
		transaction  = session.beginTransaction();
		session.save(suggest);
		transaction.commit();
		session.close();
		sessionFactory.close();
	}
	
	
	public void update_student(Student student){
		session = this.getSession();
		String hql="from Student where id= ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, student.getId());
		Student studentone = (Student)query.list().get(0);
	    student.setPwd(studentone.getPwd());
	    
	    session.clear();
        transaction = session.beginTransaction();
		session.update(student);
		transaction.commit();
	}
	public Student studentInfo(Student student){
		session = this.getSession();
		String hql="from Student where id= ?";
		Query query = session.createQuery(hql);
		query.setParameter(0, student.getId());
		Student studentone = (Student)query.list().get(0);
		return studentone;
	}


}
