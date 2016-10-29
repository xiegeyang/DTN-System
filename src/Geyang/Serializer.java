package Geyang;

import java.io.*;

public class Serializer {
	private static Serializer serializer = null;
	
	private Serializer(){}
	
	public static Serializer getInstance(){
		if(serializer==null) serializer = new Serializer();
		return serializer;
	}
	
	public byte[] serialize(ContactHis history) throws IOException{
		try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(history);
            }
            return b.toByteArray();
        }
	}
}
