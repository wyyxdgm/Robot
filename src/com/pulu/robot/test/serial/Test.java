package com.pulu.robot.test.serial;

import com.pulu.robot.om.Experience;
import com.pulu.robot.service.EMService;

public class Test {

	public static void main(String[] args) {
		EMService.addE(new Experience());
		EMService.serializeToFile();
	}

}
