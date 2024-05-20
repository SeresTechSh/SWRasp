package com.wenyao.swrasp.context.yaml;

import com.wenyao.swrasp.config.RaspConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.inspector.TagInspector;

public class YamlConstructor {
    public static Constructor getConstructor() {
        LoaderOptions loaderoptions = new LoaderOptions();
        TagInspector taginspector =
                tag -> tag.getClassName().equals(RaspConfig.class.getName());
        loaderoptions.setTagInspector(taginspector);
        return new Constructor(RaspConfig.class, loaderoptions);
    }
}
