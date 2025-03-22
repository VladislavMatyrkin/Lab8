package servlet;

import entity.ChatUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login.do"})
public class LoginServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;
    private int sessionTimeout = 10 * 60;
    private int count = 0;

    public void init() throws ServletException {
        super.init();
        String value = getServletConfig().getInitParameter("SESSION_TIMEOUT");
        if (value != null) {
            sessionTimeout = Integer.parseInt(value);// Переопределение тайм-аута сессии, если задано
        }
    }
    // Обработка GET-запроса (отображение страницы логина)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = (String) request.getSession().getAttribute("name");
        String errorMessage = (String) request.getSession().getAttribute("error");
        String previousSessionId = null;
        // Проверка на существующую сессию пользователя
        if (name == null) {
            try {
                for (Cookie aCookie : request.getCookies()) {
                    if (aCookie.getName().equals("sessionId")) {
                        previousSessionId = aCookie.getValue();
                        break;
                    }
                }
            } catch (NullPointerException e) {
                name = null;
            }
            if (previousSessionId != null) { // Проверяем, есть ли пользователь с таким sessionId
                for (ChatUser aUser : activeUsers.values()) {
                    if (aUser.getSessionId().equals(previousSessionId)) {
                        name = aUser.getName();// Если есть, восстанавливаем имя пользователя
                        aUser.setSessionId(request.getSession().getId());// Обновляем sessionId
                    }
                }
            }
        }
        // Если имя найдено, пытаемся войти
        if (name != null && !"".equals(name)) {
            errorMessage = processLogonAttempt(name, request, response);
        }
        // Отображение HTML-страницы с формой логина
        response.setCharacterEncoding("utf8");
        PrintWriter pw = response.getWriter();
        pw.println("<html><head><title>Мега-чат!</title><meta http-equiv='Content-Type' content='text/html; charset=utf-8'/></head>");
        if (errorMessage != null) {
            pw.println("<p><font color='red'>" + errorMessage + "</font></p>");
        }
        pw.println("<form action='/Java_Lab_9_war_exploded/login.do' method='post'>Введите имя: <input type='text' name='name' value=''><input type='submit' value='Войти в чат'></form></body></html>");
        request.getSession().setAttribute("error", null);
    }
    // Обработка POST-запроса (попытка входа)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String errorMessage = null;
        // Проверяем, введено ли имя
        if (name == null || "".equals(name)) {
            errorMessage = "Поле имя пустое,для входа в чат, введите имя!";
        } else if (count >= 10) {
            errorMessage = "Number of users exceeded!";
        } else {
            errorMessage = processLogonAttempt(name, request, response);// Попытка авторизации
        }
        // Если есть ошибка, перенаправляем обратно на страницу логина
        if (errorMessage != null) {
            request.getSession().setAttribute("name", null);
            request.getSession().setAttribute("error", errorMessage);
            response.sendRedirect(response.encodeRedirectURL("/Java_Lab_9_war_exploded/login.do"));
        }
    }
    // Обработка попытки входа
    String processLogonAttempt(String name, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getSession().getId();
        ChatUser aUser = activeUsers.get(name);
        // Если пользователь не существует, создаем нового
        if (aUser == null) {
            aUser = new ChatUser(name, Calendar.getInstance().getTimeInMillis(), sessionId);
            synchronized (activeUsers) {
                activeUsers.put(aUser.getName(), aUser);
            }
        }
        count++;// Увеличиваем счетчик активных пользователей
// Если пользователь может войти
        if (aUser.getSessionId().equals(sessionId) || aUser.getLastInteractionTime() < (Calendar.getInstance().getTimeInMillis() - sessionTimeout * 1000)) {
            request.getSession().setAttribute("name", name);
            aUser.setLastInteractionTime(Calendar.getInstance().getTimeInMillis());
            Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
            sessionIdCookie.setMaxAge(60 * 60 * 24 * 365);// Устанавливаем cookie на 1 год
            response.addCookie(sessionIdCookie);
            response.sendRedirect(response.encodeRedirectURL("/Java_Lab_9_war_exploded/view.htm"));
            return null;
        } else {
            // Если имя занято
            return "Sorry, <strong>" + name + "</strong> is engaged. Please try another one!";
        }
    }
}
