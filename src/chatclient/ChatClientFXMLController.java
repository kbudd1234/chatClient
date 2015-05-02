/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
      txtMessage.setOnAction(e -> {
      try {
        //get the clients name and message from textfields
        String name = txtName.getText();
        String message = txtMessage.getText();
  
        // Send the message to the server
        toServer.writeUTF(name + ": " + message);
        toServer.flush();
  
        // Display to the text area
        //txtChat.appendText(name + ": " + message + "\n");
        
      }
      catch (IOException ex) {
        System.err.println(ex);
      }
    });  
      
      try {
      // Create a socket to connect to the server
      Socket socket = new Socket("localhost", 8005);
      
      // Create an input stream to receive data from the server
      fromServer = new DataInputStream(socket.getInputStream());

      // Create an output stream to send data to the server
      toServer = new DataOutputStream(socket.getOutputStream());
      
      
      
          
          String message = fromServer.readUTF();
          
          txtChat.appendText(message + "\n");
      
     
          
    }
    catch (IOException ex) {
      txtChat.appendText(ex.toString() + '\n');
    }
        
    }    
    
}
