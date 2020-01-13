package theServer;

import java.io.IOException;
import java.io.OutputStream;

import javafx.scene.control.TextArea;

/**
 * made to send the output stream to gui
 *
 */
public class MessgesOutputStream extends OutputStream {
    private TextArea Messges;
    private int cnt=0;
     
    public MessgesOutputStream(TextArea Messges) {
        this.Messges = Messges;
    }
  /*   
    @Override
    public void write(int stuff)  {
        Messges.appendText(String.valueOf((char)stuff));
    	

    }
    */
    
    @Override
    public   void write(int b) {
    	cnt++;
        int[] bytes = {b};
        write(bytes, 0, bytes.length);
    }

    public void write(int[] bytes, int offset, int length) {
        String s = new String(bytes, offset, length);
        Messges.appendText(s);
   
}
}
