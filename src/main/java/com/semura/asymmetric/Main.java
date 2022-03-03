package com.semura.asymmetric;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;

import com.semura.asymmetric.rsa.RSADecryptCipher;
import com.semura.asymmetric.rsa.RSAEncryptCipher;
import com.semura.asymmetric.ui.GUIFrame;

public class Main {
	public static void main(String[] args) throws Throwable {
		new GUIFrame();
	}
}
