package com.semura.asymmetric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;

import com.semura.asymmetric.rsa.RSADecryptCipher;
import com.semura.asymmetric.rsa.RSAEncryptCipher;

public class Main {
	public static void main(String[] args) throws Throwable {
		File inputFile = new File("/home/muravyevas/Documents/test.txt");
		KeyPair keyPair = RSAKeyUtils.generateKeyPair();
		RSAEncryptCipher encryptCipher = new RSAEncryptCipher(keyPair.getPublic());
		RSADecryptCipher decryptCipher = new RSADecryptCipher(keyPair.getPrivate());
		encryptCipher.encrypt(new FileInputStream(inputFile), new FileOutputStream("./encrypt"));
		decryptCipher.decrypt(new FileInputStream("./encrypt"), new FileOutputStream("./decrypt"));
	}
}
