package com.example.payment_service_app.processor;

public class PaymentResult {
    private final boolean success;
    private final String providerResponse;
    private PaymentResult(boolean success, String providerResponse) {
        this.success = success; this.providerResponse = providerResponse;
    }
    public boolean isSuccess() { return success; }
    public String getProviderResponse() { return providerResponse; }
    public static PaymentResult success(String resp) { return new PaymentResult(true, resp); }
    public static PaymentResult failed(String resp) { return new PaymentResult(false, resp); }
}