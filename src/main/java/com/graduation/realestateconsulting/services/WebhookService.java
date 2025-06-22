package com.graduation.realestateconsulting.services;

import com.stripe.model.Event;

public interface WebhookService {
     void handleFinalPaymentStatus (Event event,String eventType);
    // void handlePaymentSucceeded(Event event);
}
