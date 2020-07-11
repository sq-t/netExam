package action;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.ActionContext;

import dao.EnterExamDao;
import model.Exam;
import model.Judgequestion;
import model.Optionalquestion;
import model.Student;
import model.Sturesult;

public class StartExamAction {
	private String message;

	private Exam exam;
	private double[] score;//���ڴ�Ÿ����͵ķ���
	private ArrayList<Optionalquestion> single=new ArrayList<Optionalquestion>();//���ڴ�ŵ�ѡ��
	private ArrayList<Optionalquestion> multi=new ArrayList<Optionalquestion>();//���ڴ�Ŷ�ѡ��
	private ArrayList<Judgequestion> judge=new ArrayList<Judgequestion>();//���ڴ���ж���
	private EnterExamDao enterExamDao;
	
	private String exam_id;
	private String singleArray;
	private String moreArray;
	private String judgeArray;
	private Sturesult sturesult;
	private double totalScore;
	
	private String[] singleAnswers;
	private String[] moreAnswers;
	private String[] judgeAnswers;
	
	public String[] getSingleAnswers() {
		return singleAnswers;
	}

	public String[] getMoreAnswers() {
		return moreAnswers;
	}

	public String[] getJudgeAnswers() {
		return judgeAnswers;
	}

	public double getTotalScore() {
		return totalScore;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setExam_id(String exam_id) {
		this.exam_id = exam_id;
	}
	
	@JSON(serialize=false)
	public Sturesult getSturesult() {
		return sturesult;
	}

	public void setSturesult(Sturesult sturesult) {
		this.sturesult = sturesult;
	}

	public void setSingleArray(String singleArray) {
		this.singleArray = singleArray;
	}

	public void setMoreArray(String moreArray) {
		this.moreArray = moreArray;
	}

	public void setJudgeArray(String judgeArray) {
		this.judgeArray = judgeArray;
	}

	@JSON(serialize=false)
	public EnterExamDao getEnterExamDao() {
		return enterExamDao;
	}

	public void setEnterExamDao(EnterExamDao enterExamDao) {
		this.enterExamDao = enterExamDao;
	}

	@JSON(serialize=false)
	public ArrayList<Optionalquestion> getSingle() {
		return single;
	}

	public void setSingle(ArrayList<Optionalquestion> single) {
		this.single = single;
	}

	@JSON(serialize=false)
	public ArrayList<Optionalquestion> getMulti() {
		return multi;
	}

	public void setMulti(ArrayList<Optionalquestion> multi) {
		this.multi = multi;
	}

	@JSON(serialize=false)
	public ArrayList<Judgequestion> getJudge() {
		return judge;
	}

	public void setJudge(ArrayList<Judgequestion> judge) {
		this.judge = judge;
	}
	
	@JSON(serialize=false)
	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	@JSON(serialize=false)
	public double[] getScore() {
		return score;
	}

	public void setScore(double[] score) {
		this.score = score;
	}

	//�ݴ��������
	ArrayList<Judgequestion> judgesimple;
	ArrayList<Judgequestion> judgemiddle;
	ArrayList<Judgequestion> judgehard;
	ArrayList<Optionalquestion> optionsinglesimple;
	ArrayList<Optionalquestion> optionsinglemiddle;
	ArrayList<Optionalquestion> optionsinglehard;
	ArrayList<Optionalquestion> optionmultisimple;
	ArrayList<Optionalquestion> optionmultimiddle;
	ArrayList<Optionalquestion> optionmultihard;
	//ÿ����ĸ���
	int jsnum,jmnum,jhnum,ossnum,osmnum,oshnum,omsnum,ommnum,omhnum;
	
	protected void setNum(double d1,double d2){
		jsnum=(int)(exam.getJudgeNum()*d1);
		jhnum=(int)(exam.getJudgeNum()*d2);
		jmnum=exam.getJudgeNum()-jsnum-jhnum;
		ossnum=(int)(exam.getSingleNum()*d1);
		oshnum=(int)(exam.getSingleNum()*d2);
		osmnum=exam.getSingleNum()-ossnum-oshnum;
		omsnum=(int)(exam.getMoreNum()*d1);
		omhnum=(int)(exam.getMoreNum()*d2);
		ommnum=exam.getMoreNum()-omsnum-omhnum;
	}
	protected void getList(){
		judgesimple=enterExamDao.judge(exam.getLesson().getId(), "��");
		judgemiddle=enterExamDao.judge(exam.getLesson().getId(), "�е�");
		judgehard=enterExamDao.judge(exam.getLesson().getId(), "����");
		optionsinglesimple=enterExamDao.option(exam.getLesson().getId(), "��ѡ��", "��");
		optionsinglemiddle=enterExamDao.option(exam.getLesson().getId(), "��ѡ��", "�е�");
		optionsinglehard=enterExamDao.option(exam.getLesson().getId(), "��ѡ��", "����");
		optionmultisimple=enterExamDao.option(exam.getLesson().getId(), "��ѡ��", "��");
		optionmultimiddle=enterExamDao.option(exam.getLesson().getId(), "��ѡ��", "�е�");
		optionmultihard=enterExamDao.option(exam.getLesson().getId(), "��ѡ��", "����");
	}
	//��������ѡ�����������Ծ�
	@SuppressWarnings("null")
	protected void generate(){
		Random random=new Random();
		
		//��������ж���
		int indexs[] = new int[100];
		for(int i=0;i<jsnum;){
			int index=random.nextInt(judgesimple.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				judge.add(judgesimple.get(index));
			}
		}
		indexs = new int[100];
		for(int i=0;i<jmnum;){
			int index=random.nextInt(judgemiddle.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				judge.add(judgemiddle.get(index));
			}
		}
		indexs = new int[100];
		for(int i=0;i<jhnum;){
			int index=random.nextInt(judgehard.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				judge.add(judgehard.get(index));
			}
		}
		//������ɵ�ѡ��
		indexs = new int[100];
		for(int i=0;i<ossnum;){
			int index=random.nextInt(optionsinglesimple.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				single.add(optionsinglesimple.get(index));
			}
		}
		indexs = new int[100];
		for(int i=0;i<osmnum;){
			int index=random.nextInt(optionsinglemiddle.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				single.add(optionsinglemiddle.get(index));
			}
		}
		indexs = new int[100];
		for(int i=0;i<oshnum;){
			int index=random.nextInt(optionsinglehard.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				single.add(optionsinglehard.get(index));
			}
		}
		//������ɶ�ѡ��
		indexs = new int[100];
		for(int i=0;i<omsnum;){
			int index=random.nextInt(optionmultisimple.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				multi.add(optionmultisimple.get(index));
			}
		}
		indexs = new int[100];
		for(int i=0;i<ommnum;){
			int index=random.nextInt(optionmultimiddle.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				multi.add(optionmultimiddle.get(index));
			}
		}
		indexs = new int[100];
		for(int i=0;i<omhnum;){
			int index=random.nextInt(optionmultihard.size());
			int j=0;
			for(;j<i;j++) {
				if(indexs[j] == index) {
					break;
				}
			}
			if(j>=i) {
				indexs[i++]=index;
				multi.add(optionmultihard.get(index));
			}
		}
	}
	public String startExam(){
		setExam(enterExamDao.findExam(getExam()));
		double[] s=new double[3];
		s[0]=exam.getSingleScore()*exam.getSingleNum();
		s[1]=exam.getMoreScore()*exam.getMoreNum();
		s[2]=exam.getJudgeScore()*exam.getJudgeNum();
		setScore(s);
		
		getList();
		if(exam.getLevel().equals("��")){
			setNum(0.7,0.1);
			generate();
		}
		if(exam.getLevel().equals("�е�")){
			setNum(0.6,0.2);
			generate();
		}
		if(exam.getLevel().equals("����")){
			setNum(0.5,0.3);
			generate();
		}
		return "success";
	}
	
	//У�鿼���Ƿ��Ѿ����й��ÿ���
	public String sturesult_validation() {
		
		//����exam_id���exam����
		setExam(enterExamDao.findExam(getExam()));
		
		//�õ�session
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		//��ȡsession��student
		Student student = (Student)session.get("student");
		
		message = enterExamDao.sturesult_validation(student,exam);
		
		return "success";
	}
	
	//�ύ�����
	public String submit_exam() {
		//�зָ����͵Ĵ�
		String[] singleStrArray = singleArray.split(",");
		String[] moreStrArray = moreArray.split(",");
		String[] judgeStrArray = judgeArray.split(",");
		
		//��ʼ��exam
		exam = new Exam();
		//����exam_id���exam����
		exam.setId(Long.parseLong(exam_id));
		setExam(enterExamDao.findExam(getExam()));
		
		//�õ�session
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		//��ȡsession��student
		Student student = (Student)session.get("student");
		
		//��ȡ��ǰʱ��
		java.sql.Timestamp time = new java.sql.Timestamp(System.currentTimeMillis());
		
		//��ѯ���ݿ�õ�����
		double singleScore,moreScore,jusgeScore;
		singleScore = exam.getSingleScore() * enterExamDao.single_score(singleStrArray);
		moreScore = exam.getMoreScore() * enterExamDao.more_score(moreStrArray);
		jusgeScore = exam.getJudgeScore() * enterExamDao.judge_score(judgeStrArray);
		
		singleAnswers = enterExamDao.single_answer(singleStrArray);
		moreAnswers = enterExamDao.more_answer(moreStrArray);
		judgeAnswers = enterExamDao.judge_answer(judgeStrArray);
		
		totalScore = singleScore + moreScore + jusgeScore;
		//��ʼ��sturesult
		sturesult = new Sturesult();
		
		sturesult.setExam(exam);
		sturesult.setStudent(student);
		sturesult.setJoinTime(time);
		sturesult.setResSingle(singleScore);
		sturesult.setResMore(moreScore);
		sturesult.setResJudge(jusgeScore);
		sturesult.setResTotal(totalScore);
		
		enterExamDao.insert_sturesult(sturesult);
		
		return "success";
	}

	

}
