package com.wenyao.swrasp.context;

import com.wenyao.swrasp.check.*;
import com.wenyao.swrasp.config.RaspConfig;
import com.wenyao.swrasp.context.plugin.PluginUtils;
import com.wenyao.swrasp.context.yaml.YamlConstructor;
import com.wenyao.swrasp.hook.ClassHook;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;

@Slf4j
public class RaspContext {
    private static RaspContext INSTANCE;
    @Getter
    public RaspConfig raspConfig;
    @Getter
    public String version = "1.0.0";
    @Setter
    private Instrumentation instrumentation;
    private final ArrayList<ClassHook> classHookList = new ArrayList<>();

    private final ContextCheck contextCheck = new ContextCheck(this);


    private RaspContext() {

    }

    private void loadConfig() throws IOException {
        log.info("Loading configuration");
        String configFile = System.getProperty("rasp.config");
        if (configFile == null) {
            configFile = System.getenv("rasp.config");
        }
        if (configFile == null) {
            configFile = "rasp.yml";
        }
        File file = new File(configFile);
        if (!file.exists()) {
            log.error("Config file not found: " + configFile);
            log.info("Create Config file");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(new Yaml(YamlConstructor.getConstructor()).dump(new RaspConfig()).getBytes());
            fileOutputStream.close();
        }
        try {
            raspConfig = new Yaml(YamlConstructor.getConstructor()).loadAs(file.toURI().toURL().openStream(), RaspConfig.class);
        } catch (FileNotFoundException e) {
            log.error("Config file not found: " + configFile);
        }
    }

    private void loadHooks() {
        PluginUtils.getPlugins("com.wenyao.swrasp.hook.plugin", ClassHook.class).forEach(plugin -> {
            try {
                ClassHook classHook = plugin.newInstance();
                this.classHookList.add(classHook);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Failed to load plugin: " + plugin.getName(), e);
            }
        });
        log.info("Loaded " + classHookList.size() + " hooks");
    }

    public ArrayList<ClassHook> getClassHookList() {
        return new ArrayList<>(classHookList);
    }

    public synchronized static RaspContext getInstance() {
        if (INSTANCE == null) {
            try {
                RaspContext raspContext = new RaspContext();
                raspContext.loadConfig();
                raspContext.loadHooks();
                INSTANCE = raspContext;
            } catch (Exception e) {
                log.error("Failed to load configuration", e);
                throw new RuntimeException("Failed to load configuration");
            }
        }
        return INSTANCE;
    }

    public static ContextCheck getContextCheck() {
        return getInstance().contextCheck;
    }

}
