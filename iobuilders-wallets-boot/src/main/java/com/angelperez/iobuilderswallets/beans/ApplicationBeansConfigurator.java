package com.angelperez.iobuilderswallets.beans;

import com.angelperez.iobuilderswallets.adapter.DepositsRepositoryPortAdapter;
import com.angelperez.iobuilderswallets.adapter.MovementsRepositoryPortAdapter;
import com.angelperez.iobuilderswallets.adapter.UsersRepositoryPortAdapter;
import com.angelperez.iobuilderswallets.adapter.WalletsRepositoryPortAdapter;
import com.angelperez.iobuilderswallets.applicationports.DepositsService;
import com.angelperez.iobuilderswallets.applicationports.MovementsService;
import com.angelperez.iobuilderswallets.applicationports.WalletsService;
import com.angelperez.iobuilderswallets.infrastructureports.DepositsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.MovementsRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.UsersRepositoryPort;
import com.angelperez.iobuilderswallets.infrastructureports.WalletsRepositoryPort;
import com.angelperez.iobuilderswallets.mapper.DepositEntitiesMapper;
import com.angelperez.iobuilderswallets.mapper.MovementEntitiesMapper;
import com.angelperez.iobuilderswallets.mapper.WalletEntitiesMapper;
import com.angelperez.iobuilderswallets.r2dbc.repository.DepositsRepository;
import com.angelperez.iobuilderswallets.r2dbc.repository.MovementsRepository;
import com.angelperez.iobuilderswallets.r2dbc.repository.WalletsRepository;
import com.angelperez.iobuilderswallets.service.DepositsServiceImpl;
import com.angelperez.iobuilderswallets.service.MovementsServiceImpl;
import com.angelperez.iobuilderswallets.service.WalletsServiceImpl;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApplicationBeansConfigurator {

    @Value("${users.url}")
    private String usersUrl;

    @Value("${users.path}")
    private String usersPath;

    @Bean
    public WalletsService getWalletsService(WalletsRepositoryPort walletsRepositoryPort, UsersRepositoryPort usersRepositoryPort,
                                            MovementsRepositoryPort movementsRepositoryPort, DepositsRepositoryPort depositsRepositoryPort) {
        return new WalletsServiceImpl(walletsRepositoryPort, usersRepositoryPort, movementsRepositoryPort, depositsRepositoryPort);
    }

    @Bean
    public WalletsRepositoryPort getWalletsRepository(WalletsRepository walletsRepository, WalletEntitiesMapper walletEntitiesMapper) {
        return new WalletsRepositoryPortAdapter(walletsRepository, walletEntitiesMapper);
    }

    @Bean
    public DepositsService getDepositsService(WalletsRepositoryPort walletsRepositoryPort, DepositsRepositoryPort depositsRepositoryPort) {
        return new DepositsServiceImpl(walletsRepositoryPort, depositsRepositoryPort);
    }

    @Bean
    public DepositsRepositoryPort getDepositsRepository(DepositsRepository depositsRepository, DepositEntitiesMapper depositEntitiesMapper) {
        return new DepositsRepositoryPortAdapter(depositsRepository, depositEntitiesMapper);
    }

    @Bean
    public MovementsService getMovementsService(WalletsRepositoryPort walletsRepositoryPort, MovementsRepositoryPort movementsRepositoryPort) {
        return new MovementsServiceImpl(walletsRepositoryPort, movementsRepositoryPort);
    }

    @Bean
    public MovementsRepositoryPort getMovementsRepository(MovementsRepository movementsRepository, MovementEntitiesMapper movementEntitiesMapper) {
        return new MovementsRepositoryPortAdapter(movementsRepository, movementEntitiesMapper);
    }

    @Bean
    public UsersRepositoryPort getUsersRepositoryPort() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        WebClient webClient = WebClient.builder()
            .baseUrl(usersUrl + usersPath)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        return new UsersRepositoryPortAdapter(webClient);
    }
}
