package Service;

import java.util.List;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

public class SocialMediaService {

    /*
     * 
     * Member Variables
     * 
     */

    private SocialMediaDAO smDAO;



    /*
     * 
     * Constructors
     *
     */

    public SocialMediaService() {
        smDAO = new SocialMediaDAO();
    }
    
    public SocialMediaService(SocialMediaDAO dao) {
        smDAO = dao;
    }



    /*
     * 
     * Class Methods
     * 
     */

    public Account register(Account account) {
        if (account.getUsername().length() == 0 || account.getPassword().length() < 4) return null;
        return smDAO.registerUser(account.getUsername(), account.getPassword());
    }

    public Account attemptLogin(Account account) {
        if (account.getUsername().length() == 0 || account.getPassword().length() == 0) return null;
        return smDAO.attemptLogin(account.getUsername(), account.getPassword());
    }

    public Message postMessage(Message message) {
        if (message.getMessage_text().length() == 0) return null;
        return smDAO.postMessage(message);
    }

    public List<Message> getAllMessages() {
        return smDAO.getAllMessages();
    }

    public Message getMessageByID(int id) {
        if (id == -1) return null;
        return smDAO.getMessageByID(id);
    }

    public boolean deleteMessageByID(int id) {
        if (id == -1) return false;
        return smDAO.deleteMessageByID(id);
    }

    public Message updateMessageText(Message message) {
        if (message.getMessage_id() == -1 || message.getMessage_text().length() == 0 || message.getMessage_text().length() > 255) return null;
        return smDAO.updateMessageText(message);
    }

    public List<Message> getAllMessagesFromAccountID(int id) {
        if (id == -1) return null;
        return smDAO.getAllMessagesFromAccountID(id);
    }
    
}
