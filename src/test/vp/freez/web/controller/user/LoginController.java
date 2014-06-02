package vp.freez.web.controller.user;

import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import vp.freez.db.Dao;
import vp.freez.model.user.User;
import vp.freez.web.annotation.Action;
import vp.freez.web.annotation.JSP;
import vp.freez.web.annotation.Namespace;
import vp.freez.web.annotation.Views;
import vp.freez.web.controller.Controller;

/**
 * 
 * @author vpsong
 * 
 */
@Namespace("user")
public class LoginController extends Controller {

	private String name;

	@Action("login")
	@Views(@JSP(name = "test", path = "/WEB-INF/test.jsp"))
	public void login() {
		// User user = new User();
		// user.setNickName("wanzi");
		// Dao dao = new Dao();
		// try {
		// dao.insert(user);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		HttpSession session = request.getSession();
		session.setAttribute("song", "vip");
		if (name == null) {
			renderView("test");
		} else {
			renderText(name);
		}
	}

	public static void main(String[] args) {
		Throwable ex = new Throwable();

		StackTraceElement[] stackElements = ex.getStackTrace();

		if (stackElements != null) {
			for (int i = 0; i < stackElements.length; i++) {
				System.out.println(stackElements[i].getClassName());
				System.out.println(stackElements[i].getFileName());
				System.out.println(stackElements[i].getLineNumber());
				System.out.println(stackElements[i].getMethodName());
				System.out.println("-----------------------------------");
			}
		}
	}

}
