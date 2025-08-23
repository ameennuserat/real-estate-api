package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.entity.Booking;
import com.stripe.exception.StripeException;
import com.stripe.model.Transfer;

public interface StripeTransferService {

    Transfer transferToExpert(Booking booking) throws StripeException;
}
