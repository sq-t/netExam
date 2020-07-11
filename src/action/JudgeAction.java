package action;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Judgequestion;
import dao.JudgeDao;
public class JudgeAction{
	//��ת��jsp
	private String to_page;

	public String getTo_page() {
		return to_page;
	}

	public void setTo_page(String to_page) {
		this.to_page = to_page;
	}
	//question��dao����
	private Judgequestion judge;
	
	public Judgequestion getJudge() {
		return judge;
	}
	
	File list_add_judge;

	public File getList_add_judge() {
		return list_add_judge;
	}

	public void setList_add_judge(File list_add_judge) {
		this.list_add_judge = list_add_judge;
	}

	public void setJudge(Judgequestion judge) {
		this.judge = judge;
	}

	private JudgeDao judgedao;
	
	public JudgeDao getJudgedao() {
		return judgedao;
	}

	public void setJudgedao(JudgeDao judgedao) {
		this.judgedao = judgedao;
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
    String easy;

    public String getEasy() {
		return easy;
	}

	public void setEasy(String easy) {
		this.easy = easy;
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
			lesson_names =  judgedao.list_select_course_hibernate();
			return "success";
		}
		
	//�������
	public String create_judge() {
		judgedao.create_judge_hibernate(judge);
			return "success";
		}
		
	//ɾ��question
	public String delete_judge() {
			//�зִ�ǰ̨���������ַ�������
			String[] sourceStrArray = delete_arr.split(",");
	        
	        //����delete_question��dao����
			judgedao.delete_judge_hibernate(sourceStrArray);
			return "success";
	}
	//�༭ѡ���⣨����ѡ����༭ҳ�棩
	public String edit_judge_one() {
		    judge = judgedao.edit_judge_one_hibernate(judge);
			lesson_names =  judgedao.list_select_course_hibernate();
			return "success";
		}
		
	//�༭ѡ���⣨�޸����ݿ⣩
	public String edit_judge() {
		    judgedao.edit_judge_hibernate(judge);
			return "success";
		}
	public String list_add_judge(){
		//�õ���������е�����
        List<Judgequestion> listExcel=judgedao.getAllByExcel(list_add_judge);
        //�õ����ݿ�������е�����
        for (Judgequestion judge : listExcel) {
            String title=judge.getQuestion();
            Long id = judgedao.isExist(title);
            if (id == -1) {
                //�����ھ����
            	judgedao.listAddJudgeHibernate(judge);
            }else {
                //���ھ͸���
            	judge.setId(id);
            	judgedao.listUpdateJudgeHibernate(judge);}
        }
        return "success";
	}
}
