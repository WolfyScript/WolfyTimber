package me.wolfyscript.timber.configs;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.config.Config;
import me.wolfyscript.utilities.api.config.ConfigAPI;
import me.wolfyscript.utilities.api.config.templates.LangConfiguration;
import me.wolfyscript.utilities.api.language.Language;
import me.wolfyscript.utilities.api.language.LanguageAPI;

public class ConfigHandler {

    private WolfyUtilities api;
    private ConfigAPI configAPI;
    private LanguageAPI languageAPI;

    private TimberConfig timberConfig;

    public ConfigHandler(WolfyUtilities api){
        this.api = api;
        this.configAPI = api.getConfigAPI();
        this.languageAPI = api.getLanguageAPI();
    }

    public void load(){
        this.timberConfig = new TimberConfig(configAPI);
        timberConfig.loadDefaults();
        timberConfig.reload();
        loadLang();
    }

    public void loadLang(){
        String lang = timberConfig.getLang();
        Config langConf;
        if (CustomCrafting.getInst().getResource("me/wolfyscript/timber/configs/lang/" + lang + ".yml") != null) {
            langConf = new LangConfiguration(configAPI, lang, "me/wolfyscript/timber/configs/lang", lang, "json", false);
        } else {
            langConf = new LangConfiguration(configAPI, "en_US", "me/wolfyscript/timber/configs/lang", lang, "json", false);
        }
        langConf.loadDefaults();

        languageAPI.registerLanguage(new Language(lang, langConf, configAPI));
    }

    public TimberConfig getTimberConfig(){
        return timberConfig;
    }
}
