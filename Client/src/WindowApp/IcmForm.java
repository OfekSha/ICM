package WindowApp;


/**
 * This interface implements the abstract method used to display
 * objects onto the ocf.client or osf.server UIs.
 *
 * @author Dr Robert Lagani&egrave;re
 * @author Dr Timothy C. Lethbridge
 * @version July 2000
 */

//******************* DEPRECATED (CAN BE DELETED SAFELY) *********************//
public interface IcmForm {
	/**
	   * Method that when overriden is used to display objects onto
	   * a UI.
	   */
	void getFromServer(Object message);

}
