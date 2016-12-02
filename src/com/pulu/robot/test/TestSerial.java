package com.pulu.robot.test;

import java.io.IOException;

import com.pulu.robot.om.EM;
import com.pulu.robot.util.ReadFileToObjects;

public class TestSerial {
	public static void main(String[] args) {
		EM em = new EM();
		ReadFileToObjects rto = new ReadFileToObjects("data\\chanel.txt", false, true);
		try {
			rto.StringToFile(em.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(em);
	}
}
