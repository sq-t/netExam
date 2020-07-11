package action;


import java.util.ArrayList;

import dao.LessonDao;
import model.Lesson;

public class LessonAction {
		
	private ArrayList<Lesson> lessons ;
	private String message="";
	private Lesson lesson;
	private LessonDao lessondao;
	private String condition = "";
	private boolean submitted = false;
	//Ҫɾ��id�ŵ�����
	String delete_arr;
	
	public String getDelete_arr() {
		return delete_arr;
	}

	public void setDelete_arr(String delete_arr) {
		this.delete_arr = delete_arr;
	}
	public ArrayList<Lesson> getLessons() {
		return lessons;
	}
	public void setLessons(ArrayList<Lesson> lessons) {
		this.lessons = lessons;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Lesson getLesson() {
		return lesson;
	}
	public void setLesson(Lesson lesson) {
		this.lesson = lesson;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public boolean isSubmitted() {
		return submitted;
	}
	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
	public void setLessondao(LessonDao lessondao) {
		this.lessondao = lessondao;
	}
	//��ӿγ�
	public String add_lesson() {
		message = lessondao.add_validate(lesson.getName());
		if(message.equals("")){
			lessondao.add_hibernate(lesson);
			return "success";
		}else{
			return "error";
		}
    }
	
	public String edit_lesson_one(){
		return "success";
	}
	//�޸Ŀγ�
	public String edit_lesson(){
		lessondao.edit_lesson(lesson);
		return "success";
	}
	
	//ɾ���γ�
	public String delete_lesson(){
		//�зִ�ǰ̨���������ַ�������
		String[] sourceStrArray = delete_arr.split(",");
        
        //����delete_exam��dao����
		lessondao.delete_lesson_hibernate(sourceStrArray);
		return "success";
	}
	
}
