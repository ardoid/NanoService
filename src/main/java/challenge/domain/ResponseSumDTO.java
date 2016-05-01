package challenge.domain;

public class ResponseSumDTO {
    private double sum;

    public ResponseSumDTO(double sum) {
        this.sum = sum;
    }

    public ResponseSumDTO() {
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
