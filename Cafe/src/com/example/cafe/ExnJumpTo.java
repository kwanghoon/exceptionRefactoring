package com.example.cafe;

public class ExnJumpTo extends Exception {
	private static final long serialVersionUID = 1L;
	private String pkg;
	private String clz;
	public ExnJumpTo(String pkg, String clz) {
		this.pkg = pkg;
		this.clz = clz;
	}
	public String getPackage() { return pkg; }
	public String getClazz() { return clz; }
}
