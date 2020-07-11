package action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;

import dao.ManagerDao;
import model.Manager;
import model.Suggest;

public class ManagerAction {
	private Suggest suggest;
	public Suggest getSuggest() {
		return suggest;
	}

	public void setSuggest(Suggest suggest) {
		this.suggest = suggest;
	}
	
	//Ҫɾ��id�ŵ�����
	String delete_arr;
	public String getDelete_arr() {
		return delete_arr;
	}

	public void setDelete_arr(String delete_arr) {
		this.delete_arr = delete_arr;
	}
	
	//��ʾ��ʾ��Ϣ
	private String message = "";
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
		
	//����Ա����
	Manager manager;
	
	public Manager getManager() {
		return manager;
	}


	public void setManager(Manager manager) {
		this.manager = manager;
	}


	//����Աdao����
	ManagerDao managerdao;

	public ManagerDao getManagerdao() {
		return managerdao;
	}


	public void setManagerdao(ManagerDao managerdao) {
		this.managerdao = managerdao;
	}
	
	//����Ա��¼
	public String manager_login() {
		message = managerdao.manager_login_hibernate(manager);
		if(message.equals("")) {
			return "success";
		}
		return "error";
	}
	
	//����Ա�˳���¼
	public String manager_logout() {
		ActionContext context = ActionContext.getContext();
		Map<String,Object> session = context.getSession();
		session.remove("manager");
		return "success";
	}
	
	//��ʾ����Ա��ҳ
	public String list_manager_message() {
		return "success";
	}
	
	// ɾ����������
	public String delete_feedback() {
		// �зִ�ǰ̨���������ַ�������
		String[] sourceStrArray = delete_arr.split(",");
		managerdao.delete_feedback(sourceStrArray);
		return "success";
	}

	public String suggest_content() {
		suggest = managerdao.suggest_content(suggest);
		return "success";
	}
	
}
