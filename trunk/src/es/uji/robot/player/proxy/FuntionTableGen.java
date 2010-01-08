package es.uji.robot.player.proxy;

import java.util.ArrayList;


public class FuntionTableGen {

	public static void register() {
		
	}
	
	static {
		ArrayList<TableObject> elements = new ArrayList<TableObject>();
		TableElements table = new TableElements();
		elements = table.getElements();
		
		for( int i = 0; i < elements.size(); i++ ){
			Client.registerXDRObject(elements.get(i).getIface(), elements.get(i).getType(), elements.get(i).getSubtype(), elements.get(i).getObject());
		}
	}

}
