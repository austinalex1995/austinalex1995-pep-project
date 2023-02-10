package Controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.h2.util.json.JSONObject;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {

    /*
     * 
     * Member Variables
     * 
     */

    SocialMediaService service;



    /*
     * 
     * Constructor
     * 
     */

    public SocialMediaController() {

        service = new SocialMediaService();

    }



    /*
     * 
     * Class Methods
     * 
     */

    public Javalin startAPI() {

        Javalin app = Javalin.create();
        app.post("/register", this::register);
        app.post("/login", this::attemptLogin);
        app.post("/messages", this::postMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageByID);
        app.delete("/messages/{message_id}", this::deleteMessageByID);
        app.patch("/messages/{message_id}", this::updateMessageText);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesFromAccountID);
        return app;

    }

    private void register(Context context) throws JsonProcessingException {

        String body = context.body();
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(body, Account.class);
        Account registeredAccount = service.register(account);

        if (registeredAccount == null) {
            context.status(400);
        } else {
            context.json(om.writeValueAsString(registeredAccount));
            context.status(200);
        }

    }

    private void attemptLogin(Context context) throws JsonProcessingException {

        String body = context.body();
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(body, Account.class);
        Account returnedAccount = service.attemptLogin(account);
        if (returnedAccount == null) {
            context.status(401);
        } else {
            context.json(om.writeValueAsString(returnedAccount));
            context.status(200);
        }

    }

    private void postMessage(Context context) throws JsonProcessingException {

        String body = context.body();
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(body, Message.class);
        Message returnedMessage = service.postMessage(message);
        if (returnedMessage == null) {
            context.status(400);
        } else {
            context.json(om.writeValueAsString(returnedMessage));
            context.status(200);
        }

    }

    private void getAllMessages(Context context) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        List<Message> returnedMessages = service.getAllMessages();
        if (returnedMessages == null) {
            context.status(400);
        } else {
            context.json(om.writeValueAsString(returnedMessages));
            context.status(200);
        }

    }

    private void getMessageByID(Context context) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        String idAsString = context.pathParam("message_id");
        int id = (int) Integer.valueOf(idAsString);

        Message returnedMessage = service.getMessageByID(id);
        if (returnedMessage == null) {
            context.status(400);
        } else {
            context.json(om.writeValueAsString(returnedMessage));
            context.status(200);
        }

    }

    private void deleteMessageByID(Context context) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        String idAsString = context.pathParam("message_id");
        int id = (int) Integer.valueOf(idAsString);
        Message originalMessage = service.getMessageByID(id);
        if (originalMessage == null) {
            context.json("");
            context.status(200);
        } else {
            boolean deletedMessage = service.deleteMessageByID(id);
            if (!deletedMessage) {
                context.json("{}");
                context.status(200);
            } else {
                context.json(om.writeValueAsString(originalMessage));
                context.status(200);
            }
        }

    }

    private void updateMessageText(Context context) throws JsonProcessingException {

        String body = context.body();
        System.out.println(body);
        ObjectMapper om = new ObjectMapper();
        String idAsString = context.pathParam("message_id");
        int id = (int) Integer.valueOf(idAsString);
        Message originalMessage = service.getMessageByID(id);
        if (originalMessage == null) {
            context.status(400);
            return;
        } else {
            body = body.substring(0, body.length()-2);
            body += (", \"posted_by\": " + originalMessage.getPosted_by() + ", \"time_posted_epoch\": " + originalMessage.getTime_posted_epoch() + "}");
            Message updatedMessageOutline = om.readValue(body, Message.class);
            updatedMessageOutline.setMessage_id(id);
            Message updatedMessage = service.updateMessageText(updatedMessageOutline);
            if (updatedMessage == null) {
                context.status(400);
            } else {
                context.json(om.writeValueAsString(updatedMessage));
                context.status(200);
            }
        }

    }

    private void getAllMessagesFromAccountID(Context context) throws JsonProcessingException {

        String body = context.body();
        ObjectMapper om = new ObjectMapper();
        String idAsString = context.pathParam("account_id");
        int id = (int) Integer.valueOf(idAsString);
        List<Message> messages = service.getAllMessagesFromAccountID(id);
        if (messages == null) {
            context.json("{}");
            context.status(200);
        } else {
            context.json(om.writeValueAsString(messages));
            context.status(200);
        }

    }

}