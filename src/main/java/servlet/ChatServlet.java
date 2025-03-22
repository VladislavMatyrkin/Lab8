package servlet;

import entity.ChatMessage;
import entity.ChatUser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.HashMap;

@WebServlet(name = "ChatServlet")
public class ChatServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;
    protected HashMap<String, ChatUser> activeUsers;// Список активных пользователей
    protected ArrayList<ChatMessage> messages;// История сообщений

    @SuppressWarnings("unchecked")
    public void init() throws ServletException {
        super.init();
        // Получаем активных пользователей и сообщения из контекста приложения
        activeUsers = (HashMap<String, ChatUser>) getServletContext().getAttribute("activeUsers");
        messages = (ArrayList<ChatMessage>) getServletContext().getAttribute("messages");
        // Если данные отсутствуют, инициализируем пустые структуры
        if (activeUsers == null) {
            activeUsers = new HashMap<>();
            getServletContext().setAttribute("activeUsers", activeUsers);
        }
        if (messages == null) {
            messages = new ArrayList<>(100);
            getServletContext().setAttribute("messages", messages);
        }
    }
}
