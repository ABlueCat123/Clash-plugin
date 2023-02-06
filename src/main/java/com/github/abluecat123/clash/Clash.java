package com.github.abluecat123.clash;

import com.github.abluecat123.clash.command.CommandProcessor;
import com.github.abluecat123.clash.config.ConfigHandler;
import com.github.abluecat123.clash.config.LangHandler;
import com.github.abluecat123.clash.gui.listener.ClashGUIClickListener;
import com.github.abluecat123.clash.gui.listener.ConfirmGUIClickListener;
import com.github.abluecat123.clash.listener.ClashEndListener;
import com.github.abluecat123.clash.listener.PlayerInClashListener;
import com.google.common.base.Charsets;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public final class Clash extends JavaPlugin {

    private static Clash instance;
    public static boolean useVault;
    public static boolean usePAPI;

    private static Economy econ = null;
    private final File langFile = new File(this.getDataFolder(), "lang.yml");
    private FileConfiguration langConfig = null;

    public static Clash getInstance() {
        return instance;
    }
    private static NamespacedKey TARGET_UUID;
    private static NamespacedKey BOSSBAR;
    private static NamespacedKey PAGE;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        saveDefaultLang();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            this.getLogger().warning("警告：没有发现PlaceHolderAPI插件，插件的PAPI支持功能将无法使用。若需使用请安装PlaceHolderAPI");
            usePAPI = false;
        }
        else {
            usePAPI = true;
        }
        if (!setupEconomy()) {
            this.getLogger().warning("警告：没有发现Vault插件，插件的经济部分功能将无法使用。若需使用请安装Vault");
            useVault = false;
        }
        else {
            useVault = true;
        }

        Objects.requireNonNull(Bukkit.getPluginCommand("clash")).setExecutor(new CommandProcessor());
        Objects.requireNonNull(Bukkit.getPluginCommand("clash")).setTabCompleter(new CommandProcessor());

        Bukkit.getPluginManager().registerEvents(new ClashGUIClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ConfirmGUIClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new ClashEndListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInClashListener(), this);

        ConfigHandler.reloadConfigSetting();
        LangHandler.reloadLangSetting();

        TARGET_UUID = new NamespacedKey(this, "clash_target_uuid");
        BOSSBAR = new NamespacedKey(this, "clash_bossbar");
        PAGE = new NamespacedKey(this, "clash_page");

        Clash.getInstance().getLogger().info("Clash插件已加载完成！");
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public void saveDefaultLang() {
        if (!langFile.exists()) {
            saveResource("lang.yml", false);
        }
    }

    public void reloadLang()
    {
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        final InputStream defConfigStream = getResource("lang.yml");
        if (defConfigStream == null) {
            return;
        }

        langConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    @NotNull
    public FileConfiguration getLangConfig() {
        if (langConfig == null) {
            reloadLang();
        }
        return langConfig;
    }

    public static NamespacedKey getTargetUuid() {
        return TARGET_UUID;
    }

    public static NamespacedKey getBOSSBAR() {
        return BOSSBAR;
    }

    public static NamespacedKey getPAGE() {
        return PAGE;
    }

    @Override
    public void onDisable() {

    }

}
