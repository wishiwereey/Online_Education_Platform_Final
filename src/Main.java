public final class Main {
    private Main() { }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--menu")) {
            new InteractiveMenu().run();
        } else {
            DemoScenario.run();
        }
    }
}
