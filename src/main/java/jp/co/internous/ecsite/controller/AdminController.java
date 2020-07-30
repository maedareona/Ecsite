package jp.co.internous.ecsite.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.GoodsForm;
import jp.co.internous.ecsite.model.form.LoginForm;

//下記URLでアクセスできるよう設定
@Controller
@RequestMapping("/ecsite/admin")
public class AdminController {

//前ページで作成したRepositoryを読み込む(参照　13時間目　10ｐ)
	@Autowired
	private UserRepository userRepos;
	@Autowired
	private GoodsRepository goodsRepos;
	
//トップページ(adminindex.html）に遷移するメソッド	
	@RequestMapping("/")
	public String index() {
		return "adminindex";
	}

//LoginFormを使ってユーザ情報を受け取るメソッド
	@PostMapping("/welcome")
	public String welcome(LoginForm form, Model m) {

//ユーザー名とパスワードで検索
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());

//検索結果が存在していれば、isAdmin(管理者かどうか)を取得して、管理者だった場合、処理
		if (users != null && users.size() > 0) {
			boolean isAdmin = users.get(0).getIsAdmin() != 0;
			if(isAdmin) {
				List<Goods> goods = goodsRepos.findAll();
				m.addAttribute("userName", users.get(0).getUserName());
				m.addAttribute("password", users.get(0).getPassword());
				m.addAttribute("goods", goods);
			}
		}
		return "welcome";
	}
	
//13時間目テキストで作成した管理者ページに新規商品の登録と削除機能を追加する
	//まずはgoodsMstメソッドを追加(14時間目　2ｐ)
	@RequestMapping("/goodsMst")
	public String goodsMst(LoginForm form, Model m) {
		m.addAttribute("userName", form.getUserName());
		m.addAttribute("password", form.getPassword());
		
		return "goodsmst";
	}
	
	
//formパッケージ配下にGoodsFormクラスを作成し、メソッド（addGoods）を追加 (14時間目　4ｐ)
	@RequestMapping("/addGoods")
	public String addGoods(GoodsForm goodsForm, LoginForm loginForm, Model m) {
		m.addAttribute("userName", loginForm.getUserName());
		m.addAttribute("password", loginForm.getPassword());
	
	Goods goods = new Goods();
	goods.setGoodsName(goodsForm.getGoodsName());
	goods.setPrice(goodsForm.getPrice());
	goodsRepos.saveAndFlush(goods);
	
	return "forward:/ecsite/admin/welcome";
	}
		
//商品マスタから商品を削除する機能を作成 (14時間目　8ｐ）
	//これまでのページ遷移による処理ではなく、ajaxを使用した方式での処理(REST）
	//deleteApiメソッドを追加する
	@ResponseBody
	@PostMapping("/api/deleteGoods")
	public String deleteApi(@RequestBody GoodsForm f, Model m) {
		try {
			goodsRepos.deleteById(f.getId());
		} catch (IllegalArgumentException e) {
			return "-1";
		}
		return "1";
	}

}
