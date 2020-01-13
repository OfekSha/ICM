package theServer;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

/**
 * made to send the output stream to server gui
 *
 */
public class MessgesOutputStream extends OutputStream {
    /**the text area witch will replace the console
     * 
     */
    private TextArea Messges;
    /**the string where incomplete sentences are kept until complete 
     * 
     */
    private String app="";
     
    public MessgesOutputStream(TextArea Messges) {
        this.Messges = Messges;
    }
    /**Receiving the stream input
     * 
     */
    @Override
    public   void write(int b) {
        int[] bytes = {b};
        write(bytes, 0, bytes.length);
    }

    /** creating the string to go in to the TextArea
     * @param bytes
     * @param offset
     * @param length
     */
    public void write(int[] bytes, int offset, int length) {
    	String s = new String(bytes, offset, length);
    	app=app +s;
    	// printing to text area only if full sentence  - to prevent problomes with the lunched thread
         if(app.contains("\n")) { 
        Messges.appendText(app);
        app="";
         }
   
}
}
