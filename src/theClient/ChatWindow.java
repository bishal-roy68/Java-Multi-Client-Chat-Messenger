/**
 Final day update by Mashud
 * Reference: used partial  code idea from textbook.
 */

package theClient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.Scene;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

// class for chat Window
public class ChatWindow extends Application {
    private VBox messageBox;
    private TextArea theChatArea;
    private TextField inputInField;
    private PrintWriter out;
    private ScrollPane scrollPane;

    @Override
    public void start(Stage theStage) {

        // chat bubble container
        messageBox = new VBox(6);
        messageBox.setPadding(new Insets(10));

        //scrollpane
        scrollPane = new ScrollPane(messageBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(350);

        inputInField = new TextField();
        inputInField.setPromptText("Type the message...");


        Button sendtheButton = new Button("Send");

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(scrollPane, inputInField,sendtheButton);

        Scene viewScence =  new Scene( layout, 500, 350);
        theStage.setTitle("MashBish Multi Chat-Messenger.");
        theStage.setScene(viewScence);
        theStage.setAlwaysOnTop(false);
        theStage.show();

        addDateBanner();// date and time banner

        //connect to server
        try {
            // local host and connect the server
            Socket newSock = new Socket("localhost", 1234);
            out = new PrintWriter(newSock.getOutputStream(), true);

            BufferedReader inFromServer = new BufferedReader(
                    new InputStreamReader(newSock.getInputStream()));

            // it will pop dialog box before starting the chatWindow
            TextInputDialog newMessageDialogBox = new TextInputDialog();
            newMessageDialogBox.setTitle("Welcome to MashBish Multi Chat-Messenger!");
            newMessageDialogBox.setHeaderText("Hello & Welcome!");
            newMessageDialogBox.setContentText("Enter your name");

            String username;
                    username = newMessageDialogBox.showAndWait().orElse("Anonymous user");
            out.println(username);

            // Listen for message from server
            Thread listenTheThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = inFromServer.readLine()) != null) {
                        String timeNow = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
                        String msgWithTime = serverMessage + "  (" + timeNow + ")";

                        // center system/delivered messages
                        if (serverMessage.startsWith("[System]") || serverMessage.startsWith("[Delivered")) {
                            Platform.runLater(() -> addCenterMessage(msgWithTime));
                        } else {
                            // normal messages go left side
                            Platform.runLater(() -> addTheMessage(msgWithTime, false));
                        }
                    }

                } catch (Exception ex) {
                    System.out.println("Disconnected...");
                }
            });


            // after clicking send -- it will print
            sendtheButton.setOnAction(e -> {
                String messageFor = inputInField.getText();
                if(!messageFor.isEmpty()){
                    addTheMessage("Me: " + messageFor, true);
                    out.println(messageFor);

                    System.out.println("Sent: " + messageFor);
                    inputInField.clear();
                }
            });

            // thread
            listenTheThread.setDaemon(true);
            listenTheThread.start();

        } catch (Exception e) {
            System.out.println("Cannot connect... something is not right");
        }
    }

   // this makes a banner that shows the date + time in the middle
    private void addDateBanner() {
        String dateText = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEEE, MMM d - hh:mm a"));

        Label banner = new Label(dateText);
        banner.setStyle("-fx-padding: 5; -fx-font-weight: bold; -fx-text-fill: gray;");

        HBox centerBox = new HBox(banner);
        centerBox.setAlignment(Pos.CENTER); // centers the banner

        // adding the banner before any chat messages
        messageBox.getChildren().add(centerBox);
    }

    // normal message bubble (right = mine, left = others)
    private void addTheMessage(String enterText, boolean isMine){
        Label bubble = new Label(enterText);
        HBox containerForBubble = new HBox(bubble);

        if(isMine){
            containerForBubble.setAlignment(Pos.CENTER_RIGHT);
            bubble.setStyle("-fx-padding: 8; -fx-background-color: #e2cfd4; -fx-background-radius: 8;");
        } else{
            containerForBubble.setAlignment(Pos.CENTER_LEFT);
            bubble.setStyle("-fx-padding: 8; -fx-background-color: #90b4b2; -fx-background-radius: 8;");

        }

        messageBox.getChildren().add(containerForBubble);
}
    // system/delivered messages → centered
    private void addCenterMessage(String text) {
        Label msg = new Label(text);
        msg.setStyle("-fx-padding: 5; -fx-text-fill: #555; -fx-font-style: italic;");

        HBox box = new HBox(msg);
        box.setAlignment(Pos.CENTER);

        messageBox.getChildren().add(box);
    }
    // lunch the javafx app...
    public static void main(String[] args) {
        launch();
    }
}