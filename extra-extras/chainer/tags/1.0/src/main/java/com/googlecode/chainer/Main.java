package com.googlecode.chainer;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            if (args.length < 2 || args.length > 3) {
                printUsage();
                System.exit(42);
            }
            Chainer chainer = null;
            if (args.length == 2) {
                chainer = new Chainer(args[0], args[1]);
            }
            else {
                chainer = new Chainer(args[0], args[1], Integer.parseInt(args[2]));
            }
            chainer.generate();
        }
        catch (ClassNotFoundException exc) {
            System.err.println("There was a ClassNotFoundException.");
            System.err.println("This probably means that the class for which you are trying");
            System.err.println("to generate a chainer is not on your CLASSPATH.");
            System.err.println("Please adjust it and try again.");
            System.err.println("The class that was not found is: ");
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
            
    }

    private static void printUsage() {
        System.out.print("Usage: java -jar chainer.jar <sourceClassFullName> ");
        System.out.println("<targetClassFullName> [indentSize]");
    }
}
