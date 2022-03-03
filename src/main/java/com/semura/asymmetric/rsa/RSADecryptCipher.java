package com.semura.asymmetric.rsa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

public class RSADecryptCipher {
	private Cipher cipher;
	
	public RSADecryptCipher(PrivateKey privateKey) {
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void decrypt(InputStream inputStream, OutputStream outputStream) throws IOException {
		CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
		cipherInputStream.transferTo(outputStream);
	}
}
