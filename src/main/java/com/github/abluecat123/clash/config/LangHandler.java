package com.github.abluecat123.clash.config;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.annotation.RegisterSetting;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;

public class LangHandler {

    @RegisterSetting("lang.execute_command_denied")
    public static String execute_command_denied;
    @RegisterSetting("lang.command_enter_error")
    public static String command_enter_error;
    @RegisterSetting("lang.reload_completed")
    public static String reload_completed;
    @RegisterSetting("lang.console_are_not_allowed_to_perform_this_command")
    public static String console_are_not_allowed_to_perform_this_command;
    @RegisterSetting("lang.this_world_has_been_banned")
    public static String this_world_has_been_banned;
    @RegisterSetting("lang.the_target_is_offline")
    public static String the_target_is_offline;
    @RegisterSetting("lang.money_is_not_enough_to_pay")
    public static String money_is_not_enough_to_pay;
    @RegisterSetting("lang.you_cant_clash_yourself")
    public static String you_cant_clash_yourself;
    @RegisterSetting("lang.clash_main_title_for_sender")
    public static String clash_main_title_for_sender;
    @RegisterSetting("lang.clash_subtitle_for_sender")
    public static String clash_subtitle_for_sender;
    @RegisterSetting("lang.clash_main_title_for_target")
    public static String clash_main_title_for_target;
    @RegisterSetting("lang.clash_subtitle_for_target")
    public static String clash_subtitle_for_target;
    @RegisterSetting("lang.target_is_in_banned_world")
    public static String target_is_in_banned_world;
    @RegisterSetting("lang.prepare_teleport")
    public static String prepare_teleport;
    @RegisterSetting("lang.enemy_has_been_teleported")
    public static String enemy_has_been_teleported;
    @RegisterSetting("lang.bossbar_title")
    public static String bossbar_title;
    @RegisterSetting("lang.clash_win_message")
    public static String clash_win_message;
    @RegisterSetting("lang.clash_lose_message")
    public static String clash_lose_message;
    @RegisterSetting("lang.newbie_not_allowed_to_clash")
    public static String newbie_not_allowed_to_clash;
    @RegisterSetting("lang.newbie_not_allowed_to_be_clashed")
    public static String newbie_not_allowed_to_be_clashed;
    @RegisterSetting("lang.difference_is_too_big")
    public static String difference_is_too_big;
    @RegisterSetting("lang.player_use_banned_command_in_fight")
    public static String player_use_banned_command_in_fight;
    @RegisterSetting("lang.player_use_banned_item_in_fight")
    public static String player_use_banned_item_in_fight;
    @RegisterSetting("lang.target_is_not_existed")
    public static String target_is_not_existed;

    @RegisterSetting("gui.clash_screen_title")
    public static String clash_screen_title;
    @RegisterSetting("gui.skull_title")
    public static String skull_title;
    @RegisterSetting("gui.skull_lore")
    public static @Nullable ArrayList<String> skull_lore;
    @RegisterSetting("gui.confirm_screen_title")
    public static String confirm_screen_title;
    @RegisterSetting("gui.confirm_item_title")
    public static String confirm_item_title;
    @RegisterSetting("gui.confirm_lore")
    public static @Nullable ArrayList<String> confirm_lore;
    @RegisterSetting("gui.turn_page_left")
    public static String turn_page_left;
    @RegisterSetting("gui.turn_page_right")
    public static String turn_page_right;

    @RegisterSetting("help.regularhelp")
    public static @Nullable ArrayList<String> regularhelp;
    @RegisterSetting("help.ophelp")
    public static @Nullable ArrayList<String> ophelp;

    @SuppressWarnings("unchecked")
    public static void reloadLangSetting()
    {
        FileConfiguration config = Clash.getInstance().getLangConfig();
        Class<LangHandler> langClass = LangHandler.class;
        Field[] fields = langClass.getFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(RegisterSetting.class))
            {
                RegisterSetting annotation = field.getAnnotation(RegisterSetting.class);
                try {
                    Object setting = config.get(annotation.value());
                    if (setting instanceof ArrayList)
                    {
                        ArrayList<String> list = (ArrayList<String>) setting;
                        for (int i = 0; i < list.size(); i++) {
                            list.set(i, ChatColor.translateAlternateColorCodes('&', list.get(i)));
                        }
                        field.set(null, list);
                    }
                    else
                    {
                        String str = (String) setting;
                        field.set(null, ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(str)));
                    }
                } catch (Exception e) {
                    Clash.getInstance().getLogger().warning("警告：名为%error_name%的语言项发生配置错误，为不影响使用，请及时修正。".replaceAll("%error_name%", field.getName()));
                    e.printStackTrace();
                }
            }
        }
    }
}
