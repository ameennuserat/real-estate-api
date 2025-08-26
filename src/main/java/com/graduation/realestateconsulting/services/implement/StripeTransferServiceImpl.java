package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.entity.Booking;
import com.graduation.realestateconsulting.services.StripeTransferService;
import com.stripe.exception.StripeException;
import com.stripe.model.Transfer;
import com.stripe.param.TransferCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeTransferServiceImpl implements StripeTransferService {

    @Value("${stripe.api.secret-key}")
    private String stripeSecretKey;

    @Value("${platform.fee.percentage}")
    private double platformFeePercentage;

    @Override
    public Transfer transferToExpert(Booking booking) throws StripeException {
        String expertStripeAccountId = booking.getExpert().getStripeAccountId();
        String sourceChargeId = booking.getPaymentChargeId();

        if (expertStripeAccountId == null || expertStripeAccountId.isBlank()) {
            log.error("Cannot create transfer for booking {}: Expert Stripe Account ID is missing.", booking.getId());
            throw new IllegalStateException("Expert Stripe Account ID is missing.");
        }
        if (sourceChargeId == null || sourceChargeId.isBlank()) {
            log.error("Cannot create transfer for booking {}: Source Charge ID is missing.", booking.getId());
            throw new IllegalStateException("Source Charge ID is missing.");
        }

        long totalAmount = booking.getFinalPrice().longValue();
        long platformFee = (long) (totalAmount * (platformFeePercentage / 100.0));
        long amountToTransfer = totalAmount - platformFee;

        log.info("Processing transfer for booking {}: Total={}, PlatformFee={}, NetTransfer={}",
                booking.getId(), totalAmount, platformFee, amountToTransfer);


        TransferCreateParams params = TransferCreateParams.builder()
                .setAmount(amountToTransfer)
                .setCurrency("usd")
                .setDestination(expertStripeAccountId)
                .setSourceTransaction(sourceChargeId)
                .setDescription("Payout for booking #" + booking.getId())
                .build();

        try {
            Transfer transfer = Transfer.create(params);
            log.info("Successfully created Stripe transfer {} for booking {}", transfer.getId(), booking.getId());
            return transfer;
        } catch (StripeException e) {
            log.error("Stripe transfer failed for booking {}: {}", booking.getId(), e.getMessage());
            throw e;
        }
    }
}
