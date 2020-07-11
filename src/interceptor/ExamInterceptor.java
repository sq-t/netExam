package interceptor;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

import action.ExamAction;

public class ExamInterceptor extends AbstractInterceptor{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String intercept(ActionInvocation arg0) throws Exception {
		
		//��ȡExamAction����
		ExamAction examAction = (ExamAction)arg0.getAction();
		
		//ִ��examAction��У�鷽��
		String message = examAction.question_num_validation();
		
		//���У�������⣬����ִ��
		if(message.equals("")) {
			return arg0.invoke();
		}
		
		//У�������⣬���ص�ǰҳ
		examAction.setMessage(message);
		return Action.INPUT;
	}
}
