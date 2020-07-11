package action;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Lesson;
import model.Optionalquestion;
import dao.QuestionDao;
public class QuestionAction{
	//��ת��jsp
	private String to_page;

	public String getTo_page() {
		return to_page;
	}

	public void setTo_page(String to_page) {
		this.to_page = to_page;
	}
	//question��dao����
	private Optionalquestion question;
	
	public Optionalquestion getQuestion() {
		return question;
	}

	public void setQuestion(Optionalquestion question) {
		this.question = question;
	}
    File list_add_question;
    
	
	public File getList_add_question() {
		return list_add_question;
	}

	public void setList_add_question(File list_add_question) {
		this.list_add_question = list_add_question;
	}
	private QuestionDao questiondao;

	public QuestionDao getQuestiondao() {
		return questiondao;
	}

	public void setQuestiondao(QuestionDao questiondao) {
		this.questiondao = questiondao;
	}
	//ɾ��id������
	String delete_arr;
		
	public String getDelete_arr() {
			return delete_arr;
		}

	public void setDelete_arr(String delete_arr) {
			this.delete_arr = delete_arr;
		}
	//���ҵ�ѡ��
    String single;

		public String getSingle() {
		return single;
	}

	public void setSingle(String single) {
		this.single = single;
	}
    ArrayList<String> lesson_names;
	
	public ArrayList<String> getLesson_names() {
		return lesson_names;
	}

	public void setLesson_names(ArrayList<String> lesson_names) {
		this.lesson_names = lesson_names;
	}
	//�г����Դ������ԵĿγ�
	public String list_select_course() {
			lesson_names =  questiondao.list_select_course_hibernate();
			return "success";
		}
		
	//�������
	public String create_question() {
		questiondao.create_question_hibernate(question);
			return "success";
		}
		
	//ɾ��question
	public String delete_question() {
			//�зִ�ǰ̨���������ַ�������
			String[] sourceStrArray = delete_arr.split(",");
	        
	        //����delete_question��dao����
	        questiondao.delete_question_hibernate(sourceStrArray);
			return "success";
	}
	//�༭ѡ���⣨����ѡ����༭ҳ�棩
	public String edit_question_one() {
		    question = questiondao.edit_question_one_hibernate(question);
			lesson_names =  questiondao.list_select_course_hibernate();
			return "success";
		}
		
	//�༭ѡ���⣨�޸����ݿ⣩
	public String edit_question() {
		    questiondao.edit_question_hibernate(question);
			return "success";
		}
	@SuppressWarnings("unused")
	public String list_add_question(){
		//�õ���������е�����
        List<Optionalquestion> listExcel=questiondao.getAllByExcel(list_add_question);
        //�õ����ݿ�������е�����
        for (Optionalquestion question : listExcel) {
        	Lesson lesson = question.getLesson();
            String title=question.getQuestion();
            Long id = questiondao.isExist(title);
            if (id == -1) {
                //�����ھ����
            	questiondao.listAddQuestionHibernate(question);
            }else {
                //���ھ͸���
            	question.setId(id);
            	questiondao.listUpdateQuestionHibernate(question);}
        }
        return "success";
	}
	
	@SuppressWarnings("unused")
	private String singleArray;
	@SuppressWarnings("unused")
	private String moreArray;
	@SuppressWarnings("unused")
	private String judgeArray;
	
	public void setSingleArray(String singleArray) {
		this.singleArray = singleArray;
	}

	public void setMoreArray(String moreArray) {
		this.moreArray = moreArray;
	}

	public void setJudgeArray(String judgeArray) {
		this.judgeArray = judgeArray;
	}

	public String test() {
		return "success";
	}
	
}
