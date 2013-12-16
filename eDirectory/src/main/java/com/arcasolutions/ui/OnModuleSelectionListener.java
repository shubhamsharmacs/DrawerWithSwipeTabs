package com.arcasolutions.ui;

import com.arcasolutions.api.model.Module;

// Module selection listener
public interface OnModuleSelectionListener {
    void onModuleSelected(Module module, int position, long id);
}
