package servlet;

import entity.ChatMessage;
import entity.ChatUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@WebServlet(name = "NewMessageServlet", urlPatterns = {"/send_message.do"})
public class NewMessageServlet extends ChatServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");// Устанавливаем кодировку для корректной обработки ввода
        String message = request.getParameter("message");// Получаем текст сообщения
        if (message != null && !"".equals(message)) {// Проверяем, что сообщение не пустое
            ChatUser author = activeUsers.get((String) request.getSession().getAttribute("name"));// Получаем автора по имени из сессии
            synchronized (messages) {// Синхронизируем доступ к списку сообщений
                messages.add(new ChatMessage(message, author, Calendar.getInstance().getTimeInMillis()));// Добавляем новое сообщени
            }
        }
        response.sendRedirect("/Java_Lab_9_war_exploded/message.htm");
    }
}
