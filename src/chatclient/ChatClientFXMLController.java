/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 *
 * @author kevinbudd
 */
public class ChatClientFXMLController implements Initializable {
    
    @FXML
    private TextArea txtChat;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtMessage;
    
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Socket socket = null;
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
                  new Thread( () -> {  

        txtMessage.setOnAction(e -> {
        try {
            
          //get the clients name and message from textfields
          String name = txtName.getText();
          String message = txtMessage.getText();
          txtMessage.clear();

          // Send the message to the server
          toServer.writeUTF(name + ": " + message);
          toServer.flush();
          
        // Create and start a new thread for the connection
        new Thread(new HandleServer(socket)).start(); 

        } catch (IOException ex) {
          System.err.println(ex);
        }
      });  
           
      // Create and start a new thread for the connection
      new Thread(new HandleServer(socket)).start();  
        
      }).start();
                  
        
    }    
    
  // Define the thread class for handling new connection
  class HandleServer implements Runnable {
    private Socket socket; // A connected socket

    /** Construct a thread */
    public HandleServer(Socket socket) {
      this.socket = socket;
    }

    /** Run a thread */
    @Override
    public void run() {
      try {
      
      // Create a socket to connect to the server
      socket = new Socket("localhost", 8002);
      
      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
      
      //inputStreamReader = new InputStreamReader(socket.getInputStream());
      
      //bufferedReader = new BufferedReader(inputStreamReader);
      
      String messageFromServer = fromServer.readUTF();
      
      Platform.runLater(() -> {
          txtChat.appendText(messageFromServer + '\n');
      });
      
      
    } catch (IOException ex) {
        txtChat.appendText(ex.toString() + '\n');
    }
    
    }
    
  }
}
        
        


