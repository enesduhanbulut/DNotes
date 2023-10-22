package com.duhapp.dnotes.features.manage_category.ui;

public class Deneme {
}
/*
public class BaseViewModel<T extends BaseViewModel.Functions> {

    public Class<T> functions;

    public BaseViewModel() {
        //gson type
        Type type = new TypeToken<T>(){}.getType();
        T functions = (T) Provider.getFunction(type);
    }

    public static class Functions {
        public void eat() {
            System.out.println("Animal eating");
        }
    }

    public static class Functions2 extends Functions {
        public void eat() {
            System.out.println("Animal eating 2");
        }
    }

    public static class Functions3 extends Functions {
        public void eat() {
            System.out.println("Animal eating3");
        }
    }

    public static class Functions4 extends Functions {
        public void eat() {
            System.out.println("Animal eating4");
        }
    }

    public class Provider {
        public static Map<Type, Functions> map = Map.of(
                Functions.class, new Functions(),
                Functions2.class, new Functions2(),
                Functions3.class, new Functions3(),
                Functions4.class, new Functions4()
        );

        public static Functions getFunction(Type functions) {
            return map.get(functions);
        }
        public static  void addFunction(Functions function) {
            map.put(function.getClass(), function);
        }
    }
}
*/