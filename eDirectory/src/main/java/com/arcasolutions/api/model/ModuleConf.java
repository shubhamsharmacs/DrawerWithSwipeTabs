package com.arcasolutions.api.model;

import com.arcasolutions.api.constant.ModuleName;

import java.util.HashMap;
import java.util.List;

import lombok.Data;

@Data
public class ModuleConf extends HashMap<String, List<ModuleFeature>> {

    public ModuleFeature get(ModuleName name, int level) {
        List<ModuleFeature> features = get(name.toString());
        if (features != null) {
            for (ModuleFeature f : features) {
                if (f.getLevel() == level) {
                    return  f;
                }
            }
        }
        return null;
    }

}
