package servlet;

import entity.ChatUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout.do"})
public class LogoutServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = (String) request.getSession().getAttribute("name");
        if (name != null) {
            ChatUser aUser = activeUsers.get(name);
            // Если текущая сессия соответствует пользователю, удаляем его
            if (aUser.getSessionId().equals(request.getSession().getId())) {
                synchronized (activeUsers) {
                    activeUsers.remove(name);
                }
                request.getSession().setAttribute("name", null);// Удаляем имя из сессии
                response.addCookie(new Cookie("sessionId", null));// Удаляем cookie
                response.sendRedirect(response.encodeRedirectURL("/Java_Lab_9_war_exploded/login.do"));
            } else {
                response.sendRedirect(response.encodeRedirectURL("/Java_Lab_9_war_exploded/view.htm"));
            }
        } else {
            response.sendRedirect(response.encodeRedirectURL("/Java_Lab_9_war_exploded/login.do"));
        }
    }
}
