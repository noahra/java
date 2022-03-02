package com.jsoniter;

import com.jsoniter.any.Any;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

import com.jsoniter.output.JsonStream;
import junit.framework.Test;
public class TestRecord extends TestCase {

    record TestRecord1(long field1) {
    }
    public record TestRecord0(Long id, String name) {
        public TestRecord0() {
            this(0L, "");
        }
    }

    public void test_print_record_reflection_info() {


        Class<TestRecord1> clazz = TestRecord1.class;

        System.out.println("Record Constructors :");
        for (Constructor<?> constructor : clazz.getConstructors()) {
            System.out.println(constructor);
        }

        System.out.println("Record Methods : ");
        for (Method method : clazz.getMethods()) {
            System.out.println(method);
        }

        System.out.println("Record Fields : ");
        for (Field field : clazz.getFields()) {
            System.out.println(field);
            System.out.println("    modifiers : " + Modifier.toString(field.getModifiers()));
        }

        System.out.println("Record Declared Fields : ");
        for (Field field : clazz.getDeclaredFields()) {
            System.out.println(field);
            System.out.println("    modifiers : " + Modifier.toString(field.getModifiers()));
        }

        try {
            System.out.println("Record Default Declared Constructor : " + clazz.getDeclaredConstructor());
        } catch (Exception ex) {
            System.err.println("No Record Default Declared Constructor!");
        }

        System.out.println("Record Declared Constructors : ");
        for(Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            System.out.println(constructor);
            System.out.println("    name : " + constructor.getName());
            System.out.println("    modifiers : " + Modifier.toString(constructor.getModifiers()));
            System.out.println("    input count : " + constructor.getParameterCount());
            System.out.println("    input types : ");
            for (Class<?> parameter : constructor.getParameterTypes())
            System.out.println("        " + parameter);
        }
    }
    public void test_empty_record() throws IOException {
        JsonIterator iter = JsonIterator.parse("{}");
        assertNotNull(iter.read(TestRecord0.class));
    }

    public void test_empty_simple_record() throws IOException {
        JsonIterator iter = JsonIterator.parse("{}");
        SimpleRecord simpleRecord = iter.read(SimpleRecord.class);
        assertNull(simpleRecord.field1());
        iter.reset(iter.buf);
        Object obj = iter.read(Object.class);
        assertEquals(0, ((Map) obj).size());
        iter.reset(iter.buf);
        Any any = iter.readAny();
        assertEquals(0, any.size());
    }

    public void test_one_field() throws IOException {
        JsonIterator iter = JsonIterator.parse("{ 'field1'\r:\n\t'hello' }".replace('\'', '"'));
        SimpleRecord simpleRecord = iter.read(SimpleRecord.class);
        assertEquals("hello", simpleRecord.field1());
    }

    public void test_record_error() throws IOException {

        JsonIterator iter = JsonIterator.parse("{ 'field1' : 1".replace('\'', '"'));
        iter.read(TestRecord1.class);
    }

}
