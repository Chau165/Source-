package java02.group1.productcatalogmanagementsystem.service.serviceImpl;

import java02.group1.productcatalogmanagementsystem.dto.request.AccountRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.LoginRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.AccountResponse;
import java02.group1.productcatalogmanagementsystem.entity.Account;
import java02.group1.productcatalogmanagementsystem.entity.Role;
import java02.group1.productcatalogmanagementsystem.exception.exception.AuthenticationException;
import java02.group1.productcatalogmanagementsystem.repository.AccountRepository;
import java02.group1.productcatalogmanagementsystem.repository.RoleRepository;
import java02.group1.productcatalogmanagementsystem.service.AccountService;
import java02.group1.productcatalogmanagementsystem.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AccountResponse register(AccountRequest request) {
        if(accountRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new IllegalArgumentException("Role không hợp lệ (Chưa tồn tại trong DB)"));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setFullName(request.getFullName());
        account.setRole(role);

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    private AccountResponse toResponse(Account account) {
        AccountResponse response = modelMapper.map(account, AccountResponse.class);
        response.setRoleName(account.getRole().getRoleName().name());
        return response;
    }

    @Override
    @Transactional
    public AccountResponse login(LoginRequest request) {
        authenticationConfiguration.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Account account = accountRepository.findAccountByUsername(request.getUsername());
        if (account == null) {
            throw new AuthenticationException("Invalid username or password");
        }

        String token = tokenService.generateToken(account);

        AccountResponse response = toResponse(account);
        response.setToken(token);

        return response;
    }

    @Override
    @Transactional
    public void deleteAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));

        accountRepository.delete(account);
    }

    @Override
    public AccountResponse getCurrentAccount() {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return toResponse(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Không tìm thấy username: " + username);
        }
        return account;
    }
}
