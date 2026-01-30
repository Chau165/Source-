package java02.group1.productcatalogmanagementsystem.service;

import org.springframework.stereotype.Service;
import java02.group1.productcatalogmanagementsystem.dto.request.AccountRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.LoginRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse register(AccountRequest request);

    AccountResponse login(LoginRequest request);

    void deleteAccount(long accountId);

    AccountResponse getCurrentAccount();
}
