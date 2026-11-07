import java.io.PrintStream;
final class Console {
    static final PrintStream OUT = System.out;
    private Console() { }
    static void title(String text) {
        String line = "=".repeat(Math.max(60, text.length() + 4));
        OUT.println();
        OUT.println(line);
        OUT.println("  " + text);
        OUT.println(line);
    }
    static void step(String text) {
        OUT.println();
        OUT.println("--- " + text + " ---");
    }
    static void info(String text) { OUT.println("  " + text); }
    static void bullet(Object o) { OUT.println("   * " + o); }
}
