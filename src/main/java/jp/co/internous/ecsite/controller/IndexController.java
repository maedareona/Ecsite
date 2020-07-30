//トップページに遷移するcontroller （中級　1時間目2ｐ）

package jp.co.internous.ecsite.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.PurchaseRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.dto.LoginDto;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.Purchase;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;


//localhost：8080/site/のURLにアクセスできるようアノテーションを付与
@Controller
@RequestMapping("/ecsite")
public class IndexController {
	
	
//UserエンティティからUserテーブルにアクセスするDAO(中級8ｐ）
	@Autowired
	private UserRepository userRepos;
	
//GoodsエンティティからgoodsテーブルへアクセスするDAO（中級　1時間目2ｐ）
	@Autowired
	private GoodsRepository goodsRepos;
	
//購入処理を担うメソッド(中級2時間目　5ｐ)
	@Autowired
	private PurchaseRepository purchaseRepos;
	
//WebサービスAPIとして作成するため、JSON形式を扱える様Gsonをインスタンス化する
	private Gson gson = new Gson();
	
	
//トップページ(index.html)に遷移するメソッド
	//goodsテーブルから取得した商品エンティティの一覧をフロントに渡すModelに追加
	@RequestMapping("/")
	public String index(Model m) {
		List <Goods> goods = goodsRepos.findAll();
		m.addAttribute("goods", goods);
		
		return "index";
	}
	
//DBテーブルuserから、ユーザー名/パスワードで検索し、結果を取得する(中級8ｐ)
	@ResponseBody
	@PostMapping("/api/login")
	public String loginApi(@RequestBody LoginForm form) {
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());

//DTOをゲストの情報で初期化し、検索結果が存在していた場合のみ、実在のユーザ情報をDTOに詰める
		//最終的にDTOをJSONオブジェクトとして、画面側に返す
		LoginDto dto = new LoginDto(0, null, null, "ゲスト");
		if(users.size() > 0) {
			dto = new LoginDto(users.get(0));
		}
		return gson.toJson(dto);
	}
	
//	(中級2時間目　5ｐ)
	@ResponseBody
	@PostMapping("/api/purchase")
	public String purchaseApi(@RequestBody CartForm f) {
		
		f.getCartList().forEach((c) -> {
			long total = c.getPrice() * c.getCount();
			purchaseRepos.persist(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
		});
		
		return String.valueOf(f.getCartList().size());
	}
	
//	(中級2時間目　8ｐ) 購入履歴を表示するメソッド
	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm form) {
		String userId = form.getUserId();
		List<Purchase> history = purchaseRepos.findHistory(Long.parseLong(userId));
		List<HistoryDto> historyDtoList = new ArrayList<>();
		history.forEach((v) -> {
			HistoryDto dto = new HistoryDto(v);
			historyDtoList.add(dto);
		});
		
		return gson.toJson(historyDtoList);
	}
	
}