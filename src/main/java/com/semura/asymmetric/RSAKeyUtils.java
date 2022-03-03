package com.semura.asymmetric;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RSAKeyUtils {
	
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			return generator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	public static void write(Key key, OutputStream outputStream) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(key);
	}
	
	
	public static Key readKey(InputStream inputStream) throws InvalidKeySpecException, IOException{
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		try {
			return (Key)objectInputStream.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
