package com.angelperez.iobuilderswallets.beans;

import com.angelperez.iobuilderswallets.adapter.WalletsRepositoryPortAdapter;
import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.mapper.WalletEntitiesMapper;
import com.angelperez.iobuilderswallets.r2dbc.repository.WalletsRepository;
import com.angelperez.iobuilderswallets.service.WalletsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeansConfigurator {

    @Bean
    public WalletsService getWalletsService(WalletsRepositoryPort walletsRepositoryPort) {
        return new WalletsServiceImpl(walletsRepositoryPort);
    }

    @Bean
    public WalletsRepositoryPort getWalletsRepository(WalletsRepository walletsRepository, WalletEntitiesMapper walletEntitiesMapper) {
        return new WalletsRepositoryPortAdapter(walletsRepository, walletEntitiesMapper);
    }
}
