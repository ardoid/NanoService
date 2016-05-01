package challenge.domain;

public class TransactionDTO {
    private double amount;
    private String type;
    private Long parent_id;

    public TransactionDTO(final Transaction transaction) {
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.parent_id = transaction.getParentId();
    }

    public TransactionDTO() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "amount=" + amount +
                ", type='" + type + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }
}
