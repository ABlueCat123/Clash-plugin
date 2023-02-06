package com.github.abluecat123.clash.config;

import com.github.abluecat123.clash.Clash;
import com.github.abluecat123.clash.config.annotation.RegisterSetting;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ConfigHandler {

    @RegisterSetting("teleport_radius")
    public static int teleport_radius;
    @RegisterSetting("price_rate")
    public static double price_rate;
    @RegisterSetting("disabled_world")
    public static @Nullable ArrayList<String> disabled_world;
    @RegisterSetting("countdown_time")
    public static int countdown_time;
    @RegisterSetting("clash_total_time")
    public static int clash_total_time;
    @RegisterSetting("award_rate")
    public static double award_rate;
    @RegisterSetting("newbie_protection.enable")
    public static boolean newbie_protection_enable;
    @RegisterSetting("newbie_protection.protect_time")
    public static int newbie_protection_protect_time;
    @RegisterSetting("difference_protection.enable")
    public static boolean difference_protection_enable;
    @RegisterSetting("difference_protection.different_time")
    public static int difference_protection_different_time;
    @RegisterSetting("banned_command_in_fight")
    public static @Nullable ArrayList<String> banned_command_in_fight;
    @RegisterSetting("banned_item_material_in_fight")
    public static @Nullable ArrayList<String> banned_item_material_in_fight;

    public static void reloadConfigSetting()
    {
        FileConfiguration config = Clash.getInstance().getConfig();
        Class<ConfigHandler> configClass = ConfigHandler.class;
        Field[] fields = configClass.getFields();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(RegisterSetting.class))
            {
                RegisterSetting annotation = field.getAnnotation(RegisterSetting.class);
                try {
                    field.set(null, config.get(annotation.value()));
                } catch (Exception e) {
                    Clash.getInstance().getLogger().warning("警告：名为%error_name%的配置项发生配置错误，为不影响使用，请及时修正。".replaceAll("%error_name%", field.getName()));
                    e.printStackTrace();
                }
            }
        }
    }
}
