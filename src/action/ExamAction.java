package action;

import java.util.ArrayList;

import dao.ExamDao;
import model.Exam;

public class ExamAction {
	//��ʾ��ʾ��Ϣ
	public String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	//exam����
	private Exam exam;
	
	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	//exam��dao����
	private ExamDao examdao;

	public ExamDao getExamdao() {
		return examdao;
	}

	public void setExamdao(ExamDao examdao) {
		this.examdao = examdao;
	}
	
	//Ҫɾ��id�ŵ�����
	String delete_arr;
	
	public String getDelete_arr() {
		return delete_arr;
	}

	public void setDelete_arr(String delete_arr) {
		this.delete_arr = delete_arr;
	}
	
	//�γ�������
	ArrayList<String> lesson_names;
	
	public ArrayList<String> getLesson_names() {
		return lesson_names;
	}

	public void setLesson_names(ArrayList<String> lesson_names) {
		this.lesson_names = lesson_names;
	}
	
	//Ҫɾ���Ŀ���״̬
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	//ɾ�����Ժ���ת��jsp
	private String to_page;

	public String getTo_page() {
		return to_page;
	}

	public void setTo_page(String to_page) {
		this.to_page = to_page;
	}

	//�г����Դ������ԵĿγ�
	public String list_select_course() {
		lesson_names =  examdao.list_select_course_hibernate();
		return "success";
	}
	
	//��������У��
	public String question_num_validation() {
		return examdao.question_num_validation_hibernate(exam);
	}

	//��������
	public String create_exam() {
		examdao.create_exam_hibernate(exam);
		return "success";
	}
	
	//ɾ������
	public String delete_exam() {
		//�зִ�ǰ̨���������ַ�������
		String[] sourceStrArray = delete_arr.split(",");
        
        //����delete_exam��dao����
        examdao.delete_exam_hibernate(sourceStrArray);
		return "success";
	}
	
	//�༭���ԣ����뿼�Ա༭ҳ�棩
	public String edit_exam_one() {
		exam = examdao.edit_exam_one_hibernate(exam);
		lesson_names =  examdao.list_select_course_hibernate();
		return "success";
	}
	
	//�༭���ԣ��޸����ݿ⣩
	public String edit_exam() {
		examdao.edit_exam_hibernate(exam);
		return "success";
	}
	
	//��ֹ����
	public String end_exam() {
		examdao.end_exam_hibernate(exam);
		return "success";
	}
	
	//������ֹ����
	public String ends_exam() {
		//�зִ�ǰ̨���������ַ�������
		String[] sourceStrArray = delete_arr.split(",");
        
        //����delete_exam��dao����
        examdao.ends_exam_hibernate(sourceStrArray);
		return "success";
	}
	
	//��������
	public String restart_exam() {
		examdao.restart_exam_hibernate(exam);
		return "success";
	}
	
	//������ֹ����
	public String restarts_exam() {
		//�зִ�ǰ̨���������ַ�������
		String[] sourceStrArray = delete_arr.split(",");
        
        //����delete_exam��dao����
        examdao.restarts_exam_hibernate(sourceStrArray);
		return "success";
	}
}
