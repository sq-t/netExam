package action;

import java.util.ArrayList;

import dao.StuResultDao;
import model.*;

public class StuResultAction {
	//ѧ���ɼ�dao����
	private StuResultDao sturesultdao;

	public StuResultDao getSturesultdao() {
		return sturesultdao;
	}

	public void setSturesultdao(StuResultDao sturesultdao) {
		this.sturesultdao = sturesultdao;
	}
	
	//Ҫ�����Ŀ��Զ���
	private Exam exam;
	
	
	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}
	
	//ѧ���ɼ�����
	private ArrayList<Sturesult> sturesults;
	
	public ArrayList<Sturesult> getSturesults() {
		return sturesults;
	}

	public void setSturesults(ArrayList<Sturesult> sturesults) {
		this.sturesults = sturesults;
	}
	
	//�γ̳ɼ�����ͳ�ƶ���
	private AnalysisGrade analysisGrade;

	public AnalysisGrade getAnalysisGrade() {
		return analysisGrade;
	}

	public void setAnalysisGrade(AnalysisGrade analysisGrade) {
		this.analysisGrade = analysisGrade;
	}

	//�ɼ�����
	public String analysis_grade() {
		exam = sturesultdao.total_by_exam_id(exam);
		sturesults = sturesultdao.anlysis_grade_hibernate(exam);
		analysisGrade = sturesultdao.analysis_sturesult(sturesults);
		
		return "success";
	}
	
}
