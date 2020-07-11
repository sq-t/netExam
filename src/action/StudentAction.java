package action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

import com.opensymphony.xwork2.ActionContext;

import dao.StudentDao;
import model.Sturesult;
import model.Suggest;
import model.Student;

import util.RandomSixNumber;
import util.SendMessage;

public class StudentAction {
	private static final long serialVersionUID = 1L;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	//���շ���ҳ��ı��������
	private Suggest suggest;
	private String title;         //����
	private String content;  //����
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public Suggest getSuggest() {
		return suggest;
	}

	public void setSuggest(Suggest suggest) {
		this.suggest = suggest;
	}
	
	//��ʾ��ʾ��Ϣ
	private String message = "";
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	//����loginҳ������֤�Ż�����
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	//
	private String cardNo;
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	//
	private String pwd;
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	//
	private String tel;
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}


	//ͼƬ��֤��
	private String captchaText;
	public String getCaptchaText() {
		return captchaText;
	}

	public void setCaptchaText(String captchaText) {
		this.captchaText = captchaText;
	}
	
	//�ֻ���֤��
	private String vCode;
	
	public String getvCode() {
		return vCode;
	}

	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	
	//ѧ������
	private Student student;
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
	
	//ѧ��Dao����
	private StudentDao studentdao;
	/*
	public StudentDao getStudentdao() {
		return studentdao;
	}
	*/
	public void setStudentdao(StudentDao studentdao) {
		this.studentdao = studentdao;
	}
	
	//ѧ���ɼ�
	private Sturesult studentresult;
	
	public Sturesult getStudentresult() {
		return studentresult;
	}

	public void setStudentresult(Sturesult studentresult) {
		this.studentresult = studentresult;
	}
	
	//ѧ���ɼ�����
	private ArrayList<Sturesult> studentresults;
	
	public ArrayList<Sturesult> getStudentresults() {
		return studentresults;
	}

	public void setStudentresults(ArrayList<Sturesult> studentresults) {
		this.studentresults = studentresults;
	}

	//ѧ����¼
	public String student_login() {
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		
		Student student1 = (Student)session.get("student");
		if(student1 != null) {
			return "success";
		}
		//��֤���֤�ŵ�������ʽ
		String pattern = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
		boolean isMatch = Pattern.matches(pattern, id);
		//flag ��ʾ id �ǿ����� �������֤��
		int flag;
		if(!isMatch) {
			student.setId(id);
			//flag = 1 ��ʾid�ǿ�����
			flag = 1;
		}
		else {
			student.setCardNo(id);
			//flag = 2 ��ʾid�����֤��
			flag = 2;
		}
		message = studentdao.student_login_hibernate(flag,id,student);
		if(message.equals("")) {
			return "success";
		}
		return "error";
	}
	
	//ѧ���˳���¼
	public String student_logout() {
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		Student studentone = (Student)session.get("student");
		student =(Student)studentdao.studentInfo(studentone);
		studentdao.student_logout_hibernate(student);
		session.remove("student");
		return "success";
	}
	
	//ѧ��ע��
	public String student_register() {
		message = studentdao.student_register_hibernate(student);
		if(!message.equals("")) {
			return "success";
		}
		message = "�������֤������ע�ᣬ��ֱ�ӵ�¼";
		return "error";
	}
	
	//ע��ɹ�����ʾ���ɵĿ����š�
	public String register_success() {
		return "success";
	}
	
	//�һ�����
	public String findPassword(){
		ActionContext context = ActionContext.getContext();
		Map<String, Object> session = context.getSession();
		String str = (String) session.get("vCode"); 
		if(str.equals(vCode)){
			studentdao.findPassword(student);
			message = student.getPwd();
			return "success";
		}else{
			message = "�ֻ���֤�벻��ȷ";
			return "error";
		}
		
	}
	
	//�����ֻ���֤��
	public String sendVCode(){
		if (tel != null)
			System.out.println(tel);
		if (studentdao != null) {
			if(studentdao.validateTel(tel)){
				String vCode = RandomSixNumber.randomSixNumber();
				System.out.println(vCode);
				ActionContext context = ActionContext.getContext();
				Map<String, Object> session = context.getSession();
				session.put("vCode", vCode);
				flag = true;
				SendMessage.sendAMessage(tel, vCode);
			}
		}else{
			System.out.println("dao is null");
			flag = false;
		}
		return "success";
	}
	
	private boolean flag;
	//�ж��Ƿ����ֻ���֤��
	public boolean isFlag() {
		return flag;
	}
	
	
	//��ʾѧ����ҳ
	public String list_student_message() {
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		Student studentone = (Student)session.get("student");
		student =(Student)studentdao.studentInfo(studentone);
		return "success";
	}
	
	//��ѯ�ɼ�
	public String studentresult() throws Exception{
		ActionContext context = ActionContext.getContext();
		Map<String, Object> session = context.getSession();
		student = (Student)session.get("student");
		studentresults = new ArrayList<Sturesult>();
		studentresults = studentdao.studentresult_hibernate(student);
		return "success";
	}
	
	//���淴����Ϣ
	public String save_feedback(){
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		Suggest suggest = new Suggest();
		student = (Student)session.get("student");
		suggest.setStudent(student);
		suggest.setTime(new Timestamp(System.currentTimeMillis()));
		suggest.setContent(content);
		suggest.setTitle(title);
		studentdao.feedback(suggest);
		message="��л���ı��������";
		return "success";
	}
	
	
	public String studentInfo(){
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		Student studentone = (Student)session.get("student");
		student =(Student)studentdao.studentInfo(studentone);
		return "success";
	}
	public String studentJump(){
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		Student studentone = (Student)session.get("student");
		student =(Student)studentdao.studentInfo(studentone);
		return "success";
	}
	public String studentupdate(){
		
		studentdao.update_student(student);
		return "success";
	}
	
}
