package entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatMessage {
    private String message;
    private ChatUser author;
    private long timestamp;// Временная метка сообщения

    public ChatMessage(String message, ChatUser author, long timestamp) {
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    // Конструктор для создания сообщения
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatUser getAuthor() {
        return author;
    }

    public void setAuthor(ChatUser author) {
        this.author = author;
    }

    // Метод для форматирования временной метки
    public String getFormattedTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }
}
