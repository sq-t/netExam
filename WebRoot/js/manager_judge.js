function $(id) {
    return document.getElementById(id);
}

//搜索框得到焦点，改变其样式
function search_onfcus() {
	document.getElementById("condition").className = "tool-search-active";
}

//搜索框失去焦点，改变其样式
function search_onblur() {
	document.getElementById("condition").className = "tool-search-input";
}

//点击查询图片，实现查询
function search() {
	location.href = "manager_judge.servlet?condition=" + document.getElementById("condition").value;
}

//跳转至指定的分页
function jmp_page() {
	var jmp_page = parseInt(document.getElementById("jmp_page").value);
	var totalUsers = parseInt(document.getElementById("totalUsers").value);
	var totalPages = parseInt(document.getElementById("totalPages").value);
	if(jmp_page <= totalPages && jmp_page > 0) {
		location.href = "manager_judge.servlet?page=" + jmp_page + "&totalUsers=" +totalUsers;
	}
	else {
		$("jmp_page").value = "";
		layer.open({
			  title: ''
			  ,content: '页面不存在！'
			});
	}
}

//删除judge
function delete_judge_js() {
	layer.confirm('确认删除吗？', {
		  btn: ['确认', '取消'] //可以无限个按钮
		  
		},function(index, layero){
			//获取tbody元素
			var tbody = document.getElementById("tbody");
			//通过tbody获取里面的所有input元素
			var botInpArr = tbody.getElementsByTagName("input");
			//遍历所有chechbox,看其是否选中
		
			//获取tbody的rows
			var rows = tbody.rows;
			
			var arr = [];
			
			//遍历所有chechbox,看其是否选中
			for(var i=0;i<botInpArr.length;i++) {
				if(botInpArr[i].checked == true) {
					arr.push(rows[i].cells[1].innerHTML);
				}
		    }
			
			location.href = "delete_judge?delete_arr=" + arr;
		}, function(index){
			  
		});
}
function search_easy_judge_js()
{
	location.href="manager_judge.servlet?condition="+'简单';
	}
function search_middle_judge_js()
{
	location.href="manager_judge.servlet?condition="+'中等';
	}
function search_hard_judge_js()
{
	location.href="manager_judge.servlet?condition="+'困难';
	}
window.onload = function () {
    var topInp = document.getElementById("theadInp");
    var tbody = document.getElementById("tbody");
    var botInpArr = tbody.getElementsByTagName("input");
    var DeleteJudge = document.getElementById("DeleteJudge");

    //绑定事件
    topInp.onclick = function () {
        //优化版（被点击的input按钮的checked属性值，直接作为下面的所有的input按钮的checked属性值）
        for(var i=0;i<botInpArr.length;i++){
            botInpArr[i].checked = this.checked;
        }
        if(this.checked == true) {
        	DeleteJudge.className = "delete-button-active";
        	DeleteJudge.disabled = false;
        }
        else {
        	DeleteJudge.className = "delete-button";
        	DeleteJudge.disabled = true;
        }
    }
    for(var i=0;i<botInpArr.length;i++){
        if(botInpArr[i].checked == true) {
        	DeleteJudge.className = "delete-button-active";
        	DeleteJudge.disabled = false;
        }
        botInpArr[i].onclick = function () {
            //定义一个标识是true还是false的变量
            //默认它为true
            var bool = true;
            //检测每一个input的checked属性值。
            for(var j=0;j<botInpArr.length;j++){
                if(botInpArr[j].checked == false){
                    bool = false;
                }
            }
            topInp.checked = bool;

            var bool_deletebut = false;
            for(var j=0;j<botInpArr.length;j++){
                if(botInpArr[j].checked == true){
                    bool_deletebut = true;
                }
            }
            DeleteJudge.disabled = !bool_deletebut;
            if(bool_deletebut == false) {
            	DeleteJudge.className = "delete-button";
            }
            else {
            	DeleteJudge.className = "delete-button-active";
            }
        }
    }
}