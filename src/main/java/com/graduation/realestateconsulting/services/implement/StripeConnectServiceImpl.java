package com.graduation.realestateconsulting.services.implement;

import com.graduation.realestateconsulting.model.entity.User;
import com.graduation.realestateconsulting.services.StripeConnectService;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountUpdateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class StripeConnectServiceImpl implements StripeConnectService {


    @Override
    public Account createAndVerifyCustomAccount(User user) throws StripeException {

        AccountCreateParams.Capabilities capabilities =
                AccountCreateParams.Capabilities.builder()
                        .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder().setRequested(true).build())
                        .setTransfers(AccountCreateParams.Capabilities.Transfers.builder().setRequested(true).build())
                        .build();


            AccountCreateParams createParams = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.CUSTOM)
                    .setCountry("US")
                    .setEmail(user.getEmail())
                    .setCapabilities(capabilities)
                    .setBusinessType(AccountCreateParams.BusinessType.INDIVIDUAL)
                    .build();

            Account account = Account.create(createParams);

            AccountUpdateParams.Individual.Dob dob = AccountUpdateParams.Individual.Dob.builder()
                    .setDay(1L).setMonth(1L).setYear(1990L).build();

            AccountUpdateParams.Individual.Address address = AccountUpdateParams.Individual.Address.builder()
                    .setLine1("123 Main Street")
                    .setCity("San Francisco")
                    .setState("CA")
                    .setPostalCode("94111")
                    .build();

            AccountUpdateParams.Individual individual = AccountUpdateParams.Individual.builder()
                    .setFirstName(user.getFirstName())
                    .setLastName(user.getLastName())
                    .setEmail(user.getEmail())
                    .setDob(dob)
                    .setAddress(address)
                    .setSsnLast4("0000")
                    .build();

            AccountUpdateParams updateParams = AccountUpdateParams.builder()
                    .setIndividual(individual)
                    .setTosAcceptance(
                            AccountUpdateParams.TosAcceptance.builder()
                                    .setDate(Instant.now().getEpochSecond())
                                    .setIp("127.0.0.1")
                                    .build()
                    )
                    .setExternalAccount("btok_us_verified")
                    .build();

            return account.update(updateParams);
        }

}
