package service;
import java.util.ArrayList;
import java.util.List;
public class PaymentService {
    public record Receipt(String studentId, String description, double amount) { }
    private final List<Receipt> receipts = new ArrayList<>();
    public Receipt charge(String studentId, String description, double amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        Receipt r = new Receipt(studentId, description, Math.round(amount * 100) / 100.0);
        receipts.add(r);
        return r;
    }
    public double totalRevenue() {
        return receipts.stream().mapToDouble(Receipt::amount).sum();
    }
}
