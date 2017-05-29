package jp.powerbase.console;

import jp.powerbase.Command;
import jp.powerbase.xml.DOM;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CommandRetriever {
	private Command command;
	public CommandRetriever(Document entity) {
		DOM dom = new DOM(entity);
		Element root = dom.get().getDocumentElement();
		String command = root.getAttribute("cmd");
		if (!command.equals("")) {
			for (Command val : Command.values()) {
				if (val.value.equalsIgnoreCase(command)) {
					this.command = val;
				}
			}
		} else {
			this.command = null;
		}
	}
	/**
	 * Get command
	 * @return command
	 */
	public Command getCommand() {
	    return command;
	}
}
