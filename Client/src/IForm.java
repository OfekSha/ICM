// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

/**
 * This interface implements the abstract method used to display
 * objects onto the client or server UIs.
 *
 * @author Dr Robert Lagani&egrave;re
 * @author Dr Timothy C. Lethbridge
 * @version July 2000
 */
public interface IForm
{
  /**
   * Method that when overriden is used to display objects onto
   * a UI.
   */
  void display(String message);
}
