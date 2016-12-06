package com.pulu.robot.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.pulu.robot.om.EM;
import com.pulu.robot.om.Experience;

@SuppressWarnings("resource")
public class EMService {
	public static EM em = new EM();
	private final static String EM_PATH = "data//em.txt";
	static {
		FileInputStream fis;
		try {
			fis = new FileInputStream(EM_PATH);
			ObjectInputStream ois = new ObjectInputStream(fis);
			em = (EM) ois.readObject();
			if (em == null)
				em = new EM();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void serializeToFile() {
		File file = new File(EM_PATH);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(em);
			oos.flush();
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(em.getExperience().getName());
	}

	public static void addE(Experience e) {
		em.setExperience(e);
	}
}
