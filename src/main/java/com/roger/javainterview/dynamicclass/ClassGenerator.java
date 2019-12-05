package com.roger.javainterview.dynamicclass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class ClassGenerator {
  public static void main(String[] args) {

  }

  public static void createNewClass()
      throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
      // ClassPool：CtClass对象的容器
		ClassPool pool = ClassPool.getDefault();

		// 通过ClassPool生成一个public新类Employee.java
		CtClass ctClass = pool.makeClass("com.demo.bean.Employee");

		// 添加字段
		// 首先添加字段private String ename
		CtField enameField = new CtField(pool.getCtClass("java.lang.String"), "ename", ctClass);
		enameField.setModifiers(Modifier.PRIVATE);
		ctClass.addField(enameField);

		// 其次添加字段privtae int eage
		CtField eageField = new CtField(pool.getCtClass("int"), "eage", ctClass);
		eageField.setModifiers(Modifier.PRIVATE);
		ctClass.addField(eageField);

		// 其次添加字段privtae int eage
		CtField esexField = new CtField(pool.getCtClass("int"), "esex", ctClass);
		esexField.setModifiers(Modifier.PRIVATE);
		ctClass.addField(esexField);

		// 为字段ename和eno添加getter和setter方法
		ctClass.addMethod(CtNewMethod.getter("getEname", enameField));
		ctClass.addMethod(CtNewMethod.setter("setEname", enameField));
		ctClass.addMethod(CtNewMethod.getter("getEage", eageField));
		ctClass.addMethod(CtNewMethod.setter("setEage", eageField));
		ctClass.addMethod(CtNewMethod.getter("getSex", esexField));
		ctClass.addMethod(CtNewMethod.setter("setSex", esexField));

		// 添加构造函数
		CtConstructor ctConstructor = new CtConstructor(new CtClass[] {}, ctClass);
		// 为构造函数设置函数体
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\n").append("ename=\"gsls200808\";\n").append("eage=25;\n").append("esex=1;\n}");
		ctConstructor.setBody(buffer.toString());
		// 把构造函数添加到新的类中
		ctClass.addConstructor(ctConstructor);

		// 添加自定义方法
		CtMethod ctMethod = new CtMethod(CtClass.voidType, "printInfo", new CtClass[] {}, ctClass);
		// 为自定义方法设置修饰符
		ctMethod.setModifiers(Modifier.PUBLIC);
		// 为自定义方法设置函数体
		StringBuffer buffer2 = new StringBuffer();
		buffer2.append("{\n").append("System.out.println(\"begin!\");\n")
				.append("System.out.println(\"name=\"+ename);\n").append("System.out.println(\"age=\"+eage);\n")
				.append("System.out.println(\"sex=\"+esex);\n").append("System.out.println(\"end!\");\n").append("}");
		ctMethod.setBody(buffer2.toString());
		ctClass.addMethod(ctMethod);

		// 为了验证效果，下面使用反射执行方法printInfo
		Class<?> clazz = ctClass.toClass();
		Object obj = clazz.newInstance();
		obj.getClass().getMethod("printInfo", new Class[] {}).invoke(obj, new Object[] {});
    }
}