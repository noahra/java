package com.jsoniter;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;
import com.jsoniter.spi.ClassInfo;
import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestRecord extends TestCase {

    record TestRecord1(long field1) {}

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

    public void test_record_error() throws IOException {

        JsonIterator iter = JsonIterator.parse("{ 'field1' : 1 }".replace('\'', '"'));
        iter.read(TestRecord1.class);
    }

    public void test_record_withOnlyFieldDecoder() throws IOException {

        assertEquals(ReflectionDecoderFactory.create(new ClassInfo(TestRecord1.class)).getClass(), ReflectionObjectDecoder.OnlyField.class);

        JsonIterator iter = JsonIterator.parse("{ 'field1' : 1 }".replace('\'', '"'));
        iter.read(TestRecord1.class);
    }

    public void test_record_withCtorDecoder() throws IOException {

        record TestRecord2(@JsonProperty long field1) {

            @JsonCreator
            TestRecord2 {}
        }

        assertEquals(ReflectionDecoderFactory.create(new ClassInfo(TestRecord2.class)).getClass(), ReflectionObjectDecoder.WithCtor.class);

        JsonIterator iter = JsonIterator.parse("{ 'field1' : 1 }".replace('\'', '"'));
        TestRecord2 record = iter.read(TestRecord2.class);

        assertEquals(record.field1, 1);
    }
}
