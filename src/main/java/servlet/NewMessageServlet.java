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
        request.setCharacterEncoding("UTF-8");
        String message = request.getParameter("message");
        if (message != null && !"".equals(message)) {
            ChatUser author = activeUsers.get((String) request.getSession().getAttribute("name"));
            synchronized (messages) {
                messages.add(new ChatMessage(message, author, Calendar.getInstance().getTimeInMillis()));
            }
        }
        response.sendRedirect("/Java_Lab_9_war_exploded/message.htm");
    }
}
