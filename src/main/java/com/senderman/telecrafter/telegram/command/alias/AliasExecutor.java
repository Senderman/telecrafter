package com.senderman.telecrafter.telegram.command.alias;

import com.senderman.telecrafter.minecraft.provider.MinecraftProvider;
import org.bukkit.command.CommandException;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AliasExecutor {

    private final MinecraftProvider minecraftProvider;

    public AliasExecutor(MinecraftProvider minecraftProvider) {
        this.minecraftProvider = minecraftProvider;
    }

    public void execute(Alias alias, Predicate<Boolean> hasPermissions, Consumer<String> callback) {
        if (!hasPermissions.test(alias.isAdminOnly())) {
            callback.accept("У вас недостаточно прав для данной команды!");
            return;
        }

        try {
            minecraftProvider.runCommand(alias.getCommand(), result ->
                    callback.accept(result ? "Команда отправлена серверу!" : "Такой команды нет!")
            );
        } catch (CommandException e) {
            callback.accept("Ошибка выполнения команды. См. логи для подробностей");
            e.printStackTrace();
        }
    }
}
