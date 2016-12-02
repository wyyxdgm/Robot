package com.pulu.robot.test.serial;

import java.io.Serializable;

public class Student implements Serializable {
	private static final long serialVersionUID = 7104097280682850111L;
	private String name;
	private char sex;
	private int year;
	private double gpa;

	public Student() {

	}

	public Student(String name, char sex, int year, double gpa) {
		this.name = name;
		this.sex = sex;
		this.year = year;
		this.gpa = gpa;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public String getName() {
		return this.name;
	}

	public char getSex() {
		return this.sex;
	}

	public int getYear() {
		return this.year;
	}

	public double getGpa() {
		return this.gpa;
	}
}