package com.semura.asymmetric.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.semura.asymmetric.RSAKeyUtils;
import com.semura.asymmetric.rsa.RSADecryptCipher;
import com.semura.asymmetric.rsa.RSAEncryptCipher;

public class GUIFrame extends JFrame{
	private final JButton encryptBtn;
	private final JButton decryptBtn;
	private final JTextArea rawMessage;
	private final JTextArea encryptedMessage;
	
	private final JButton savePublicKeyBtn;
	private final JButton savePrivateKeyBtn;
	
	private final JButton readPublicKeyBtn;
	private final JButton readPrivateKeyBtn;

	private PublicKey publicKey;
	private PrivateKey privateKey;
	private RSADecryptCipher decryptCipher;
	private RSAEncryptCipher encryptCipher;
	
	private JFileChooser fileChooser = new JFileChooser();
	
	
	private final JButton generateKeyPairBtn;

	
	public GUIFrame() {
		encryptBtn = new JButton("Encrypt");
		encryptBtn.setVisible(encryptCipher != null);
		encryptBtn.addActionListener(ev->encryptMessage());
		
		decryptBtn = new JButton("Decrypt");
		decryptBtn.setVisible(decryptCipher != null);
		decryptBtn.addActionListener(ev->decryptMessage());
		
		savePublicKeyBtn = new JButton("Save Public key");
		savePublicKeyBtn.setVisible(publicKey != null);
		savePublicKeyBtn.addActionListener(ev->saveKey(publicKey));
		
		savePrivateKeyBtn = new JButton("Save Private key");
		savePrivateKeyBtn.setVisible(privateKey != null);
		savePrivateKeyBtn.addActionListener(ev->saveKey(privateKey));
		
		rawMessage = new JTextArea();
		encryptedMessage = new JTextArea();
		
		readPublicKeyBtn = new JButton("Read Public key");
		readPublicKeyBtn.addActionListener(ev->readPublicKey());
		readPrivateKeyBtn = new JButton("Read Private key");
		readPrivateKeyBtn.addActionListener(ev->readPrivateKey());
		
		generateKeyPairBtn = new JButton("Generate key pair");
		generateKeyPairBtn.addActionListener(l->generateKeyPair());
		
		setContentPane(new JPanel());
		
		final int rows = 10;
		final int col = 30;
		rawMessage.setColumns(col);
		rawMessage.setRows(rows);
		rawMessage.setLineWrap(true);
		encryptedMessage.setColumns(col);
		encryptedMessage.setRows(rows);
		encryptedMessage.setLineWrap(true);
		add(generateKeyPairBtn);
		add(readPrivateKeyBtn);
		add(readPublicKeyBtn);
		add(encryptBtn);
		add(decryptBtn);
		add(savePrivateKeyBtn);
		add(savePublicKeyBtn);
		add(new JScrollPane(rawMessage));
		add(new JScrollPane(encryptedMessage));
		notShow();
		setVisible(true);
		setTitle("Asymmetric algorithm");
		setSize(500, 700);
	}
	
	private void showPrivateKeyPart() {
		decryptBtn.setVisible(true);
		encryptedMessage.setEditable(true);
		savePrivateKeyBtn.setVisible(true);
	}
	
	private void showPublicKeyPart() {
		encryptBtn.setVisible(true);
		rawMessage.setEditable(true);
		savePublicKeyBtn.setVisible(true);
	}
	
	private void notShow() {
		generateKeyPairBtn.setVisible(true);
		encryptBtn.setVisible(false);
		rawMessage.setEditable(false);
		savePublicKeyBtn.setVisible(false);
		decryptBtn.setVisible(false);
		encryptedMessage.setEditable(false);
		savePrivateKeyBtn.setVisible(false);
	}
	
	public void generateKeyPair() {
		KeyPair keyPair = RSAKeyUtils.generateKeyPair();
		privateKey = keyPair.getPrivate();
		publicKey = keyPair.getPublic();
		decryptCipher = new RSADecryptCipher(privateKey);
		encryptCipher = new RSAEncryptCipher(publicKey);
		encryptBtn.setVisible(encryptCipher != null);
		decryptBtn.setVisible(decryptCipher != null);
		savePrivateKeyBtn.setVisible(privateKey != null);
		savePublicKeyBtn.setVisible(publicKey != null);
		showPublicKeyPart();
		showPrivateKeyPart();
		setVisible(true);
	}
	
	public void encryptMessage() {
		String text = rawMessage.getText();
		Encoder encoder = Base64.getEncoder();
		OutputStream outputStream = new ByteArrayOutputStream();
		byte[] encryptBytes = encryptCipher.encrypt(text.getBytes());
		String eeString = new String(encoder.encode(encryptBytes));
		encryptedMessage.setText(eeString);
	}
	
	public void decryptMessage() {
		String text = encryptedMessage.getText();
		Decoder decoder = Base64.getDecoder();
		byte[] decryptMessage = decryptCipher.decrypt(decoder.decode(text.getBytes()));
		rawMessage.setText(new String(decryptMessage));
	}
	
	public void saveKey(Key key) {
		fileChooser.showSaveDialog(this);
		File file = fileChooser.getSelectedFile();
		if (file == null) return;
		try (FileOutputStream outputStream = new FileOutputStream(file)){
			RSAKeyUtils.write(key, outputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readPublicKey() {
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if (file == null) return;
		try (InputStream inputStream = new FileInputStream(file)) {
			this.publicKey = (PublicKey)(RSAKeyUtils.readKey(inputStream));
			this.encryptCipher = new RSAEncryptCipher(publicKey);
			notShow();
			showPublicKeyPart();
			setVisible(true);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void readPrivateKey() {
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if (file == null) return;
		try (InputStream inputStream = new FileInputStream(file)) {
			this.privateKey = (PrivateKey)(RSAKeyUtils.readKey(inputStream));
			this.decryptCipher = new RSADecryptCipher(privateKey);
			notShow();
			showPrivateKeyPart();
			setVisible(true);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
