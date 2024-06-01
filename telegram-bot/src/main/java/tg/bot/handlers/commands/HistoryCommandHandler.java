package tg.bot.handlers.commands;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import tg.bot.handlers.Impl.UpdateHandler;
import tg.bot.model.Client;
import tg.bot.model.Order;
import tg.bot.model.enums.CommandBot;
import tg.bot.model.enums.OrderStatus;
import tg.bot.repository.OrderRepository;
import tg.bot.service.ClientService;
import tg.bot.util.ConvertEmojiToCommand;

import java.util.List;

import static tg.bot.util.MessageUtils.sendMessage;

@Slf4j
@Component
public class HistoryCommandHandler implements UpdateHandler {

    private final ConvertEmojiToCommand utilEmoji;
    private final OrderRepository orderRepository;
    private final ClientService clientService;
    private final AbsSender absSender;

    public HistoryCommandHandler(ConvertEmojiToCommand utilEmoji, OrderRepository orderRepository,
                                 ClientService clientService, @Lazy AbsSender absSender) {
        this.utilEmoji = utilEmoji;
        this.orderRepository = orderRepository;
        this.clientService = clientService;
        this.absSender = absSender;
    }

    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            log.info("Получено обновление для чата {} с командой /order_history", chatId);
            try {
                Client client = clientService.findByChatId(chatId)
                        .orElseThrow(() -> new EntityNotFoundException("Клиент не найден!"));
                List<Order> confirmedOrders = orderRepository.findByClientAndStatus(client, OrderStatus.COMPLETED);

                if (!confirmedOrders.isEmpty()) {
                    for (Order order : confirmedOrders) {
                        sendMessage(absSender, chatId, order.toString());
                    }
                    log.info("Отправлены подтвержденные заказы в чат {}", chatId);
                } else {
                    sendMessage(absSender, chatId, "У вас пока нет подтвержденных заказов.");
                    log.info("Нет подтвержденных заказов для чата {}", chatId);
                }
            } catch (EntityNotFoundException e) {
                log.error("Клиент не найден для чата {}", chatId, e);
                sendMessage(absSender, chatId, "Клиент не найден!");
            } catch (Exception e) {
                log.error("Ошибка при обработке команды /order_history для чата {}", chatId, e);
                sendMessage(absSender, chatId, "Произошла ошибка при обработке вашего запроса.");
            }
        } else {
            log.warn("Обновление не содержит сообщения с текстом для команды /order_history");
        }
    }

    @Override
    public boolean canHandleUpdate(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            log.warn("Обновление не может быть обработано, так как оно не содержит сообщения или текста");
            return false;
        }
        String text = update.getMessage().getText();
        String convertedCommand = utilEmoji.convertEmojiToCommand(text);
        return convertedCommand.equalsIgnoreCase("/order_history");
    }

    @Override
    public CommandBot getCommand() {
        return CommandBot.ORDER_HISTORY;
    }
}
