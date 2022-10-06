
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class TgBot extends TelegramLongPollingBot{
public String botUsername = "@WizHapbot";
public String botToken = "5709233914:AAEWAe-_3WiWV8z4zHsIsqKuV-LbFwyBG-I";
    //////////////////////////////////////////Methods////////////////////////////////////////////////////////////////

    /////////////////////////////////////////Private///////////////////////////////////////////////////////////////
    public void initKeyboard()
    {
        //Создаем объект будущей клавиатуры и выставляем нужные настройки
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true); //подгоняем размер
        replyKeyboardMarkup.setOneTimeKeyboard(false); //скрываем после использования

        //Создаем список с рядами кнопок
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        //Создаем один ряд кнопок и добавляем его в список
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        //Добавляем одну кнопку с текстом "Просвяти" наш ряд
        keyboardRow.add(new KeyboardButton("Просвяти"));
        //добавляем лист с одним рядом кнопок в главный объект
        replyKeyboardMarkup.setKeyboard(keyboardRows);
    }
    private void doEntities(Message message){
        Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e-> "bot_command".equals(e.getType())).findFirst();
        String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        for (Currency currency : Currency.values()){
            buttons.add(
                    Arrays.asList(
                            InlineKeyboardButton.builder().text(currency.name()).callbackData(currency.name()).build()

                            // InlineKeyboardButton.builder().text(currency.name()).callbackData("СДЕЛАТЬ ЗАКАЗ").build()
                    )

            );

        }

        switch (command){
            case "/start": {command = "/menu";}
            case "/menu" : {
                try {
                    execute(
                            SendMessage.builder()
                                    .text("Добро пожаловать!")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                    .build()
                    );
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
    /////////////////////////////////////////Public////////////////////////////////////////////////////////////////
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasEntities()){
                doEntities(update.getMessage());
            }
            else if (update.getMessage().hasText()){
                try {
                    execute(SendMessage.builder().text("Человек, я тебя не понимать, лучше зайди в меню и нажми на кнопку").chatId(update.getMessage().getChatId().toString()).build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

            }
        }else if (update.hasCallbackQuery()){
            String call_data = update.getCallbackQuery().getData();

            if (call_data.equals("МЕНЮ")) {
                try {
                    execute(SendMessage.builder()
                            .text("Шаверма:\n" +
                                            "Классическая - 199руб\n" +
                                            "Боярская - 199 руб\n" +
                                            "Сырная - 209руб\n" +
                                            "\n" +
                                            "Бургеры:")
                            .chatId(update.getCallbackQuery().getMessage().getChatId().toString())
                            .build());
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    @Override
    public String getBotUsername(){
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    public static void main(String[] args) {
        TgBot bot = new TgBot();
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}