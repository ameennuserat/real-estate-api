package com.graduation.realestateconsulting.services;

import com.graduation.realestateconsulting.model.entity.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;

public interface StripeConnectService {
    Account createAndVerifyCustomAccount(User user) throws StripeException;
}
