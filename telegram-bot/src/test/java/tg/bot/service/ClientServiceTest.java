package tg.bot.service;

import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tg.bot.model.Client;
import tg.bot.model.enums.BotState;
import tg.bot.repository.ClientRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByChatId_ExistingClient() {
        long chatId = 123L;
        Client expectedClient = new Client();
        expectedClient.setChatId(chatId);

        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.of(expectedClient));

        Optional<Client> actualClient = clientService.findByChatId(chatId);

        assertEquals(Optional.of(expectedClient), actualClient);
        verify(clientRepository, times(1)).findByChatId(chatId);
    }

    @Test
    public void testFindByChatId_NonExistingClient() {
        long chatId = 123L;

        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        Optional<Client> actualClient = clientService.findByChatId(chatId);

        assertEquals(Optional.empty(), actualClient);

        verify(clientRepository, never()).save(any());
    }

    @Test
    public void testUpdateFirstName_UserExists() {

        long chatId = 123456789;
        String newFirstName = "Amur";
        Client expectedClient = new Client();

        expectedClient.setFirstName("Old Name");
        expectedClient.setChatId(chatId);

        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.of(expectedClient));

        clientService.updateFirstName(chatId, newFirstName);

        verify(clientRepository, times(1)).save(expectedClient);
        assertEquals(newFirstName, expectedClient.getFirstName());

    }

    @Test
    public void testUpdateFirstName_UserNotFound() {
        long chatId = 123456789;
        String newFirstName = "Amur";

        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        clientService.updateFirstName(chatId, newFirstName);

        verify(clientRepository, never()).save(any());
    }

    @Test
    public void testUpdateLastName_UserExists() {
        long chatId = 123456789;
        String newLastName = "Dzarasov";

        Client expectedClient = new Client();
        expectedClient.setChatId(chatId);
        expectedClient.setLastName("Old Name");


        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.of(expectedClient));

        clientService.updateLastName(chatId, newLastName);

        verify(clientRepository, times(1)).save(expectedClient);

        assertEquals(newLastName, expectedClient.getLastName());
    }

    @Test
    public void testUpdateLastName_UserNotFound() {
        long chatId = 123456789;
        String newLastName = "Dzarasov";

        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.empty());

        clientService.updateLastName(chatId, newLastName);

        verify(clientRepository, never()).save(any());

    }

    @Test
    public void updateUserState() {
        long chatId = 123456789;
        BotState newState = BotState.AWAITING_FIRST_NAME;

        Client client = new Client();
        client.setChatId(chatId);
        client.setState(BotState.NONE);
        when(clientRepository.findByChatId(chatId)).thenReturn(Optional.of(client));

        clientService.updateUserState(chatId, newState);

        verify(clientRepository, times(1)).save(client);
        assertEquals(newState, client.getState());
    }
}
