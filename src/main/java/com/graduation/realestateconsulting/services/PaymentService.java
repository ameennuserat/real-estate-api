package com.graduation.realestateconsulting.services;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;

public interface PaymentService {
    PaymentIntent createPaymentIntent(Long bookingId, Long amount) throws StripeException;
    Refund createRefund(String paymentIntentId) throws StripeException;
}
