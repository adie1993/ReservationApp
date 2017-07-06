package app.adie.reservation.app;

/**
 * Created by Adie on 06/05/16.
 */
public class EndPoints {


    public static final String BASE_API_URL = "http://10.0.2.2/kraka/api";
    public static final String BASE_URL = "http://10.0.2.2/kraka/v1/index.php";
    public static final String INSERTCHAT = BASE_URL + "/user/chat";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms_all/_ID_";
    public static final String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_";
    public static final String UPDATE = BASE_URL + "/update_chat/_ID_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
}
