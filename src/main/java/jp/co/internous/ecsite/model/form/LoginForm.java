
package jp.co.internous.ecsite.model.form;

import java.io.Serializable;

//13時間目　5ｐ
//ログインするには、HTMLのformからユーザ情報（ユーザ名/パスワード）を渡す必要があるので、
//LoginForm（画面からJavaプログラムに送るデータを管理するクラス）から作成していきます。

public class LoginForm implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
