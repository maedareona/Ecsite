//(中級2時間目　9ｐ）履歴表示機能の作成
//フロントからデータを渡す
package jp.co.internous.ecsite.model.form;

import java.io.Serializable;

public class HistoryForm implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
